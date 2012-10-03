package wcs.util.log

class LogFormatter {

  private val sdf = new java.text.SimpleDateFormat("HH:mm:ss")

  def format(time: Long, name: String, level: String, message: String ): String = {
    "%s %s: %s - %s".format(sdf.format(time),level, name, message)
  }

}