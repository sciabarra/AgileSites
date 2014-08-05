package wcs.build

import javax.swing._

object Dialog {

 def inputDialog(prompt: String, defaultValue: String): Option[String] = {
    val jf = new JFrame()
    jf.setLocationRelativeTo(jf.getRootPane)
    jf.setVisible(true)

    val res = JOptionPane.showInputDialog(jf, prompt, defaultValue)
    jf.setVisible(false)
    jf.dispose
    //println("res="+res)
    if (res != null) Some(res) else None
  }

  def optionsDialog(prompt: String, options: Array[Object]): Int = {
    val jf = new JFrame()
    jf.setLocationRelativeTo(jf.getRootPane)
    jf.setVisible(true)

    val res = JOptionPane.showOptionDialog(jf, prompt, "Options",
      JOptionPane.YES_NO_CANCEL_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      null, options, options(0) )

    jf.setVisible(false)
    jf.dispose
    res
  }


  def messageDialog(prompt: String) {
    val jf = new JFrame()
    jf.setLocationRelativeTo(jf.getRootPane)
    jf.setVisible(true)

   JOptionPane.showMessageDialog(jf, prompt, "Information", JOptionPane.INFORMATION_MESSAGE)

    jf.setVisible(false)
    jf.dispose
  }

}