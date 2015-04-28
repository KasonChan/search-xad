package main

import actors.{Search, Supervisor}
import akka.actor.{ActorRef, ActorSystem}
import util.{Util, ValidArguments}

/**
 * Created by kasonchan on 4/24/15.
 */
object SearchXAd extends Util {

  def main(args: Array[String]) {

    // Call function isValidArguments to validate command line arguments
    isValidArguments(args) match {
      case Some(ValidArguments(source, None)) => executeSearch(source, None)
      case Some(ValidArguments(source, to@Some(timeout))) => executeSearch(source, to)
      case None => System.err.println("Invalid input arguments")
    }

  }

  def executeSearch(source: String, timeoutOption: Option[Long]) = {

    // Create a actor system called system
    val system: ActorSystem = ActorSystem("system")

    val search: String = "xAd"

    // The default timeout will be set to 60 to the supervisor if there is no
    // timeout input command-line argument
    val defaultTimeout: Int = 60

    val timeout: Long = timeoutOption.getOrElse(defaultTimeout)

    val supervisor: ActorRef =
      system.actorOf(Supervisor.props(timeout), "supervisor")

    // Send the search message to supervisor
    supervisor ! Search(source, search)

    // Wait for the system to shutdown
    system.awaitTermination()
  }

}
