package mainTest

import org.scalatest.{FlatSpec, MustMatchers}
import util.{Util, ValidArguments}

/**
 * Created by kasonchan on 4/26/15.
 */
class ArgumentsTest extends FlatSpec with MustMatchers with Util {
  "isValidArguments(Array(\"source\")) " must "= Some(ValidArguments(\"source\"))" in {
    isValidArguments(Array("source")) must be(Some(ValidArguments("source", None)))
  }

  "isValidArguments(Array(\"helloWorld\")) " must "= Some(ValidArguments(\"helloWorld\"))" in {
    isValidArguments(Array("helloWorld")) must be(Some(ValidArguments("helloWorld", None)))
  }

  "isValidArguments(Array()) " must "= None" in {
    isValidArguments(Array()) must be(None)
  }

  "isValidArguments(Array(\"source\", \"-h\", \"5\")) " must "= Some(ValidArguments(\"source\", Some(5)))" in {
    isValidArguments(Array("source", "-h", "5")) must be(Some(ValidArguments("source", Some(5))))
  }

  "isValidArguments(Array(\"source\", \"-h\", \"0\")) " must "= None" in {
    isValidArguments(Array("source", "-h", "0")) must be(None)
  }

  "isValidArguments(Array(\"source\", \"-h\", \"X\")) " must "= None" in {
    isValidArguments(Array("source", "-h", "X")) must be(None)
  }

  "isValidArguments(Array(\"source\", \"-h\", \"50.5\")) " must "= None" in {
    isValidArguments(Array("source", "-h", "50.5")) must be(None)
  }

  "isValidArguments(Array(\"-h\", \"120\", \"source\")) " must "= Some(ValidArguments(\"source\", Some(120)))" in {
    isValidArguments(Array("-h", "120", "source")) must be(Some(ValidArguments("source", Some(120))))
  }

  "isValidArguments(Array(\"-h\", \"-5\", \"source\")) " must "= None" in {
    isValidArguments(Array("-h", "-5", "source")) must be(None)
  }

  "isValidArguments(Array(\"-h\", \"A\", \"source\")) " must "= None" in {
    isValidArguments(Array("-h", "A", "source")) must be(None)
  }

  "isValidArguments(Array(\"-h\", \"120.7\", \"source\")) " must "= None" in {
    isValidArguments(Array("-h", "120.7", "source")) must be(None)
  }

  "isValidArguments(Array(\"source\", \"source\", \"source\")) " must "= None" in {
    isValidArguments(Array("source", "source", "source")) must be(None)
  }

  "isValidArguments(Array(\"source\", \"source\")) " must "= None" in {
    isValidArguments(Array("source", "source")) must be(None)
  }

  "isValidArguments(Array(\"source\", \"source\", \"source\", \"source\")) " must "= None" in {
    isValidArguments(Array("source", "source", "source", "source")) must be(None)
  }
}
