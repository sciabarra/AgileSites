package wcs

import COM.FutureTense.Interfaces.FTValList

package object tag {

  // parameters are 'a := val
  class ArgAssoc(x: Symbol) {
    def :=(y: String): Tuple2[Symbol, String] = Tuple2(x, y)
  }

  // creating the param assoc
  implicit def sym2ArgAssoc(x: Symbol) = new ArgAssoc(x)
}


