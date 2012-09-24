package wcs.api

import COM.FutureTense.Interfaces.FTValList

package object tag {

  // parameters are 'a := val
  class ArgAssoc(x: Symbol) {
    def :=(y: String): Tuple2[Symbol, String] = Tuple2(x, y)
  }
  // creating the param assoc
  implicit def sym2ArgAssoc(x: Symbol) = new ArgAssoc(x)

  // dump an ftval list for logging
  def ftValList2String(name: String, vl: FTValList) = {
    var sb = new StringBuilder()
    sb.append("[TAG ");
    sb.append(name)
    val en = vl.keys
    while (en.hasMoreElements()) {
      val k = en.nextElement().toString()
      val v = vl.getValString(k)
      sb.append(" %s=%s".format(k, v))
    }
    sb.append("] ");
    sb.toString()
  }

}


