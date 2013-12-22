package wcs.core;
import static wcs.Api.*;
import wcs.api.Id;

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
import COM.FutureTense.Interfaces.ICS;
import COM.FutureTense.Interfaces.IJSPObject;
import COM.FutureTense.Interfaces.IList;
import COM.FutureTense.Interfaces.IMIMENotifier;
import COM.FutureTense.Interfaces.IProperties;
import COM.FutureTense.Interfaces.ISearchEngine;
import COM.FutureTense.Interfaces.ISyncHash;
import COM.FutureTense.Interfaces.PastramiEngine;
import COM.FutureTense.Util.ftErrors;
import COM.FutureTense.XML.Template.Seed;
import com.fatwire.cs.core.db.PreparedStmt;
import com.fatwire.cs.core.db.StatementParam;
import com.fatwire.cs.core.uri.Definition;

@SuppressWarnings("deprecation")
public class ICSProxyJ implements ICS {

	public String getVersion() { return "11.1.1.6.0"; }
	public int getVersionMajor() { return 11; }
	public int getVersionMinor() { return 6; }
	public int getVersionLevel() { return 0; }
	
	public ICS ics;

	public ICSProxyJ() {

	}

	public ICSProxyJ(ICS ics) {
		init(ics);
	}

	public void init(ICS ics) {
		this.ics = ics;
	}


	public String getSiteId(String siteName) {
		
		String pub = tmp();
		String out = tmp();
					
		FTValList attrs = new FTValList();
		attrs.setValString("NAME", pub);
		attrs.setValString("FIELD", "name");
		attrs.setValString("VALUE", siteName);
		ics.runTag("PUBLICATION.LOAD", attrs);
		
		attrs = new FTValList();
		attrs.setValString("NAME", pub);
		attrs.setValString("FIELD", "id");
		attrs.setValString("OUTPUT", out);
		ics.runTag("PUBLICATION.GET", attrs);
		
		return ics.GetVar(out);
	}

	public Id getSitePlanRoot(String siteName) {
	   return new Id("Publication", Long.parseLong(getSiteId(siteName)));
	}

	public boolean AppEvent(String arg0, String arg1, String arg2,
			FTValList arg3) {
		return ics.AppEvent(arg0, arg1, arg2, arg3);
	}

	public boolean BlobServer(FTValList arg0, IMIMENotifier arg1,
			OutputStream arg2) {
		return ics.BlobServer(arg0, arg1, arg2);
	}

	public boolean BlobServer(FTValList arg0, OutputStream arg1) {
		return ics.BlobServer(arg0, arg1);
	}

	public boolean CallElement(String arg0, FTValList arg1) {
		return ics.CallElement(arg0, arg1);
	}

