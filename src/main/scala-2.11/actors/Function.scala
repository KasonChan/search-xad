package actors

import java.io.{FileNotFoundException, FileReader, IOException}
import java.util.Scanner

/**
 * Created by kasonchan on 4/25/15.
 */
case class Search(source: String, search: String)

case class Status(s: String)

case class Elapsed(t: Option[Long])

case class ByteCount(b: Option[Long])

case class PartialResult(worker: String, elapsedTime: Elapsed, byteCount: ByteCount)

case class Result(worker: String, elapsedTime: Elapsed, byteCount: ByteCount, status: Status)

trait Function {

  def read(source: String, search: String): Option[Long] = {

    var found: Boolean = false
    var totalBytes = 0
    var index: Int = -1

    val scanner: Scanner = new Scanner(new FileReader(source))

    try {
      while (scanner.hasNextLine() && !found) {
        totalBytes = totalBytes + scanner.nextLine().length
        index = scanner.nextLine().indexOf(search)
        found = index >= 0
      }
    }
    catch {
      case e: IOException => System.err.println(e.printStackTrace())
      case e: FileNotFoundException => System.err.println(e.printStackTrace())
      case e: Exception => System.err.println(e.printStackTrace())
    }
    finally {
      scanner.close()
    }

    found match {
      case true => Some(totalBytes)
      case false => None
    }
  }

}
