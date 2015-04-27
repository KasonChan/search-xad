package main

import actors.{Search, Supervisor}
import akka.actor.{ActorRef, ActorSystem}
import util.{Util, ValidArguments}

/**
 * Created by kasonchan on 4/24/15.
 */
object SearchXAd extends Util {

  def main(args: Array[String]) {

    //    Call function isValidArguments to validate command line arguments
    isValidArguments(args) match {
      case Some(ValidArguments(source, None)) => executeSearch(source, None)
      case Some(ValidArguments(source, to@Some(timeout))) => executeSearch(source, to)
      case None => System.err.println("Invalid input arguments")
    }

  }

  def executeSearch(source: String, timeoutOption: Option[Long]) = {

    // Create a actor system
    val system: ActorSystem = ActorSystem("System")

    val search: String = "xAd"

    // The default timeout will be set to 60 to the Supervisor if there is no
    // timeout input argument
    val defaultTimeout: Int = 60

    val supervisor: ActorRef =
      system.actorOf(Supervisor.props(timeoutOption.getOrElse(defaultTimeout)),
        "Supervisor")

    //    Send the search message to supervisor
    supervisor ! Search(source, search)

    system.shutdown()
    system.awaitTermination()
  }

}
