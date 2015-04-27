package util

/**
 * Created by kasonchan on 4/26/15.
 */

case class ValidArguments(source: String, timeout: Option[Long])

trait Util {

  /**
   * Validate arguments
   *
   * If there is only 1 argument, it is assumed to be the source and wil be
   * returned as valid argument.
   * If there are 3 arguments and in the form "-h timeout source" or
   * "source -h timeout", the function will then validate the timeout argument.
   * If the timeout is valid position integer, source and timeout will be
   * returned as valid arguments.
   * Otherwise, return None.
   *
   * @param args Array[String]
   * @return Some(ValidArguments) if the arguments are valid; None otherwise
   */
  def isValidArguments(args: Array[String]): Option[ValidArguments] = {

    val timeoutPattern = """([1-9][0-9]*)"""

    args match {
      case Array(source) =>
        Some(ValidArguments(source, None))
      case Array("-h", timeout, source) =>
        if (args(1).matches(timeoutPattern))
          Some(ValidArguments(source, Some(timeout.toLong)))
        else
          None
      case Array(source, "-h", timeout) =>
        if (args(2).matches(timeoutPattern))
          Some(ValidArguments(source, Some(timeout.toLong)))
        else
          None
      case _ =>
        None
    }
  }
}
