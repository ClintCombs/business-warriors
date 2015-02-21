
package buswarriors

import akka.actor.ActorSystem
import akka.event.Logging
import akka.util.Timeout
import buswarriors.actors.{GetProductResponse, GetProduct, ActorPaths}
import buswarriors.metrics.Metrics
import com.codahale.metrics.MetricRegistry
import spray.http.HttpResponse
import spray.routing.{ExceptionHandler, SimpleRoutingApp}
import spray.util.LoggingContext
import spray.http.StatusCodes._
import spray.http.MediaTypes._
import akka.pattern.ask
import scala.concurrent.duration._

class BusinessWarriorRouting(val registry: MetricRegistry, val serverPort: Int)(implicit system: ActorSystem)
extends SimpleRoutingApp with Metrics with JsonRendering with ActorPaths {

  implicit val dispatcher = system.dispatcher

  val logger = Logging(system, this.toString)

  implicit val askTimeout = Timeout(500.milliseconds)

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
                  val product = Product(sku = sku, name = s"p-$sku", price = 10.00)
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
                  Product(sku = sku, name = s"p-$sku", price = 10.00)
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
                for { response <- (catalogActor ? GetProduct(sku)).mapTo[GetProductResponse] } yield {
                  response.product match {
                    case Some(p) => HttpResponse(OK, renderProduct(p))
                    case None => HttpResponse(NotFound, s"""{ "error": ${response.sku} not found" }""")
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
