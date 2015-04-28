package actors

import akka.actor.SupervisorStrategy._
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Sorting

/**
 * Created by kasonchan on 4/24/15.
 */
object Supervisor {
  def props(to: Long): Props = Props(new Supervisor(to))
}

/**
 * Supervisor class
 *
 * This class creates and watches 10 workers children actor. Supervisor receives
 * search message from the main program and forwards to its workers. This
 * supervisor handle workers' exception by stopping the workers. After it
 * receives all the works from the workers, it shut down the actor system.
 *
 * @param to timeout: Long
 */
class Supervisor(val to: Long) extends Actor with ActorLogging with Function {

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.minute) {
      case _: ActorKilledException => {
        partialResults = partialResults :+
          PartialResult("", Elapsed(None), ByteCount(None))
        Stop
      }
      case _: ArithmeticException => {
        partialResults = partialResults :+
          PartialResult("", Elapsed(None), ByteCount(None))
        Stop
      }
      case _: NullPointerException => {
        partialResults = partialResults :+
          PartialResult("", Elapsed(None), ByteCount(None))
        Stop
      }
      case _: IllegalArgumentException => {
        partialResults = partialResults :+
          PartialResult("", Elapsed(None), ByteCount(None))
        Stop
      }
      case _: Exception => {
        partialResults = partialResults :+ PartialResult(self.path.name,
          Elapsed(None),
          ByteCount(None))
        Stop
      }
    }

  // Creates 10 workers
  val workers = Vector.fill(10) {
    val r = context.actorOf(Props[Worker])
    context watch r
  }.par

  var partialResults: Array[PartialResult] = Array()

  var terminatedActors: Vector[ActorRef] = Vector()

  override def preStart() = {
    log.debug("Prestart")
  }

  override def postStop() = {
    log.debug("Poststop")
  }

  def receive: PartialFunction[Any, Unit] = {

    case s: Search => {
      log.debug(s.toString)

      for (i <- 0 until workers.size) {
        // Set timeout
        // Ask the workers with the search message
        implicit val timeout = Timeout(to.seconds)
        val partialResultFuture: Future[PartialResult] =
          ask(workers(i), s).mapTo[PartialResult]
      }
    }
    case pr: PartialResult => {
      log.debug(pr.toString)

      partialResults = partialResults :+ pr

      if (partialResults.size == 10) {

        // Construct results from partial results
        val results = partialResults.map { pr =>
          pr.byteCount.b match {
            case Some(b) =>
              Result(pr.worker, pr.elapsedTime, pr.byteCount, Status("SUCCESS"))
            case None =>
              Result(pr.worker, pr.elapsedTime, pr.byteCount, Status("FAILURE"))
          }
        }

        var sum: Double = 0

        Sorting.quickSort(results)(ResultOrdering)

        // Print the elapsed time, bytes count and status of workers in
        // descending order if the worker succeeded; Otherwise, only print
        // status
        for (i <- results.size - 1 to 0 by -1) {
          results(i).status match {
            case Status(v@"SUCCESS") => {
              log.info(results(i).worker + "\t" +
                results(i).elapsedTime.t.getOrElse(0L) + " ms\t" +
                results(i).byteCount.b.getOrElse(0L) + " bytes\t" +
                v)

              //  Convert ms to ns and then calculate time per byte
              sum = sum +
                (results(i).elapsedTime.t.getOrElse(0L) * 1000000L / results(i).byteCount.b.getOrElse(0L))
            }
            case Status(f@"FAILURE") => {
              log.info(results(i).worker + " " + f)
            }
          }
        }

        // Print average time spent per byte read
        log.info("Average time spent per byte: " + "%.4f".format((sum / 10)) + " ns/byte")
      }
    }
    case t: Throwable => {
      log.error(t.getCause, t.getMessage)
    }
    case Terminated(a) => {
      log.debug(a.path.name.toString + " is terminated")

      terminatedActors = terminatedActors :+ a

      // Shut down the actor system when all the workers are terminated
      if (terminatedActors.size == 10)
        context.system.shutdown()
    }
    case x => {
      log.error("Unknown message: " + x)
    }
  }

}
