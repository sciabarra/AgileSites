package wcs.scala.util

class Logger {

}

class LogFormatter {

  private val sdf = new java.text.SimpleDateFormat("HH:mm:ss")

  def format(time: Long, name: String, level: String, message: String): String = {
    "%s %s: %s - %s".format(sdf.format(time), level, name, message)
  }
}

class LogHandler {
  val fmt = new LogFormatter

  def dump(message: String)(implicit name: String) {
    Console.err.println(fmt.format(System.currentTimeMillis(), name, " DUMP", message))
  }
  def trace(message: String)(implicit name: String) {
    Console.err.println(fmt.format(System.currentTimeMillis(), name, "TRACE", message))
  }
  def debug(message: String)(implicit name: String) {
    Console.err.println(fmt.format(System.currentTimeMillis(), name, "DEBUG", message))
  }
  def info(message: String)(implicit name: String) {
    Console.err.println(fmt.format(System.currentTimeMillis(), name, " INFO", message))
  }
  def warn(message: String)(implicit name: String) {
    Console.err.println(fmt.format(System.currentTimeMillis(), name, " WARN", message))
  }
  def error(message: String)(implicit name: String) {
    Console.err.println(fmt.format(System.currentTimeMillis(), name, "ERROR", message))
  }

}