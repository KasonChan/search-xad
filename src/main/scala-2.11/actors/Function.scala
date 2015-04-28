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

  /**
   * Read lines from the source file and compare with the search string.
   * Returns Some of total bytes read if the search string is existed; Otherwise,
   * return None
   *
   * @param source The source to search from: String
   * @param search The string to be search: String
   * @return Some of total bytes read if the search string is existed; Otherwise,
   *         return None
   */
  def read(source: String, search: String): Option[Long] = {

    var found: Boolean = false
    var totalBytes: Long = 0

    val scanner: Scanner = new Scanner(new FileReader(source))

    try {

      while (scanner.hasNextLine() && !found) {

        var line: String = scanner.nextLine()

        // Accumlate the total bytes searched
        totalBytes = totalBytes + line.length

        if (line.contains(search)) {
          found = true
          return Some(totalBytes)
        }
      }

      return None
    }
    catch {
      case e: IOException =>
        System.err.println(e.printStackTrace())
        None
      case e: FileNotFoundException =>
        System.err.println(e.printStackTrace())
        None
      case e: Exception =>
        System.err.println(e.printStackTrace())
        None
    }
    finally {
      scanner.close()
    }
  }

}
