import org.specs2.mutable._

class L(name: String) {
  def error(s: String) = { s; true }
  def warn(s: String) = { s; true }
  def info(s: String) = { s; true }
}

object L {
  def apply(s: String) = new L(s)
}

class LogSpec extends Specification {

  val a = L("a")
  val ab = L("a.b")
  val abc = L("a.b.c")

  "with simple config" in {

    """
    """ in {

      a.error("a error") must_== true
      ab.error("ab error") must_== true
      abc.error("abc error") must_== true

      a.warn("a warn") must_== true
      ab.warn("ab warn") must_== true
      abc.warn("abc warn") must_== true

      a.info("a info") must_== true
      ab.info("ab info") must_== true
      abc.info("abc info") must_== true

    };

    """
    level.a=INFO
    level.a.b=WARN
    level.a.b.c=ERROR
    """ in {
      a.error("a error") must_== true
      ab.error("ab error") must_== true
      abc.error("abc error") must_== true

      a.warn("a warn") must_== true
      ab.warn("ab warn") must_== true
      abc.warn("abc warn") must_== true

      a.info("a info") must_== true
      ab.info("ab info") must_== true
      abc.info("abc info") must_== true

    };

    """
    a=ERROR
    a.b=WARN
    a.b.c=INFO
    """ in {
      a.error("a error") must_== true
      ab.error("ab error") must_== true
      abc.error("abc error") must_== true

      a.warn("a warn") must_== true
      ab.warn("ab warn") must_== true
      abc.warn("abc warn") must_== true

      a.info("a info") must_== true
      ab.info("ab info") must_== true
      abc.info("abc info") must_== true

    };
    
    
    """
    a=INFO
    a.b=WARN
    a.b.c=INFO
    """ in {
      a.error("a error") must_== true
      ab.error("ab error") must_== true
      abc.error("abc error") must_== true

      a.warn("a warn") must_== true
      ab.warn("ab warn") must_== true
      abc.warn("abc warn") must_== true

      a.info("a info") must_== true
      ab.info("ab info") must_== true
      abc.info("abc info") must_== true

    };

  };

}


