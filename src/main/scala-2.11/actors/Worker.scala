package actors

import akka.actor.{Actor, ActorLogging}

/**
 * Created by kasonchan on 4/25/15.
 */
class Worker extends Actor with ActorLogging with Function {

  def receive: PartialFunction[Any, Unit] = {

    case Search(source: String, search: String) => {
      log.info(source)

      val initialTime: Long = System.currentTimeMillis

      //      Search the string from source
      val byteCount = read(source, search)

      val elapsedTime: Long = System.currentTimeMillis - initialTime

      log.debug("Time elapsed: " + elapsedTime.toString + " ms")

      val partialResult = PartialResult(self.path.name,
        Elapsed(Some(elapsedTime)),
        ByteCount(byteCount))
      log.info(partialResult.toString)
    }
    case t: Throwable => {
      log.error(t.getCause, t.getMessage)
    }
    case _ => {
      log.info("???")
    }
  }

}
