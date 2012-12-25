package wcs.implx

import java.io.OutputStream
import java.security.Principal
import java.util.Enumeration
import com.fatwire.cs.core.uri.Definition
import com.fatwire.cs.core.db.StatementParam
import com.fatwire.cs.core.db.PreparedStmt
import COM.FutureTense.ContentServer.PageData
import COM.FutureTense.XML.Template.Seed
import COM.FutureTense.Util.ftErrors
import COM.FutureTense.Cache.Satellite
import COM.FutureTense.Interfaces.ICS
import COM.FutureTense.Interfaces.FTVAL
import COM.FutureTense.Interfaces.IList
import COM.FutureTense.Interfaces.FTValList
import COM.FutureTense.Interfaces.ISearchEngine
import COM.FutureTense.Interfaces.ISyncHash
import COM.FutureTense.Interfaces.IServlet
import COM.FutureTense.Interfaces.IURLDefinition
import COM.FutureTense.Interfaces.IJSPObject
import COM.FutureTense.Interfaces.PastramiEngine
import COM.FutureTense.Interfaces.IMIMENotifier
import COM.FutureTense.Interfaces.IProperties
import java.io.File

/**
 * Mock class simulating (part of) ICS behaviour
 */
class XmlICS(val base: File) extends ICS {

  def this() = this(new File("export"))
  
  var varMap = Map[String, String]()
  var listMap = Map[String, IList]()
  var errno = 0;

  def addMapList(name: String, map: Map[String, List[String]]) = {
    listMap = listMap + (name -> new MapListIList(name, map))
  }

  def addMapVar(map: Map[String, String]) = { this.varMap = map }

  // implemented
  def GetVar(arg0: String): String = { varMap.get(arg0).getOrElse { null } }

  def GetList(arg0: String): IList = { listMap.getOrElse(arg0, null) }

  // to be implemented soon (but yet unimplemeneted)

  def GetList(arg0: String, arg1: Boolean): IList = { null }

  def RenameList(arg0: String, arg1: String): Boolean = { false }

  def CopyList(arg0: String, arg1: String): Boolean = { false }

  def SetVar(arg0: String, arg1: String): Unit = {
    varMap += arg0 -> arg1
  }

  def SetVar(arg0: String, arg1: Int): Unit = {}

  def SetVar(arg0: String, arg1: FTVAL): Unit = {}

  // unimplemented

  def PushVars(): Unit = {}

  def PopVars(): Unit = {}

  def SetObj(arg0: String, arg1: Object): Boolean = { false }

  def GetObj(arg0: String): Object = { null }

  def PopObj(arg0: String): Object = { null }

  def PushObj(arg0: String, arg1: Object): Boolean = { false }

  def GetBin(arg0: String): Array[Byte] = { null }

  def GetCounter(arg0: String): Int = { 0 }

  def SetCounter(arg0: String, arg1: Int): Unit = {}

  def RemoveCounter(arg0: String): Unit = {}

  def GetVars(): Enumeration[_] = { null }

  def RemoveVar(arg0: String): Unit = {}

  def SetSSVar(arg0: String, arg1: String): Unit = {}

  def SetSSVar(arg0: String, arg1: COM.FutureTense.Interfaces.FTVAL): Unit = {}

  def SetSSVar(arg0: String, arg1: Int): Unit = {}

  def GetSSVar(arg0: String): String = { null }

  def GetSSVars(): Enumeration[_] = { null }

  def RemoveSSVar(arg0: String): Unit = {}

  def SessionID(): String = { null }

  def IsSystemSecure(): Boolean = { false }

  def GetCgi(arg0: String): FTVAL = { null }

  def GetProperty(arg0: String): String = { null }

  def GetProperty(arg0: String, arg1: String, arg2: Boolean): String = { null }

  def IsElement(arg0: String): Boolean = { false }

  def getElementArgumentValue(arg0: String, arg1: String): String = { null }

  def GetCatalogType(arg0: String): Int = { 0 }

