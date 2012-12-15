package wcs.scala.util

trait Common {

  /*
  def ??[A <: AnyRef](opt: Option[A]) = opt match {
    case Some(s) => s.??
    case None => false
  }
  */
  
  def ??(s: AnyRef) = {
    if (s == null)
      false
    else {
      val t = s.toString.toLowerCase
      if (t == "yes" || t == "on" || t == "true")
        true
      else false
    }
  }
}