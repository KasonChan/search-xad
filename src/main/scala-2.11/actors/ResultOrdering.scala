package actors

/**
 * Created by kasonchan on 4/28/15.
 */
object ResultOrdering extends Ordering[Result] {

  /**
   * Compare results
   *
   * @param r1 r1: Result
   * @param r2 r2: Result
   * @return Integer
   */
  def compare(r1: Result, r2: Result): Int =
    r1.elapsedTime.t.getOrElse(0L) compare r2.elapsedTime.t.getOrElse(0L)
}
