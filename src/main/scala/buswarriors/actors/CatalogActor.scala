
package buswarriors.actors

import akka.actor.{Props, Actor}
import akka.event.Logging
import buswarriors.Product

object CatalogActor {
  def props(): Props = {
    Props(new CatalogActor())
  }
}

class CatalogActor() extends Actor {

  implicit val system = context.system

  val logger = Logging(system, this.toString)

  def receive = {
    case GetProduct(sku) => {
      logger.info(s"get ${sku}")
      sender() ! GetProductResponse(sku, Some(Product(sku, s"p-$sku", 1.0)))
    }
    case msg => logger.error(s"unknown message received: ${msg}")
  }
}
