package wcs.scala
import COM.FutureTense.Interfaces.ICS

object Presentation {
  def apply(pres: String, attr: String)(implicit e: Env) = {
    val v = new wcs.java.util.Presentation(e.ics).get(pres, attr)
    if (v == null || e.isError())
      None
    else Some(v)
  }
}