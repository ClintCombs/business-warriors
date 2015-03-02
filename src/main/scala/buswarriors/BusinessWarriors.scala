
package buswarriors

import akka.actor.ActorSystem
import akka.event.Logging
import buswarriors.actors.BusinessWarriorActors
import buswarriors.catalog.CSVCatalogLoader
import buswarriors.metrics.Jmx
import com.codahale.metrics.MetricRegistry

import scala.util.{Failure, Success, Try}

object BusinessWarriors extends App {

  implicit val system = ActorSystem()

  implicit val logger = Logging(system, this.toString)

  // TODO: parse command-line with scopt
  // TODO: load config with Typesafe Config

  run() match {
    case Success(u) => println("startup complete")
    case Failure(ex) => ex.printStackTrace()
  }

  def run(): Try[Unit] = Try {
    val registry = new MetricRegistry()

    logger.info("starting JMX metrics...")
    new Jmx(registry).start()
    logger.info("JMX metrics up and running.")

    BusinessWarriorActors.build()

    val catalog = CSVCatalogLoader.load("products.csv")

    logger.info("Starting HTTP service...")
    val serverPort = 9812
    val routing = new BusinessWarriorRouting(registry, serverPort, catalog)
    routing.start()
    logger.info(s"HTTP service started on port ${serverPort}.")
  }
}