	public IList CallSQL(String arg0, String arg1, int arg2, boolean arg3,
			boolean arg4, StringBuffer arg5) {
		return ics.CallSQL(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public IList CallSQL(String arg0, String arg1, int arg2, boolean arg3,
			StringBuffer arg4) {
		return ics.CallSQL(arg0, arg1, arg2, arg3, arg4);
	}

	public IList CatalogDef(String arg0, String arg1, StringBuffer arg2) {
		return ics.CatalogDef(arg0, arg1, arg2);
	}

	public IList CatalogIndexDef(String arg0, String arg1, StringBuffer arg2) {
		return ics.CatalogIndexDef(arg0, arg1, arg2);
	}

	public boolean CatalogManager(FTValList arg0, Object arg1) {
		return ics.CatalogManager(arg0, arg1);
	}

	public boolean CatalogManager(FTValList arg0) {
		return ics.CatalogManager(arg0);
	}

	public void ClearErrno() {
		ics.ClearErrno();
	}

	public boolean CommitBatchedCommands(Object arg0) {
		return ics.CommitBatchedCommands(arg0);
	}

	public boolean CopyList(String arg0, String arg1) {
		return ics.CopyList(arg0, arg1);
	}

	public boolean DeleteSynchronizedHash(String arg0) {
		return ics.DeleteSynchronizedHash(arg0);
	}

	public boolean DestroyEvent(String arg0) {
		return ics.DestroyEvent(arg0);
	}

	public void DisableCache() {
		ics.DisableCache();
	}

	public boolean DisableEvent(String arg0) {
		return ics.DisableEvent(arg0);
	}

	public void DisableFragmentCache() {
		ics.DisableFragmentCache();
	}

	public boolean EmailEvent(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, FTValList arg6) {
		return ics.EmailEvent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}

	public boolean EmailEvent(String arg0, String arg1, String arg2, String arg3) {
		return ics.EmailEvent(arg0, arg1, arg2, arg3);
	}

	public boolean EnableEvent(String arg0) {
		return ics.EnableEvent(arg0);
	}

	public boolean FlushCatalog(String arg0) {
		return ics.FlushCatalog(arg0);
	}

	public void FlushStream() {
		ics.FlushStream();
	}

	public byte[] GetBin(String arg0) {
		return ics.GetBin(arg0);
	}

	public int GetCatalogType(String arg0) {
		return ics.GetCatalogType(arg0);
	}

	public FTVAL GetCgi(String arg0) {
		return ics.GetCgi(arg0);
	}

	public int GetCounter(String arg0) throws Exception {
		return ics.GetCounter(arg0);
	}

	public int GetErrno() {
		return ics.GetErrno();
	}

	public IList GetList(String arg0, boolean arg1) {
		return ics.GetList(arg0, arg1);
	}

	public IList GetList(String arg0) {
		return ics.GetList(arg0);
	}

	public Object GetObj(String arg0) {
		return ics.GetObj(arg0);
	}

	public String GetProperty(String arg0, String arg1, boolean arg2) {
		return ics.GetProperty(arg0, arg1, arg2);
	}

	public String GetProperty(String arg0) {
		return ics.GetProperty(arg0);
	}

	public String GetSSVar(String arg0) {
		return ics.GetSSVar(arg0);
	}

	@SuppressWarnings("rawtypes")
	public Enumeration GetSSVars() {
		return ics.GetSSVars();
	}

	public ISearchEngine GetSearchEngine(String arg0, String arg1,
			StringBuffer arg2) {
		return ics.GetSearchEngine(arg0, arg1, arg2);
	}

	public String GetSearchEngineList() {
		return ics.GetSearchEngineList();
	}

	public ISyncHash GetSynchronizedHash(String arg0, boolean arg1, int arg2,
			int arg3, boolean arg4, boolean arg5, @SuppressWarnings("rawtypes") Collection arg6) {
		return ics
				.GetSynchronizedHash(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}

	public ISyncHash GetSynchronizedHash(String arg0, boolean arg1, int arg2,
			int arg3, boolean arg4, boolean arg5) {
		return ics.GetSynchronizedHash(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public String GetVar(String arg0) {
		return ics.GetVar(arg0);
	}

	@SuppressWarnings("rawtypes")
	public Enumeration GetVars() {
		return ics.GetVars();
	}

	public boolean IndexAdd(String arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, String arg6, FTValList arg7,
			FTValList arg8, FTValList arg9, String arg10, String arg11,
			StringBuffer arg12) {
		return ics.IndexAdd(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
				arg8, arg9, arg10, arg11, arg12);
	}

	public boolean IndexCreate(String arg0, FTValList arg1, String arg2,
			String arg3, StringBuffer arg4) {
		return ics.IndexCreate(arg0, arg1, arg2, arg3, arg4);
	}

	public boolean IndexDestroy(String arg0, String arg1, String arg2,
			StringBuffer arg3) {
		return ics.IndexDestroy(arg0, arg1, arg2, arg3);
	}

	public boolean IndexExists(String arg0, String arg1, String arg2,
			StringBuffer arg3) {
		return ics.IndexExists(arg0, arg1, arg2, arg3);
	}

	public boolean IndexRemove(String arg0, String arg1, String arg2,
			String arg3, StringBuffer arg4) {
		return ics.IndexRemove(arg0, arg1, arg2, arg3, arg4);
	}

	public boolean IndexReplace(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, String arg6, FTValList arg7,
			FTValList arg8, FTValList arg9, String arg10, String arg11,
			StringBuffer arg12) {
		return ics.IndexReplace(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
				arg8, arg9, arg10, arg11, arg12);
	}

	public boolean InsertPage(String arg0, FTValList arg1) {
		return ics.InsertPage(arg0, arg1);
	}

	public boolean IsElement(String arg0) {
		return ics.IsElement(arg0);
	}

	public boolean IsSystemSecure() {
		return ics.IsSystemSecure();
	}

	public boolean IsTracked(String arg0) {
		return ics.IsTracked(arg0);
	}

	public boolean IsTrackedNewFormat(String arg0) {
		return ics.IsTrackedNewFormat(arg0);
	}

	public boolean LoadProperty(String arg0) {
		return ics.LoadProperty(arg0);
	}

	public void LogMsg(String arg0) {
		ics.LogMsg(arg0);
	}

	public int Mirror(IList arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, int arg6, boolean arg7, String arg8,
			String arg9, String arg10, String arg11, boolean arg12, int arg13,
			String arg14, StringBuffer arg15) {
		return ics.Mirror(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
				arg9, arg10, arg11, arg12, arg13, arg14, arg15);
	}

	public int Mirror(@SuppressWarnings("rawtypes") Vector arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, int arg6, boolean arg7, boolean arg8,
			int arg9, StringBuffer arg10) {
		return ics.Mirror(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
				arg9, arg10);
	}

	public int Mirror(@SuppressWarnings("rawtypes") Vector arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, int arg6, boolean arg7, String arg8,
			String arg9, String arg10, String arg11, boolean arg12, int arg13,
			StringBuffer arg14) {
		return ics.Mirror(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
				arg9, arg10, arg11, arg12, arg13, arg14);
	}

	public int Mirror(@SuppressWarnings("rawtypes") Vector arg0, @SuppressWarnings("rawtypes") Vector arg1, String arg2, String arg3,
			String arg4, String arg5, String arg6, int arg7, boolean arg8,
			boolean arg9, int arg10, StringBuffer arg11) {
		return ics.Mirror(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
				arg9, arg10, arg11);
	}

	public int Mirror(@SuppressWarnings("rawtypes") Vector arg0, @SuppressWarnings("rawtypes") Vector arg1, String arg2, String arg3,
			String arg4, String arg5, String arg6, int arg7, boolean arg8,
			String arg9, String arg10, String arg11, String arg12,
			boolean arg13, int arg14, StringBuffer arg15) {
		return ics.Mirror(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
				arg9, arg10, arg11, arg12, arg13, arg14, arg15);
	}

	public Seed NewSeedFromTagname(String arg0) {
		return ics.NewSeedFromTagname(arg0);
	}

	public Object PopObj(String arg0) {
		return ics.PopObj(arg0);
	}

	public void PopVars() {
		ics.PopVars();
	}

	public boolean PushObj(String arg0, Object arg1) {
		return ics.PushObj(arg0, arg1);
	}

	public void PushVars() {
		ics.PushVars();
	}

	public IList QueryEvents(String arg0, String arg1, Boolean arg2, String arg3) {
		return ics.QueryEvents(arg0, arg1, arg2, arg3);
	}

	public int RTCommit(String arg0, String arg1, String arg2, boolean arg3) {
		return ics.RTCommit(arg0, arg1, arg2, arg3);
	}

	public int RTDeleteRevision(String arg0, String arg1, int arg2) {
		return ics.RTDeleteRevision(arg0, arg1, arg2);
	}

	public IList RTHistory(String arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, String arg6) {
		return ics.RTHistory(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}

	public IList RTInfo(String arg0, String arg1) {
		return ics.RTInfo(arg0, arg1);
	}

	public int RTLock(String arg0, String arg1) {
		return ics.RTLock(arg0, arg1);
	}

	public int RTRelease(String arg0, String arg1) {
		return ics.RTRelease(arg0, arg1);
	}

	public IList RTRetrieveRevision(String arg0, String arg1, int arg2,
			String arg3) {
		return ics.RTRetrieveRevision(arg0, arg1, arg2, arg3);
	}

	public IList RTRetrieveRevision(String arg0, String arg1, String arg2,
			String arg3) {
		return ics.RTRetrieveRevision(arg0, arg1, arg2, arg3);
	}

	public int RTRollback(String arg0, String arg1, int arg2) {
		return ics.RTRollback(arg0, arg1, arg2);
	}

	public int RTRollback(String arg0, String arg1, String arg2) {
		return ics.RTRollback(arg0, arg1, arg2);
	}

	public int RTSetVersions(String arg0, int arg1) {
		return ics.RTSetVersions(arg0, arg1);
	}

	public int RTTrackTable(String arg0, String arg1, int arg2) {
		return ics.RTTrackTable(arg0, arg1, arg2);
	}

	public int RTUnlockRecord(String arg0, String arg1) {
		return ics.RTUnlockRecord(arg0, arg1);
	}

	public int RTUntrackTable(String arg0) {
		return ics.RTUntrackTable(arg0);
	}

	public IList ReadEvent(String arg0, String arg1) {
		return ics.ReadEvent(arg0, arg1);
	}

	public String ReadPage(String arg0, FTValList arg1) {
		return ics.ReadPage(arg0, arg1);
	}

	public boolean RegisterList(String arg0, IList arg1) {
		return ics.RegisterList(arg0, arg1);
	}

	public void RemoveCounter(String arg0) {
		ics.RemoveCounter(arg0);
	}

	public void RemoveSSVar(String arg0) {
		ics.RemoveSSVar(arg0);
	}

	public void RemoveVar(String arg0) {
		ics.RemoveVar(arg0);
	}

	public boolean RenameList(String arg0, String arg1) {
		return ics.RenameList(arg0, arg1);
	}

	public String ResolveVariables(String arg0, boolean arg1) {
		return ics.ResolveVariables(arg0, arg1);
	}

	public String ResolveVariables(String arg0) {
		return ics.ResolveVariables(arg0);
	}

	public boolean RestoreProperty(boolean arg0) {
		return ics.RestoreProperty(arg0);
	}

	public boolean RollbackBatchedCommands(Object arg0) {
		return ics.RollbackBatchedCommands(arg0);
	}

	public IList SQL(PreparedStmt arg0, StatementParam arg1, boolean arg2) {
		return ics.SQL(arg0, arg1, arg2);
	}

	public IList SQL(String arg0, String arg1, String arg2, int arg3,
			boolean arg4, boolean arg5, StringBuffer arg6) {
		return ics.SQL(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}

	public IList SQL(String arg0, String arg1, String arg2, int arg3,
			boolean arg4, StringBuffer arg5) {
		return ics.SQL(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public String SQLExp(String arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5) {
		return ics.SQLExp(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public String SQLExp(String arg0, String arg1, String arg2, String arg3,
			String arg4) {
		return ics.SQLExp(arg0, arg1, arg2, arg3, arg4);
	}

	public IList Search(String arg0, String arg1, String arg2, String arg3,
			int arg4, FTValList arg5, String arg6, String arg7, String arg8,
			StringBuffer arg9) {
		return ics.Search(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
				arg9);
	}

	public boolean SearchDateToNative(String arg0, StringBuffer arg1,
			String arg2, String arg3, StringBuffer arg4) {
		return ics.SearchDateToNative(arg0, arg1, arg2, arg3, arg4);
	}

	public IList SelectTo(String arg0, String arg1, String arg2, String arg3,
			int arg4, String arg5, boolean arg6, StringBuffer arg7) {
		return ics.SelectTo(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}

	public boolean SendMail(String arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, FTValList arg6) {
		return ics.SendMail(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}

	public boolean SendMail(String arg0, String arg1, String arg2, String arg3,
			String arg4) {
		return ics.SendMail(arg0, arg1, arg2, arg3, arg4);
	}

	public boolean SendMail(String arg0, String arg1, String arg2) {
		return ics.SendMail(arg0, arg1, arg2);
	}

	public boolean SessionExists(String arg0) {
		return ics.SessionExists(arg0);
	}

	public String SessionID() {
		return ics.SessionID();
	}

	public boolean SetCookie(String arg0, String arg1, int arg2, String arg3,
			String arg4, boolean arg5) {
		return ics.SetCookie(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public void SetCounter(String arg0, int arg1) throws Exception {
		ics.SetCounter(arg0, arg1);
	}

	public void SetErrno(int arg0) {
		ics.SetErrno(arg0);
	}

	public boolean SetObj(String arg0, Object arg1) {
		return ics.SetObj(arg0, arg1);
	}

	public void SetSSVar(String arg0, int arg1) {
		ics.SetSSVar(arg0, arg1);
	}

	public void SetSSVar(String arg0, String arg1) {
		ics.SetSSVar(arg0, arg1);
	}

	public void SetVar(String arg0, FTVAL arg1) {
		ics.SetVar(arg0, arg1);
	}

	public void SetVar(String arg0, int arg1) {
		ics.SetVar(arg0, arg1);
	}

	public void SetVar(String arg0, String arg1) {
		ics.SetVar(arg0, arg1);
	}

	public Object StartBatchContext() {
		return ics.StartBatchContext();
	}

	public void StreamBinary(byte[] arg0, int arg1, int arg2) {
		ics.StreamBinary(arg0, arg1, arg2);
	}

	public void StreamEvalBytes(String arg0) {
		ics.StreamEvalBytes(arg0);
	}

	public void StreamHeader(String arg0, String arg1) {
		ics.StreamHeader(arg0, arg1);
	}

	public void StreamText(String arg0) {
		ics.StreamText(arg0);
	}

	public void ThrowException() {
		ics.ThrowException();
	}

	public boolean TreeManager(FTValList arg0, Object arg1) {
		return ics.TreeManager(arg0, arg1);
	}

	public boolean TreeManager(FTValList arg0) {
		return ics.TreeManager(arg0);
	}

	public boolean UserIsMember(String arg0) {
		return ics.UserIsMember(arg0);
	}

	public void close() {
		ics.close();
	}

	public boolean dbDebug() {
		return ics.dbDebug();
	}

	public void decode(String arg0, @SuppressWarnings("rawtypes") Map arg1) {
		ics.decode(arg0, arg1);
	}

	public IJSPObject deployJSPData(String arg0, String arg1, StringBuffer arg2) {
		return ics.deployJSPData(arg0, arg1, arg2);
	}

	public IJSPObject deployJSPFile(String arg0, String arg1, StringBuffer arg2) {
		return ics.deployJSPFile(arg0, arg1, arg2);
	}

	public String diskFileName() {
		return ics.diskFileName();
	}

	public String diskFileName(String arg0, FTValList arg1) {
		return ics.diskFileName(arg0, arg1);
	}

	public String diskFileName(String arg0, String arg1) {
		return ics.diskFileName(arg0, arg1);
	}

	public String encode(String arg0, @SuppressWarnings("rawtypes") Map arg1, boolean arg2) {
		return ics.encode(arg0, arg1, arg2);
	}

	public boolean eventDebug() {
		return ics.eventDebug();
	}

	public String genID(boolean arg0) {
		return ics.genID(arg0);
	}

	public Object getAttribute(String arg0) {
		return ics.getAttribute(arg0);
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getAttributeNames() {
		return ics.getAttributeNames();
	}

	public ftErrors getComplexError() {
		return ics.getComplexError();
	}

	public String getCookie(String arg0) {
		return ics.getCookie(arg0);
	}

	public String getElementArgumentValue(String arg0, String arg1) {
		return ics.getElementArgumentValue(arg0, arg1);
	}

	public IProperties getIProperties() {
		return ics.getIProperties();
	}

	@SuppressWarnings("deprecation")
	public COM.FutureTense.Interfaces.IServlet getIServlet() {
		return ics.getIServlet();
	}

	public String getLocaleString(String arg0, String arg1) {
		return ics.getLocaleString(arg0, arg1);
	}

	public String getNamespace() {
		return ics.getNamespace();
	}

	public PageData getPageData(String arg0) {
		return ics.getPageData(arg0);
	}

	public PastramiEngine getPastramiEngine() {
		return ics.getPastramiEngine();
	}

	public String getSQL(String arg0) {
		return ics.getSQL(arg0);
	}

	public Satellite getSatellite(String arg0) {
		return ics.getSatellite(arg0);
	}

	public int getTrackingStatus(String arg0, String arg1) {
		return ics.getTrackingStatus(arg0, arg1);
	}

	public String getURL(Definition arg0, String arg1) {
		return ics.getURL(arg0, arg1);
	}

	@SuppressWarnings("deprecation")
	public String getURL(COM.FutureTense.Interfaces.IURLDefinition arg0) {
		return ics.getURL(arg0);
	}

	public Principal getUserPrincipal() {
		return ics.getUserPrincipal();
	}

	public byte[] grabCacheStatus() {
		return ics.grabCacheStatus();
	}

	public FTValList grabHeaders() {
		return ics.grabHeaders();
	}

	public boolean ioErrorThrown() {
		return ics.ioErrorThrown();
	}

	public boolean isCacheable(String arg0) {
		return ics.isCacheable(arg0);
	}

	public String literal(String arg0, String arg1, String arg2) {
		return ics.literal(arg0, arg1, arg2);
	}

	public String[] pageCriteriaKeys(String arg0) {
		return ics.pageCriteriaKeys(arg0);
	}

	public String pageURL() {
		return ics.pageURL();
	}

	public String pageURL(String arg0, FTValList arg1) {
		return ics.pageURL(arg0, arg1);
	}

	public boolean pastramiDebug() {
		return ics.pastramiDebug();
	}

	public boolean pgCacheDebug() {
		return ics.pgCacheDebug();
	}

	public void removeAttribute(String arg0) {
		ics.removeAttribute(arg0);
	}

	public boolean rsCacheDebug() {
		return ics.rsCacheDebug();
	}

	public String runTag(String arg0, FTValList arg1) {
		return ics.runTag(arg0, arg1);
	}

	public boolean sessionDebug() {
		return ics.sessionDebug();
	}

	public void setAttribute(String arg0, Object arg1) {
		ics.setAttribute(arg0, arg1);
	}

	public void setComplexError(ftErrors arg0) {
		ics.setComplexError(arg0);
	}

	public boolean syncDebug() {
		return ics.syncDebug();
	}

	public boolean systemDebug() {
		return ics.systemDebug();
	}

	public boolean systemSession() {
		return ics.systemSession();
	}

	public boolean timeDebug() {
		return ics.timeDebug();
	}

	public boolean xmlDebug() {
		return ics.xmlDebug();
	}

}
