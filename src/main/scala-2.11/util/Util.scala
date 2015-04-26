package util

/**
 * Created by kasonchan on 4/26/15.
 */

case class ValidArguments(source: String, time: Long*)

trait Util {

  /**
   * Validate arguments
   *
   * If there is only 1 argument, it is assumed to be the source and wil be
   * returned as valid argument.
   * If there are 3 arguments and in the form -h time source or source -h time,
   * the function will then validate the time argument. If the time is valid as
   * in position integer, source and time will be returned as valid arguments.
   * Otherwise, return None.
   *
   * @param args Array[String]
   * @return Some(ValidArguments) if the arguments are valid; None otherwise
   */
  def isValidArguments(args: Array[String]): Option[ValidArguments] = {
    val timePattern = """([1-9][0-9]*)"""

    args match {
      case Array(source) =>
        Some(ValidArguments(source))
      case Array(h@"-h", time, source) =>
        if (args(1).matches(timePattern))
          Some(ValidArguments(source, time.toLong))
        else
          None
      case Array(source, h@"-h", time) =>
        if (args(2).matches(timePattern))
          Some(ValidArguments(source, time.toLong))
        else
          None
      case _ =>
        None
    }
  }
}
