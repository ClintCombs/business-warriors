
package buswarriors

import akka.actor.{ActorSelection, ActorSystem}

package object actors {

  case class GetProduct(sku: String)
  case class GetProductResponse(sku: String, product: Option[Product])

  object ActorNames {
    val catalog = "catalogActor"
  }
}
