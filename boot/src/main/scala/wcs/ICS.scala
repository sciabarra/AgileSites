package wcs

import java.io.OutputStream;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import COM.FutureTense.Cache.Satellite;
import COM.FutureTense.ContentServer.PageData;
import COM.FutureTense.Interfaces.FTVAL;
import COM.FutureTense.Interfaces.FTValList;
import COM.FutureTense.Interfaces.IJSPObject;
import COM.FutureTense.Interfaces.IList;
import COM.FutureTense.Interfaces.IMIMENotifier;
import COM.FutureTense.Interfaces.IProperties;
import COM.FutureTense.Interfaces.ISearchEngine;
//import COM.FutureTense.Interfaces.IServlet;
import COM.FutureTense.Interfaces.ISyncHash;
//import COM.FutureTense.Interfaces.IURLDefinition;
import COM.FutureTense.Interfaces.PastramiEngine;
import COM.FutureTense.Util.ftErrors;
//import COM.FutureTense.XML.Template.Seed;

import com.fatwire.cs.core.db.PreparedStmt;
import com.fatwire.cs.core.db.StatementParam;
import com.fatwire.cs.core.uri.Definition;

class ICS(val ics: COM.FutureTense.Interfaces.ICS) {

  def AppEvent(arg0: String, arg1: String, arg2: String, arg3: FTValList) = ics.AppEvent(arg0, arg1, arg2, arg3)

  def BlobServer(arg0: FTValList, arg1: IMIMENotifier,
    arg2: OutputStream) = ics.BlobServer(arg0, arg1, arg2);

  def BlobServer(arg0: FTValList, arg1: OutputStream) = ics.BlobServer(arg0, arg1);

  def CallElement(arg0: String, arg1: FTValList) = ics.CallElement(arg0, arg1);

  def CallSQL(arg0: String, arg1: String, arg2: Int, arg3: Boolean,
    arg4: Boolean, arg5: StringBuffer) = ics.CallSQL(arg0, arg1, arg2, arg3, arg4, arg5);

  def CallSQL(arg0: String, arg1: String, arg2: Int, arg3: Boolean,
    arg4: StringBuffer) = ics.CallSQL(arg0, arg1, arg2, arg3, arg4);

  def CatalogDef(arg0: String, arg1: String, arg2: StringBuffer) = ics.CatalogDef(arg0, arg1, arg2);

  def CatalogIndexDef(arg0: String, arg1: String, arg2: StringBuffer) = ics.CatalogIndexDef(arg0, arg1, arg2);

  def CatalogManager(arg0: FTValList, arg1: Object) = ics.CatalogManager(arg0, arg1);

  def CatalogManager(arg0: FTValList) = ics.CatalogManager(arg0);

  def ClearErrno() {
    ics.ClearErrno();
  }

  def CommitBatchedCommands(arg0: Object) = ics.CommitBatchedCommands(arg0);

  def CopyList(arg0: String, arg1: String) = ics.CopyList(arg0, arg1);

  def DeleteSynchronizedHash(arg0: String) = ics.DeleteSynchronizedHash(arg0);

  def DestroyEvent(arg0: String) = ics.DestroyEvent(arg0);

  def DisableCache() {
    ics.DisableCache();
  }

  def DisableEvent(arg0: String) = ics.DisableEvent(arg0);

  def DisableFragmentCache() {
    ics.DisableFragmentCache();
  }

  def EmailEvent(arg0: String, arg1: String, arg2: String,
    arg3: String, arg4: String, arg5: String, arg6: FTValList) = ics.EmailEvent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

  def EmailEvent(arg0: String, arg1: String, arg2: String, arg3: String) = ics.EmailEvent(arg0, arg1, arg2, arg3);

  def EnableEvent(arg0: String) = ics.EnableEvent(arg0);

  def FlushCatalog(arg0: String) = ics.FlushCatalog(arg0);

  def FlushStream() {
    ics.FlushStream();
  }

  def GetBin(arg0: String) = ics.GetBin(arg0);

  def GetCatalogType(arg0: String) = ics.GetCatalogType(arg0);

  def GetCgi(arg0: String) = ics.GetCgi(arg0);

  def GetCounter(arg0: String) = ics.GetCounter(arg0);

  def GetErrno() = ics.GetErrno();