  def IsTracked(arg0: String): Boolean = { false }

  def IsTrackedNewFormat(arg0: String): Boolean = { false }

  def getTrackingStatus(arg0: String, arg1: String): Int = { 0 }

  def FlushCatalog(arg0: String): Boolean = { false }

  def GetErrno(): Int = { errno }

  def getComplexError(): ftErrors = { null }

  def setComplexError(arg0: ftErrors): Unit = {}

  def SetErrno(arg0: Int): Unit = {
    errno = arg0
  }

  def ClearErrno(): Unit = {
    errno = 0
  }

  def ResolveVariables(arg0: String): String = { null }

  def ResolveVariables(arg0: String, arg1: Boolean): String = { null }

  def SelectTo(arg0: String, arg1: String, arg2: String, arg3: String, arg4: Int, arg5: String, arg6: Boolean, arg7: StringBuffer): IList = { null }

  def CallSQL(arg0: String, arg1: String, arg2: Int, arg3: Boolean, arg4: StringBuffer): IList = { null }

  def getSQL(arg0: String): String = { null }

  def CallSQL(arg0: String, arg1: String, arg2: Int, arg3: Boolean, arg4: Boolean, arg5: StringBuffer): IList = { null }

  def SQL(arg0: String, arg1: String, arg2: String, arg3: Int, arg4: Boolean, arg5: StringBuffer): IList = { null }

  def SQL(arg0: String, arg1: String, arg2: String, arg3: Int, arg4: Boolean, arg5: Boolean, arg6: StringBuffer): IList = { null }

  def SQL(arg0: PreparedStmt, arg1: StatementParam, arg2: Boolean): IList = { null }

  def literal(arg0: String, arg1: String, arg2: String): String = { null }

  def SQLExp(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): String = { null }

  def SQLExp(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String): String = { null }

  def CatalogDef(arg0: String, arg1: String, arg2: StringBuffer): IList = { null }

  def CatalogIndexDef(arg0: String, arg1: String, arg2: StringBuffer): IList = { null }

  def CatalogManager(arg0: FTValList): Boolean = { false }

  def CatalogManager(arg0: FTValList, arg1: Object): Boolean = { false }

  def TreeManager(arg0: FTValList): Boolean = { false }

  def TreeManager(arg0: FTValList, arg1: Object): Boolean = { false }

  def CallElement(arg0: String, arg1: FTValList): Boolean = { false }

  def StreamEvalBytes(arg0: String): Unit = {}

  def ReadPage(arg0: String, arg1: FTValList): String = { null }

  def InsertPage(arg0: String, arg1: FTValList): Boolean = { false }

  def NewSeedFromTagname(arg0: String): Seed = { null }

  def runTag(arg0: String, arg1: FTValList): String = { null }

  def StreamText(arg0: String): Unit = {}

  def FlushStream(): Unit = {}

  def StreamBinary(arg0: Array[Byte], arg1: Int, arg2: Int): Unit = {}

  def StreamHeader(arg0: String, arg1: String): Unit = {}

  def grabHeaders(): FTValList = { null }

  def grabCacheStatus(): Array[Byte] = { null }

  def LoadProperty(arg0: String): Boolean = { false }

  def RestoreProperty(arg0: Boolean): Boolean = { false }

  def getCookie(arg0: String): String = { null }

  def SetCookie(arg0: String, arg1: String, arg2: Int, arg3: String, arg4: String, arg5: Boolean): Boolean = { false }

  def DisableCache(): Unit = {}

  def SendMail(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: FTValList): Boolean = { false }

  def SendMail(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): Boolean = { false }

  def SendMail(arg0: String, arg1: String, arg2: String): Boolean = { false }

  def UserIsMember(arg0: String): Boolean = { false }

  def LogMsg(arg0: String): Unit = {}

  def GetSearchEngine(arg0: String, arg1: String, arg2: StringBuffer): ISearchEngine = { null }

  def GetSearchEngineList(): String = { null }

