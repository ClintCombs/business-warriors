
package buswarriors.actors

import akka.actor.{ActorSelection, ActorSystem}

trait ActorPaths {
  def catalogActor(implicit system: ActorSystem): ActorSelection = {
    system.actorSelection(s"/user/${ActorNames.catalog}")
  }
}
