package wcs

import java.io.OutputStream
import java.security.Principal
import java.util.Collection
import java.util.Enumeration
import java.util.Map
import java.util.Vector
import scala.collection.immutable
import COM.FutureTense.Cache.Satellite
import COM.FutureTense.ContentServer.PageData
import COM.FutureTense.Interfaces.FTVAL
import COM.FutureTense.Interfaces.FTValList
import COM.FutureTense.Interfaces.IJSPObject
import COM.FutureTense.Interfaces.IList
import COM.FutureTense.Interfaces.IMIMENotifier
import COM.FutureTense.Interfaces.IProperties
import COM.FutureTense.Interfaces.ISearchEngine
import COM.FutureTense.Interfaces.ISyncHash
import COM.FutureTense.Interfaces.PastramiEngine
import COM.FutureTense.Util.ftErrors
import com.fatwire.cs.core.db.PreparedStmt
import com.fatwire.cs.core.db.StatementParam
import com.fatwire.cs.core.uri.Definition
import org.eintr.loglady.Logging

/**
 * Receive an ICS and it is an interface so it can be manager by Java
 */
trait ICSReceiver  {
 
  /**
   * Initialze with a ICS
   */
  def init(ics: COM.FutureTense.Interfaces.ICS)
  
  
  
}
