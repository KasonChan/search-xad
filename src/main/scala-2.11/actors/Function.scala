package actors

import java.io.{IOException, FileInputStream}

/**
 * Created by kasonchan on 4/25/15.
 */
case class Search(s: String)

case class Status(s: String)

case class Elapsed(t: Long*)

case class ByteCount(b: Long*)

case class PartialResult(worker: String, elapsedTime: Elapsed, byteCount: ByteCount)

case class Result(worker: String, elapsedTime: Elapsed, byteCount: ByteCount, status: Status)

trait Function {

  def read(source: String): Option[Int] = {

    val reader = scala.io.Source.fromFile(source)

//    for (line <- reader.getLines()) {
//
//      if (line.contains("xAd")) {
//        println(line.size)
//      }
//    }

    val byteArray = reader.map(_.toByte).mkString

    println(byteArray)

    reader.close()

    Some(5)
  }

}
