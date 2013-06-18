package wcs.scala.util

trait Specification {

  var level = 1
  val output = new StringBuilder
  override def toString = output.toString

  class In(s: String) {

    def in(f: => Unit) = {
      println(("=" * level) + " " + s)
      level = level + 1
      try {
        //println("qui...")
        f
        //println("e qua")
      } catch {
        case e: Throwable => println("??? Unexpected Exception: " + e)
      }
      level = level - 1
    }
  }

  class Must(_a: => Any) {

    val (a, ae) = try {
      (_a, null)
    } catch {
      case e: Throwable => (null, e)
    }

    def must_==(b: Any) {
      if (a != b) {
        println("!!! expected: " + a)
        println("??? found   : " + b)
      } else {
        //println("Ok.")
      }
    }

    def must_!=(b: Any) {
      if (a == b) {
        println("!!! expected: " + a)
        println("??? found   : " + b)
      } else {
        //println("Ok.")
      }
    }

    def must(b: Throwable) {

      if (ae == null) {
        println("!!! Expected Exception " + b.getClass)
        println("??? no exception thrown")
      } else {
        if (b.getClass == ae.getClass()) {
          //println("Ok.")
        } else {
          println("!!! Expected Exception " + b.getClass)
          println("??? Found    Exception " + ae.getClass)
        }
      }
    }
  }

  implicit def s2in(s: String) = new In(s)

  implicit def a2must(a: => Any) = new Must(a)

  def println(s: Any) {
    //Console.println(s)
    output.append(s.toString()).append("\n")
  }

  def throwA[T <: Throwable](implicit m: scala.reflect.Manifest[T]) = {
    m.erasure.newInstance.asInstanceOf[T]
  }

}