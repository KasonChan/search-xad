package main

import actors.{Search, Supervisor}
import akka.actor.{ActorRef, ActorSystem, Props}
import util.{Util, ValidArguments}

/**
 * Created by kasonchan on 4/24/15.
 */
object SearchXAd extends Util {

  def main(args: Array[String]) {

    isValidArguments(args) match {
      case Some(ValidArguments(source)) => println(source)
      case Some(ValidArguments(source, time)) => println(source + " " + time)
      case None => System.err.println("Invalid input arguments")
    }

  }

  def executeSearch(source: String, time: Long*) = {
    //    Create a actor system
    val system: ActorSystem = ActorSystem("System")

    val supervisor: ActorRef = system.actorOf(Props[Supervisor], "supervisor")

    println(source)

    supervisor ! Search(source)

    system.shutdown()
    system.awaitTermination()
  }

}
