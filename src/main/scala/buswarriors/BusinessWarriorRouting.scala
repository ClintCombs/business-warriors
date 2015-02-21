
package buswarriors

import akka.actor.ActorSystem
import akka.event.Logging
import buswarriors.metrics.Metrics
import com.codahale.metrics.MetricRegistry
import spray.http.HttpResponse
import spray.routing.{ExceptionHandler, SimpleRoutingApp}
import spray.util.LoggingContext
import spray.http.StatusCodes._
import spray.http.MediaTypes._

class BusinessWarriorRouting(val registry: MetricRegistry, val serverPort: Int)(implicit system: ActorSystem)
extends SimpleRoutingApp with Metrics with JsonRendering {

  implicit val dispatcher = system.dispatcher

  val logger = Logging(system, this.toString)

  def start() = {
    startServer(interface = "0.0.0.0", port = serverPort)(route)
  }

  def generalPurposeExceptionHandler(implicit log: LoggingContext) = ExceptionHandler {
    case ex: Exception => {
      requestUri { uri =>
        log.error(ex, s"request to ${uri} failed")
        complete(InternalServerError, "")
      }
    }
  }

  def route = {
    handleExceptions(generalPurposeExceptionHandler) {
      path("") {
        get {
          respondWithMediaType(`application/json`) {
            complete {
              HttpResponse(OK, renderRoot)
            }
          }
        }
      } ~
      pathPrefix("inventory") {
        pathEndOrSingleSlash {
          get {
            respondWithMediaType(`application/json`) {
              complete {
                val productInventory = List("123", "321", "456") map { sku =>
                  val product = Product(sku = "321", name = s"p-321", price = 10.00)
                  ProductInventory(product = product, quantity = 3)
                }
                HttpResponse(OK, renderInventoryList(productInventory: _*))
              }
            }
          }
        } ~
        path(Segment) { sku =>
          get {
            respondWithMediaType(`application/json`) {
              complete {
                val product = Product(sku = sku, name = s"p-$sku", price = 10.00)
                val productInventory = ProductInventory(product = product, quantity = 3)
                HttpResponse(OK, renderInventory(productInventory))
              }
            }
          }
        }
      } ~
      pathPrefix("products") {
        pathEndOrSingleSlash {
          get {
            respondWithMediaType(`application/json`) {
              complete {
                val products = List("123", "321", "456") map { sku =>
                  Product(sku = "321", name = s"p-321", price = 10.00)
                }
                HttpResponse(OK, renderProducts(products: _*))
              }
            }
          }
        } ~
        path(Segment) { sku =>
          get {
            respondWithMediaType(`application/json`) {
              complete {
                val product = Product(sku = sku, name = s"p-$sku", price = 10.00)
                HttpResponse(OK, renderProduct(product))
              }
            }
          }
        }
      }
    }
  }
}