  def IndexCreate(arg0: String, arg1: FTValList, arg2: String, arg3: String, arg4: StringBuffer): Boolean = { false }

  def IndexDestroy(arg0: String, arg1: String, arg2: String, arg3: StringBuffer): Boolean = { false }

  def IndexExists(arg0: String, arg1: String, arg2: String, arg3: StringBuffer): Boolean = { false }

  def IndexAdd(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String, arg7: FTValList, arg8: FTValList, arg9: FTValList, arg10: String, arg11: String, arg12: StringBuffer): Boolean = { false }

  def IndexReplace(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String, arg7: FTValList, arg8: FTValList, arg9: FTValList, arg10: String, arg11: String, arg12: StringBuffer): Boolean = { false }

  def IndexRemove(arg0: String, arg1: String, arg2: String, arg3: String, arg4: StringBuffer): Boolean = { false }

  def SearchDateToNative(arg0: String, arg1: StringBuffer, arg2: String, arg3: String, arg4: StringBuffer): Boolean = { false }

  def Search(arg0: String, arg1: String, arg2: String, arg3: String, arg4: Int, arg5: FTValList, arg6: String, arg7: String, arg8: String, arg9: StringBuffer): IList = { null }

  def RTHistory(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String): IList = { null }

  def RTInfo(arg0: String, arg1: String): IList = { null }

  def RTLock(arg0: String, arg1: String): Int = { 0 }

  def RTCommit(arg0: String, arg1: String, arg2: String, arg3: Boolean): Int = { 0 }

  def RTRelease(arg0: String, arg1: String): Int = { 0 }

  def RTRetrieveRevision(arg0: String, arg1: String, arg2: Int, arg3: String): IList = { null }

  def RTRetrieveRevision(arg0: String, arg1: String, arg2: String, arg3: String): IList = { null }

  def RegisterList(arg0: String, arg1: IList): Boolean = { false }

  def RTRollback(arg0: String, arg1: String, arg2: String): Int = { 0 }

  def RTRollback(arg0: String, arg1: String, arg2: Int): Int = { 0 }

  def RTTrackTable(arg0: String, arg1: String, arg2: String): String = { null }

  def RTTrackTable(arg0: String, arg1: String, arg3: Int): Int = { 0 }

  def RTUntrackTable(arg0: String): Int = { 0 }

  def RTUnlockRecord(arg0: String, arg1: String): Int = { 0 }

  def RTDeleteRevision(arg0: String, arg1: String, arg2: Int): Int = { 0 }

  def RTSetVersions(arg0: String, arg1: Int): Int = { 0 }

  def Mirror(arg0: java.util.Vector[_], arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: Int, arg7: Boolean, arg8: Boolean, arg9: Int, arg10: StringBuffer): Int = { 0 }

  def Mirror(arg0: java.util.Vector[_], arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: Int, arg7: Boolean, arg8: String, arg9: String, arg10: String, arg11: String, arg12: Boolean, arg13: Int, arg14: StringBuffer): Int = { 0 }

  def Mirror(arg0: IList, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: Int, arg7: Boolean, arg8: String, arg9: String, arg10: String, arg11: String, arg12: Boolean, arg13: Int, arg14: String, arg15: StringBuffer): Int = { 0 }

  def Mirror(arg0: java.util.Vector[_], arg1: java.util.Vector[_], arg2: String, arg3: String, arg4: String, arg5: String, arg6: String, arg7: Int, arg8: Boolean, arg9: Boolean, arg10: Int, arg11: StringBuffer): Int = { 0 }

  def Mirror(x$1: java.util.Vector[_], x$2: java.util.Vector[_], x$3: java.lang.String, x$4: java.lang.String, x$5: java.lang.String, x$6: java.lang.String, x$7: java.lang.String, x$8: Int, x$9: Boolean, x$10: java.lang.String, x$11: java.lang.String, x$12: java.lang.String, x$13: java.lang.String, x$14: Boolean, x$15: Int, x$16: java.lang.StringBuffer): Int = { 0 }

