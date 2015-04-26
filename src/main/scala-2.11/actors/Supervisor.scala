package actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * Created by kasonchan on 4/24/15.
 */
class Supervisor extends Actor with ActorLogging with Function {

  val worker1 = context.actorOf(Props[Worker])
  context watch worker1

  def receive: PartialFunction[Any, Unit] = {
    case s: Search => {
      implicit val timeout = Timeout(60 milliseconds)
      val worker1Result: Future[Result] = ask(worker1, s).mapTo[Result]

      worker1Result.onSuccess {
        case r: Result => log.info(r.toString)
      }

      worker1Result.onFailure {
        case e: Exception => log.error(e.getMessage)
      }

    }
    case pr: PartialResult => {
      log.info(pr.toString)
    }
    case t: Throwable => log.error(t.getCause, t.getMessage)
    case _ => log.info("???")
  }

}
