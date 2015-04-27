package actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.{ActorRefRoutee, BroadcastRoutingLogic, Router}

/**
 * Created by kasonchan on 4/24/15.
 */
object Supervisor {
  def props(to: Long): Props = Props(new Supervisor(to))
}

class Supervisor(val to: Long) extends Actor with ActorLogging with Function {

  var router: Router = {

    val routees: Vector[ActorRefRoutee] = Vector.fill(1) {
      val r = context.actorOf(Props[Worker])
      context watch r
      ActorRefRoutee(r)
    }

    Router(BroadcastRoutingLogic(), routees)
  }

  def receive: PartialFunction[Any, Unit] = {

    case s: Search => {
      log.info(s.toString)
      router.route(s, self)
    }
    case pr: PartialResult => {
      //      TODO: Finish up partial result and ranking
      log.info(pr.toString)
    }
    case t: Throwable => {
      log.error(t.getCause, t.getMessage)
    }
    case _ => {
      log.info("???")
    }
  }

}