  def GetSynchronizedHash(arg0: String, arg1: Boolean, arg2: Int, arg3: Int, arg4: Boolean, arg5: Boolean): ISyncHash = { null }

  def GetSynchronizedHash(arg0: String, arg1: Boolean, arg2: Int, arg3: Int, arg4: Boolean, arg5: Boolean, arg6: java.util.Collection[_]): ISyncHash = { null }

  def DeleteSynchronizedHash(arg0: String): Boolean = { false }

  def AppEvent(arg0: String, arg1: String, arg2: String, arg3: FTValList): Boolean = { false }

  def EmailEvent(arg0: String, arg1: String, arg2: String, arg3: String): Boolean = { false }

  def EmailEvent(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: FTValList): Boolean = { false }

  def DestroyEvent(arg0: String): Boolean = { false }

  def EnableEvent(arg0: String): Boolean = { false }

  def DisableEvent(arg0: String): Boolean = { false }

  def ReadEvent(arg0: String, arg1: String): IList = { null }

  def QueryEvents(arg0: String, arg1: String, arg2: java.lang.Boolean, arg3: String): IList = { null }

  def SessionExists(arg0: String): Boolean = { false }

  def deployJSPFile(arg0: String, arg1: String, arg2: StringBuffer): IJSPObject = { null }

  def deployJSPData(arg0: String, arg1: String, arg2: StringBuffer): IJSPObject = { null }

  def getIServlet(): IServlet = { null }

  def ThrowException(): Unit = {}

  def ioErrorThrown(): Boolean = { false }

  def genID(arg0: Boolean): String = { null }

  def getURL(arg0: IURLDefinition): String = { null }

  def getURL(arg0: Definition, arg1: String): String = { null }

  def encode(arg0: String, arg1: java.util.Map[_, _], arg2: Boolean): String = { null }

  def decode(arg0: String, arg1: java.util.Map[_, _]): Unit = {}

  def diskFileName(): String = { null }

  def diskFileName(arg0: String, arg1: String): java.lang.String = { null }

  def diskFileName(arg0: String, arg1: COM.FutureTense.Interfaces.FTValList): java.lang.String = { null }

  def getPageData(arg0: String): PageData = { null }

  def isCacheable(arg0: String): Boolean = { false }

  def pageURL(): String = { null }

  def pageCriteriaKeys(arg0: String): Array[String] = { null }

  def pageURL(arg0: String, arg1: FTValList): String = { null }

  def DisableFragmentCache(): Unit = {}

  def StartBatchContext(): Object = { null }

  def CommitBatchedCommands(arg0: Object): Boolean = { false }

  def RollbackBatchedCommands(arg0: Object): Boolean = { false }

  def rsCacheDebug(): Boolean = { false }

  def pgCacheDebug(): Boolean = { false }

  def pastramiDebug(): Boolean = { false }

  def xmlDebug(): Boolean = { false }

  def syncDebug(): Boolean = { false }

  def eventDebug(): Boolean = { false }

  def systemDebug(): Boolean = { false }

  def dbDebug(): Boolean = { false }

  def sessionDebug(): Boolean = { false }

  def timeDebug(): Boolean = { false }

  def systemSession(): Boolean = { false }

  def BlobServer(arg0: FTValList, arg1: OutputStream): Boolean = { false }

  def BlobServer(arg0: FTValList, arg1: IMIMENotifier, arg2: OutputStream): Boolean = { false }

  def getLocaleString(arg0: String, arg1: String): String = { null }

  def getPastramiEngine(): PastramiEngine = { null }

  def getSatellite(arg0: String): Satellite = { null }

  def getAttribute(arg0: String): Object = { null }

  def getAttributeNames(): Enumeration[_] = { null }

  def removeAttribute(arg0: String): Unit = {}

  def setAttribute(arg0: String, arg1: Object): Unit = {}

  def close(): Unit = {}

  def getNamespace(): String = { null }

  def getUserPrincipal(): Principal = { null }

  def getIProperties(): IProperties = { null }

}