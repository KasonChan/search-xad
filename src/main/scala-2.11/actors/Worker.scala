package actors

import akka.actor.{Actor, ActorLogging}

/**
 * Created by kasonchan on 4/25/15.
 */
class Worker extends Actor with ActorLogging with Function {

  def receive: PartialFunction[Any, Unit] = {
    case Search(source: String) => {
      log.info(source)

      val initialTime: Long = System.currentTimeMillis

      //      Read from source from source
      val byteCount = read(source)

      val elapsedTime: Long = System.currentTimeMillis - initialTime

      log.info(elapsedTime.toString)

      println(byteCount.getOrElse(-1))

      val partialResult = PartialResult(self.path.name,
        Elapsed(elapsedTime),
        ByteCount(4))

      log.info(partialResult.toString)

      sender() ! partialResult
    }
    case t: Throwable => log.error(t.getCause, t.getMessage)
    case _ => log.info("???")
  }

}
