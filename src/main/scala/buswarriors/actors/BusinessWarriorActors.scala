
package buswarriors.actors

import akka.actor.{ActorRef, ActorSystem}

case class BusinessWarriorActorRefs(catalogActor: ActorRef)

object BusinessWarriorActors {
  def build()(implicit system: ActorSystem): BusinessWarriorActorRefs = {
    val catalogActorRef = system.actorOf(CatalogActor.props(), ActorNames.catalog)
    BusinessWarriorActorRefs(catalogActorRef)
  }
}
