package wcs.scala

import scala.annotation.elidable
import scala.annotation.elidable._

import java.util.logging.{ Logger => JLogger }

trait Log {

  @elidable(FINEST) implicit val _name = this.getClass.getCanonicalName

  @elidable(FINEST) val _handler = new wcs.scala.util.LogHandler

  /** Format a string using params, if any, otherwise use the string as-is */
  @inline protected final def format(fmt: String, params: Seq[Any]) = {
    if (params.nonEmpty) fmt.format(params: _*) else fmt
  }

  @elidable(FINEST) def dump(message: String, params: Any*) {
    _handler.dump(format(message, params))
  }

  @elidable(FINER) def trace(message: String, params: Any*) {
    _handler.trace(format(message, params))
  }

  @elidable(FINER) def debug(message: String, params: Any*) {
    _handler.debug(format(message, params))
  }

  @elidable(FINER) def info(message: String, params: Any*) {
    _handler.info(format(message, params))
  }

  @elidable(FINER) def warn(message: String, params: Any*) {
    _handler.warn(format(message, params))
  }

  @elidable(SEVERE) def error(message: String, params: Any*) {
    _handler.error(format(message, params))
  }

  /**
   * Passthrough logger function for tests
   */

  def log[T](msg: String, t: T): T = {
    if (t != null)
      info(msg + " " + t.toString)
    else
      info(msg + " (null)")
    t
  }

  /**
   * Disabled passthrough
   */
  def log[T](t: T): T = log("", t)

  def nlog[T](msg: String, t: T): T = t

  def nlog[T](t: T): T = t

}
