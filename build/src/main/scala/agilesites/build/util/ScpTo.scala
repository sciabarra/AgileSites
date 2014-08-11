/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate the file transfer from local to remote.
 *   $ CLASSPATH=.:../build javac ScpTo.java
 *   $ CLASSPATH=.:../build java ScpTo file1 user@remotehost:file2
 * You will be asked passwd.
 * If everything works fine, a local file 'file1' will copied to
 * 'file2' on 'remotehost'.
 *
 */
package agilesites.build.util

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session

object ScpTo {

  def scp(lfile: String, user: String, pass: String, host: String, port: Int, rfile: String): Boolean = {

    var fis: FileInputStream = null;
    try {

      val jsch = new JSch();
      val session = jsch.getSession(user, host, port);

      session.setConfig("StrictHostKeyChecking", "no");
      session.setPassword(pass);
      session.connect();

      val ptimestamp = true;

      // exec 'scp -t rfile' remotely
      var command = "scp " + (if (ptimestamp) "-p" else "") + " -t " + rfile;

      val channel = session.openChannel("exec");
      channel.asInstanceOf[ChannelExec].setCommand(command);

      // get I/O streams for remote scp
      val out = channel.getOutputStream();
      val in = channel.getInputStream();

      channel.connect();

      if (checkAck(in) != 0) {
        return false;
      }

      val _lfile = new File(lfile);

      if (ptimestamp) {
        command = "T " + (_lfile.lastModified() / 1000) + " 0";
        // The access time should be sent here,
        // but it is not accessible with JavaAPI ;-<
        command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
        out.write(command.getBytes()); out.flush();
        if (checkAck(in) != 0) {
          return false;
        }
      }

      // send "C0644 filesize filename", where filename should not include '/'
      val filesize = _lfile.length();
      command = "C0644 " + filesize + " ";
      if (lfile.lastIndexOf('/') > 0) {
        command += lfile.substring(lfile.lastIndexOf('/') + 1);
      } else {
        command += lfile;
      }
      command += "\n";
      out.write(command.getBytes()); out.flush();
      if (checkAck(in) != 0) {
        return false;
      }

      // send a content of lfile
      fis = new FileInputStream(lfile);
      val buf = new Array[Byte](1024);
      var len = 1 // dummy
      while (len > 0) {
        len = fis.read(buf, 0, buf.length);
        if (len > 0)
          out.write(buf, 0, len); //out.flush();
      }
      fis.close();
      fis = null;
      // send '\0'
      buf(0) = 0; out.write(buf, 0, 1); out.flush();
      if (checkAck(in) != 0) {
        return false;
      }
      out.close();

      channel.disconnect();
      session.disconnect();

      return true;
    } catch {
      case e: Exception =>
        System.out.println(e);
        try { if (fis != null) fis.close() } catch { case t: Throwable => }
        return false;
    }
  }

  def checkAck(in: InputStream): Int = {
    val b: Int = in.read();
    // b may be 0 for success,
    //          1 for error,
    //          2 for fatal error,
    //          -1
    if (b == 0) return b;
    if (b == -1) return b;

    if (b == 1 || b == 2) {
      val sb = new StringBuffer();
      var c: Int = -1;
      while (c != '\n') {
        c = in.read();
        sb.append(c.toChar);
      }

      if (b == 1) { // error
        System.out.print(sb.toString());
      }
      if (b == 2) { // fatal error
        System.out.print(sb.toString());
      }
    }
    return b;
  }
}
