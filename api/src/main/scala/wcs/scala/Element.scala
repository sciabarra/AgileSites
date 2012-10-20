package wcs.scala

import wcs.scala._
import wcs.java.{ Element => JElement }
import scala.xml.NodeSeq
import COM.FutureTense.Interfaces.ICS

abstract class Element extends JElement with Log {

  /**
   * Execute the element
   *
   */
  override def exec(ics: ICS): String = {
    //TODO
    return null;
  }
  
  /**
   * This method must not be implemented - the apply(e: wcs.scala.Env) should instead
   */
  def apply(e: wcs.java.Env): String = null
  

  /**
   * Call another element
   */
  def call(name: String, args: Tuple2[Symbol, String]*) = {

    // TODO
  }

  /**
   * Generate an url for an asset specified by id
   */
  def url(id: Tuple2[String, Long]) = {
    
    // TODO
    
  }

  /**
   * Apply the element
   */
  def apply(e: wcs.scala.Env): String
}