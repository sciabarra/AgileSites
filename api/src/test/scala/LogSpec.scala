import org.specs2.mutable._
//import wcs.Log

class Log {
  val name = getClass.getCanonicalName()
  def error(s: String) = { s; true }
  def warn(s: String) = { s; true }
  def info(s: String) = { s; true }
}


package a {
  class A extends Log
  package b {
    class B extends Log
    package c {
      class C extends Log
    }
  }
}

class LogSpec extends Specification {

  val _a = new a.A
  val _ab = new a.b.B
  val _abc = new a.b.c.C

  "with simple config" in {

    """
    _=WARN
    """ in {

      _a.error("a error") must_== true
      _ab.error("ab error") must_== true
      _abc.error("abc error") must_== true

      _a.warn("a warn") must_== true
      _ab.warn("ab warn") must_== true
      _abc.warn("abc warn") must_== true

      _a.info("a info") must_== true
      _ab.info("ab info") must_== true
      _abc.info("abc info") must_== true

    };

    """
    a=INFO
    a.b=WARN
    a.b.c=ERROR
    """ in {
      _a.error("a error") must_== true
      _ab.error("ab error") must_== true
      _abc.error("abc error") must_== true

      _a.warn("a warn") must_== true
      _ab.warn("ab warn") must_== true
      _abc.warn("abc warn") must_== true

      _a.info("a info") must_== true
      _ab.info("ab info") must_== true
      _abc.info("abc info") must_== true

    };

    """
    a=ERROR
    a.b=WARN
    a.b.c=INFO
    """ in {
      _a.error("a error") must_== true
      _ab.error("ab error") must_== true
      _abc.error("abc error") must_== true

      _a.warn("a warn") must_== true
      _ab.warn("ab warn") must_== true
      _abc.warn("abc warn") must_== true

      _a.info("a info") must_== true
      _ab.info("ab info") must_== true
      _abc.info("abc info") must_== true

    };

    """
    a=INFO
    a.b=WARN
    a.b.c=INFO
    """ in {
      _a.error("a error") must_== true
      _ab.error("ab error") must_== true
      _abc.error("abc error") must_== true

      _a.warn("a warn") must_== true
      _ab.warn("ab warn") must_== true
      _abc.warn("abc warn") must_== true

      _a.info("a info") must_== true
      _ab.info("ab info") must_== true
      _abc.info("abc info") must_== true

    };

  };

}


