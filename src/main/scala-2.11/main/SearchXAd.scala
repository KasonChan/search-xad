package main

import actors.{Search, Supervisor}
import akka.actor.{ActorRef, ActorSystem, Props}

/**
 * Created by kasonchan on 4/24/15.
 */
object SearchXAd {

  def main(args: Array[String]) {

    //    Create a actor system
    val system: ActorSystem = ActorSystem("System")

    val supervisor: ActorRef = system.actorOf(Props[Supervisor], "supervisor")

    println(args(0).toString)

    supervisor ! Search(args(0).toString)

    system.shutdown()
    system.awaitTermination()
  }

}