  def GetList(arg0: String, arg1: Boolean) = ics.GetList(arg0, arg1);

  def GetList(arg0: String) = ics.GetList(arg0);

  def GetObj(arg0: String) = ics.GetObj(arg0);

  def GetProperty(arg0: String, arg1: String, arg2: Boolean) = ics.GetProperty(arg0, arg1, arg2);

  def GetProperty(arg0: String) = ics.GetProperty(arg0);

  def GetSSVar(arg0: String) = ics.GetSSVar(arg0);

  def GetSSVars() = ics.GetSSVars();

  def GetSearchEngine(arg0: String, arg1: String,
    arg2: StringBuffer) = ics.GetSearchEngine(arg0, arg1, arg2);

  def GetSearchEngineList() = ics.GetSearchEngineList();

  def GetSynchronizedHash(arg0: String, arg1: Boolean, arg2: Int, arg3: Int, arg4: Boolean, arg5: Boolean, arg6: java.util.Collection[_]) =
    ics.GetSynchronizedHash(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

  def GetSynchronizedHash(arg0: String, arg1: Boolean, arg2: Int,
    arg3: Int, arg4: Boolean, arg5: Boolean) = ics.GetSynchronizedHash(arg0, arg1, arg2, arg3, arg4, arg5);

  def GetVar(arg0: String) = ics.GetVar(arg0);

  def GetVars() = ics.GetVars();

  def IndexAdd(arg0: String, arg1: String, arg2: String, arg3: String,
    arg4: String, arg5: String, arg6: String, arg7: FTValList,
    arg8: FTValList, arg9: FTValList, arg10: String, arg11: String,
    arg12: StringBuffer) = ics.IndexAdd(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
    arg8, arg9, arg10, arg11, arg12);

  def IndexCreate(arg0: String, arg1: FTValList, arg2: String,
    arg3: String, arg4: StringBuffer) = ics.IndexCreate(arg0, arg1, arg2, arg3, arg4);

  def IndexDestroy(arg0: String, arg1: String, arg2: String,
    arg3: StringBuffer) = ics.IndexDestroy(arg0, arg1, arg2, arg3);

  def IndexExists(arg0: String, arg1: String, arg2: String,
    arg3: StringBuffer) = ics.IndexExists(arg0, arg1, arg2, arg3);

  def IndexRemove(arg0: String, arg1: String, arg2: String,
    arg3: String, arg4: StringBuffer) = ics.IndexRemove(arg0, arg1, arg2, arg3, arg4);

  def IndexReplace(arg0: String, arg1: String, arg2: String,
    arg3: String, arg4: String, arg5: String, arg6: String, arg7: FTValList,
    arg8: FTValList, arg9: FTValList, arg10: String, arg11: String,
    arg12: StringBuffer) =
    ics.IndexReplace(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
      arg8, arg9, arg10, arg11, arg12);

  def InsertPage(arg0: String, arg1: FTValList) = ics.InsertPage(arg0, arg1);

  def IsElement(arg0: String) = ics.IsElement(arg0);

  def IsSystemSecure() = ics.IsSystemSecure();

  def IsTracked(arg0: String) = ics.IsTracked(arg0);

  def IsTrackedNewFormat(arg0: String) = ics.IsTrackedNewFormat(arg0);

  def LoadProperty(arg0: String) = ics.LoadProperty(arg0);

  /*
	 * def LogMsg(arg0: String) { ics.LogMsg(arg0); }
	 */

  def Mirror(arg0: IList, arg1: String, arg2: String, arg3: String,
    arg4: String, arg5: String, arg6: Int, arg7: Boolean, arg8: String,
    arg9: String, arg10: String, arg11: String, arg12: Boolean, arg13: Int,
    arg14: String, arg15: StringBuffer) =
    ics.Mirror(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
      arg9, arg10, arg11, arg12, arg13, arg14, arg15);

  def Mirror(arg0: java.util.Vector[_], arg1: String, arg2: String, arg3: String,
    arg4: String, arg5: String, arg6: Int, arg7: Boolean, arg8: Boolean,
    arg9: Int, arg10: StringBuffer) =
    ics.Mirror(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
      arg9, arg10);

  def Mirror(arg0: java.util.Vector[_], arg1: String, arg2: String, arg3: String,
    arg4: String, arg5: String, arg6: Int, arg7: Boolean, arg8: String,
    arg9: String, arg10: String, arg11: String, arg12: Boolean, arg13: Int,
    arg14: StringBuffer) =
    ics.Mirror(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
      arg9, arg10, arg11, arg12, arg13, arg14);

  def Mirror(arg0: java.util.Vector[_], arg1: java.util.Vector[_], arg2: String, arg3: String,
    arg4: String, arg5: String, arg6: String, arg7: Int, arg8: Boolean,
    arg9: Boolean, arg10: Int, arg11: StringBuffer) = ics.Mirror(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
    arg9, arg10, arg11);

  def Mirror(arg0: java.util.Vector[_], arg1: java.util.Vector[_], arg2: String, arg3: String,
    arg4: String, arg5: String, arg6: String, arg7: Int, arg8: Boolean,
    arg9: String, arg10: String, arg11: String, arg12: String,
    arg13: Boolean, arg14: Int, arg15: StringBuffer) =
    ics.Mirror(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
      arg9, arg10, arg11, arg12, arg13, arg14, arg15);

  /*
	 * def NewSeedFromTagname(arg0: String) { return
	 * ics.NewSeedFromTagname(arg0); }
	 */

  def PopObj(arg0: String) = ics.PopObj(arg0);

  def PopVars() = ics.PopVars();

  def PushObj(arg0: String, arg1: Object) = ics.PushObj(arg0, arg1);

  def PushVars() = ics.PushVars();

  def QueryEvents(arg0: String, arg1: String, arg2: Boolean, arg3: String) = ics.QueryEvents(arg0, arg1, arg2, arg3);

  def RTCommit(arg0: String, arg1: String, arg2: String, arg3: Boolean) = ics.RTCommit(arg0, arg1, arg2, arg3);

  def RTDeleteRevision(arg0: String, arg1: String, arg2: Int) = ics.RTDeleteRevision(arg0, arg1, arg2);

  def RTHistory(arg0: String, arg1: String, arg2: String, arg3: String,
    arg4: String, arg5: String, arg6: String) = ics.RTHistory(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

  def RTInfo(arg0: String, arg1: String) = ics.RTInfo(arg0, arg1);

  def RTLock(arg0: String, arg1: String) = ics.RTLock(arg0, arg1);

  def RTRelease(arg0: String, arg1: String) = ics.RTRelease(arg0, arg1);

  def RTRetrieveRevision(arg0: String, arg1: String, arg2: Int,
    arg3: String) = ics.RTRetrieveRevision(arg0, arg1, arg2, arg3);

  def RTRetrieveRevision(arg0: String, arg1: String, arg2: String,
    arg3: String) = ics.RTRetrieveRevision(arg0, arg1, arg2, arg3);

  def RTRollback(arg0: String, arg1: String, arg2: Int) = ics.RTRollback(arg0, arg1, arg2);

  def RTRollback(arg0: String, arg1: String, arg2: String) = ics.RTRollback(arg0, arg1, arg2);

  def RTSetVersions(arg0: String, arg1: Int) = ics.RTSetVersions(arg0, arg1);

  def RTTrackTable(arg0: String, arg1: String, arg2: Int) = ics.RTTrackTable(arg0, arg1, arg2);

  def RTUnlockRecord(arg0: String, arg1: String) = ics.RTUnlockRecord(arg0, arg1);

  def RTUntrackTable(arg0: String) = ics.RTUntrackTable(arg0);

  def ReadEvent(arg0: String, arg1: String) = ics.ReadEvent(arg0, arg1);

  def ReadPage(arg0: String, arg1: FTValList) = ics.ReadPage(arg0, arg1);

  def RegisterList(arg0: String, arg1: IList) = ics.RegisterList(arg0, arg1);

  def RemoveCounter(arg0: String) = ics.RemoveCounter(arg0);

  def RemoveSSVar(arg0: String) = ics.RemoveSSVar(arg0);

  def RemoveVar(arg0: String) = ics.RemoveVar(arg0);

  def RenameList(arg0: String, arg1: String) = ics.RenameList(arg0, arg1);

  def ResolveVariables(arg0: String, arg1: Boolean) = ics.ResolveVariables(arg0, arg1);

  def ResolveVariables(arg0: String) = ics.ResolveVariables(arg0);

  def RestoreProperty(arg0: Boolean) = ics.RestoreProperty(arg0);

  def RollbackBatchedCommands(arg0: Object) = ics.RollbackBatchedCommands(arg0);

  def SQL(arg0: PreparedStmt, arg1: StatementParam, arg2: Boolean) = ics.SQL(arg0, arg1, arg2);

  def SQL(arg0: String, arg1: String, arg2: String, arg3: Int,
    arg4: Boolean, arg5: Boolean, arg6: StringBuffer) = ics.SQL(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

  def SQL(arg0: String, arg1: String, arg2: String, arg3: Int,
    arg4: Boolean, arg5: StringBuffer) = ics.SQL(arg0, arg1, arg2, arg3, arg4, arg5);

  def SQLExp(arg0: String, arg1: String, arg2: String, arg3: String,
    arg4: String, arg5: String) = ics.SQLExp(arg0, arg1, arg2, arg3, arg4, arg5);

  def SQLExp(arg0: String, arg1: String, arg2: String, arg3: String,
    arg4: String) = ics.SQLExp(arg0, arg1, arg2, arg3, arg4);

  def Search(arg0: String, arg1: String, arg2: String, arg3: String,
    arg4: Int, arg5: FTValList, arg6: String, arg7: String, arg8: String,
    arg9: StringBuffer) =
    ics.Search(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
      arg9);

  def SearchDateToNative(arg0: String, arg1: StringBuffer,
    arg2: String, arg3: String, arg4: StringBuffer) = ics.SearchDateToNative(arg0, arg1, arg2, arg3, arg4);

  def SelectTo(arg0: String, arg1: String, arg2: String, arg3: String,
    arg4: Int, arg5: String, arg6: Boolean, arg7: StringBuffer) = ics.SelectTo(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);

  def SendMail(arg0: String, arg1: String, arg2: String, arg3: String,
    arg4: String, arg5: String, arg6: FTValList) = ics.SendMail(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

  def SendMail(arg0: String, arg1: String, arg2: String, arg3: String,
    arg4: String) = ics.SendMail(arg0, arg1, arg2, arg3, arg4);

  def SendMail(arg0: String, arg1: String, arg2: String) = ics.SendMail(arg0, arg1, arg2);

  /*
	 * def SessionExists(arg0: String) { return
	 * ics.SessionExists(arg0); }
	 */

  def SessionID() = ics.SessionID();

  def SetCookie(arg0: String, arg1: String, arg2: Int, arg3: String,
    arg4: String, arg5: Boolean) = ics.SetCookie(arg0, arg1, arg2, arg3, arg4, arg5);

  def SetCounter(arg0: String, arg1: Int) =
    ics.SetCounter(arg0, arg1);

  def SetErrno(arg0: Int) {
    ics.SetErrno(arg0);
  }

  def SetObj(arg0: String, arg1: Object) = ics.SetObj(arg0, arg1);

  def SetSSVar(arg0: String, arg1: Int) {
    ics.SetSSVar(arg0, arg1);
  }

  def SetSSVar(arg0: String, arg1: String) {
    ics.SetSSVar(arg0, arg1);
  }

  def SetVar(arg0: String, arg1: FTVAL) {
    ics.SetVar(arg0, arg1);
  }

  def SetVar(arg0: String, arg1: Int) {
    ics.SetVar(arg0, arg1);
  }

  def SetVar(arg0: String, arg1: String) {
    ics.SetVar(arg0, arg1);
  }

  def StartBatchContext() = ics.StartBatchContext();

  /*
	 * def StreamBinary(byte[] arg0, arg1: Int, arg2: Int) {
	 * ics.StreamBinary(arg0, arg1, arg2); }
	 */

  def StreamEvalBytes(arg0: String) = ics.StreamEvalBytes(arg0);

  def StreamHeader(arg0: String, arg1: String) = ics.StreamHeader(arg0, arg1);

  def StreamText(arg0: String) = ics.StreamText(arg0);

  def ThrowException() = ics.ThrowException();

  def TreeManager(arg0: FTValList, arg1: Object) = ics.TreeManager(arg0, arg1);

  def TreeManager(arg0: FTValList) = ics.TreeManager(arg0);

  def UserIsMember(arg0: String) = ics.UserIsMember(arg0);

  def close() = ics.close();

  /*
	 * def dbDebug() { return ics.dbDebug(); }
	 */

  def decode(arg0: String, arg1: java.util.Map[_, _]) = ics.decode(arg0, arg1);

  def deployJSPData(arg0: String, arg1: String, arg2: StringBuffer) = ics.deployJSPData(arg0, arg1, arg2);

  def deployJSPFile(arg0: String, arg1: String, arg2: StringBuffer) = ics.deployJSPFile(arg0, arg1, arg2);

  /*
	 * def diskFileName() { return ics.diskFileName(); }
	 * 
	 * def diskFileName(arg0: String, arg1: FTValList) { return
	 * ics.diskFileName(arg0, arg1); }
	 * 
	 * def diskFileName(arg0: String, arg1: String) { return
	 * ics.diskFileName(arg0, arg1); }
	 */

  def encode(arg0: String, arg1: java.util.Map[_, _], arg2: Boolean) = ics.encode(arg0, arg1, arg2);

  /*
	 * 
	 * def eventDebug() { return ics.eventDebug(); }
	 */

  def genID(arg0: Boolean) = ics.genID(arg0);

  def getAttribute(arg0: String) = ics.getAttribute(arg0);

  def getAttributeNames() = ics.getAttributeNames();

  def getComplexError() = ics.getComplexError();

  def getCookie(arg0: String) = ics.getCookie(arg0);

  def getElementArgumentValue(arg0: String, arg1: String) = ics.getElementArgumentValue(arg0, arg1);

  def getIProperties() = ics.getIProperties();

  /*
	 * def getIServlet() { return ics.getIServlet(); }
	 */

  def getLocaleString(arg0: String, arg1: String) = ics.getLocaleString(arg0, arg1);

  def getNamespace() = ics.getNamespace();

  def getPageData(arg0: String) = ics.getPageData(arg0);

  def getPastramiEngine() = ics.getPastramiEngine();

  def getSQL(arg0: String) = ics.getSQL(arg0);

  def getSatellite(arg0: String) = ics.getSatellite(arg0);

  def getTrackingStatus(arg0: String, arg1: String) = ics.getTrackingStatus(arg0, arg1);

  def getURL(arg0: Definition, arg1: String) = ics.getURL(arg0, arg1);

  /*
	 * def getURL(arg0: IURLDefinition) { return ics.getURL(arg0); }
	 */

  def getUserPrincipal() = ics.getUserPrincipal();

  def grabCacheStatus() = ics.grabCacheStatus();

  def grabHeaders() = ics.grabHeaders();

  def ioErrorThrown() = ics.ioErrorThrown();

  def isCacheable(arg0: String) = ics.isCacheable(arg0);

  def literal(arg0: String, arg1: String, arg2: String) = ics.literal(arg0, arg1, arg2);

  def pageCriteriaKeys(arg0: String) = ics.pageCriteriaKeys(arg0);

  def pageURL() = ics.pageURL();

  def pageURL(arg0: String, arg1: FTValList) = ics.pageURL(arg0, arg1);

  /*
	 * 
	 * def pastramiDebug() { return ics.pastramiDebug(); }
	 * 
	 * def pgCacheDebug() { return ics.pgCacheDebug(); }
	 */

  def removeAttribute(arg0: String) = ics.removeAttribute(arg0);

  /*
	 * def rsCacheDebug() { return ics.rsCacheDebug(); }
	 */

  def runTag(arg0: String, arg1: FTValList) = ics.runTag(arg0, arg1);

  /*
	 * def sessionDebug() { return ics.sessionDebug(); }
	 */

  def setAttribute(arg0: String, arg1: Object) = ics.setAttribute(arg0, arg1);

  def setComplexError(arg0: ftErrors) = ics.setComplexError(arg0);

  /*
	 * def syncDebug() { return ics.syncDebug(); }
	 * 
	 * def systemDebug() { return ics.systemDebug(); }
	 */

  def systemSession() = ics.systemSession();

  /*
	 * def timeDebug() { return ics.timeDebug(); }
	 * 
	 * def xmlDebug() { return ics.xmlDebug(); }
	 */

}
