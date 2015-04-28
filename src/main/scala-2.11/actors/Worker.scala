package actors

import akka.actor.{Actor, ActorLogging}

/**
 * Created by kasonchan on 4/25/15.
 */
/**
 * Worker class
 *
 * Worker receives the search message and search from the source. After search
 * is executed, it sends the partial result back to it's supervisor parent.
 * If there is any exception happens, it will ask it's supervisor parent for
 * supervision. Otherwise, it will stop itself after sending back the partial
 * result.
 */
class Worker extends Actor with ActorLogging with Function {

  override def preStart() = {
    log.debug("Prestart")
  }

  override def postStop() = {
    log.debug("Poststop")
  }

  def receive: PartialFunction[Any, Unit] = {

    case Search(source: String, search: String) => {
      log.debug(source)

      val initialTime: Long = System.currentTimeMillis

      // Search the string from source
      val byteCount = read(source, search)

      val elapsedTime: Long = System.currentTimeMillis - initialTime

      log.debug("Time elapsed: " + elapsedTime.toString + " ms")

      // Print error message if the search string is not found
      byteCount match {
        case Some(b) =>
        case None => log.error(search + " is not found in " + source)
      }

      // Construct partial result
      val partialResult = PartialResult(self.path.name,
        Elapsed(Some(elapsedTime)),
        ByteCount(byteCount))

      log.debug(partialResult.toString)

      context.parent ! partialResult

      context.stop(self)
    }
    case t: Throwable => {
      log.error(t.getCause, t.getMessage)
    }
    case x => {
      log.error("Unknown message: " + x)
    }
  }

}
