package wcs.java;
    
import COM.FutureTense.Interfaces.*;
import java.util.logging.*;
import java.lang.String;
    
public class ListobjectTag1  {    
  private static boolean debug = true;
  private static Logger log = Logger.getLogger("ListobjectTag");  
  static String ftValList2String(String name, FTValList vl)  {
    StringBuilder sb = new StringBuilder();
    sb.append(">>>>>");
    sb.append(name);
    sb.append(":");
    java.util.Enumeration en = vl.keys();
    while (en.hasMoreElements()) {
      String k = en.nextElement().toString();
      Object v = vl.getValString(k);
      sb.append( " "+k+"="+v);
    }
    return sb.toString();
  }
    
 
public static class Argument {
  private FTValList args = new FTValList();
  public Argument set(String name, String value) { args.setValString(name,value); return this; }


  public Argument(String name,String value) {
args.setValString("name", name);
args.setValString("value", value);
  }

  public int run(ICS ics) { 
      ics.runTag("LISTOBJECT.ARGUMENT", args); 
      if(debug) {
         java.lang.System.err.println(ftValList2String("Argument", args));
         log.finest(ftValList2String("Argument", args));
      }
      return ics.GetErrno(); 
  }
  
  private java.util.Random rnd = new java.util.Random();
  public String run(ICS ics, String output) {
	  String tmp = "_OUT_"+(rnd.nextInt());
	  args.setValString(output, tmp);
	  run(ics);
	  String res = ics.GetVar(tmp);
	  ics.RemoveVar(tmp);
	  return res;		
  }	
}

public static Argument argument(String name,String value) {
  return new Argument(name,value);
}


public static class Create {
  private FTValList args = new FTValList();
  public Create set(String name, String value) { args.setValString(name,value); return this; }


  public Create(String name,String columns) {
args.setValString("NAME", name);
args.setValString("COLUMNS", columns);
  }

  public int run(ICS ics) { 
      ics.runTag("LISTOBJECT.CREATE", args); 
      if(debug) {
         java.lang.System.err.println(ftValList2String("Create", args));
         log.finest(ftValList2String("Create", args));
      }
      return ics.GetErrno(); 
  }
  
  private java.util.Random rnd = new java.util.Random();
  public String run(ICS ics, String output) {
	  String tmp = "_OUT_"+(rnd.nextInt());
	  args.setValString(output, tmp);
	  run(ics);
	  String res = ics.GetVar(tmp);
	  ics.RemoveVar(tmp);
	  return res;		
  }	
}

public static Create create(String name,String columns) {
  return new Create(name,columns);
}


public static class Tolist {
  private FTValList args = new FTValList();
  public Tolist set(String name, String value) { args.setValString(name,value); return this; }


  public Tolist(String name,String listvarname) {
args.setValString("NAME", name);
args.setValString("LISTVARNAME", listvarname);
  }

  public int run(ICS ics) { 
      ics.runTag("LISTOBJECT.TOLIST", args); 
      if(debug) {
         java.lang.System.err.println(ftValList2String("Tolist", args));
         log.finest(ftValList2String("Tolist", args));
      }
      return ics.GetErrno(); 
  }
  
  private java.util.Random rnd = new java.util.Random();
  public String run(ICS ics, String output) {
	  String tmp = "_OUT_"+(rnd.nextInt());
	  args.setValString(output, tmp);
	  run(ics);
	  String res = ics.GetVar(tmp);
	  ics.RemoveVar(tmp);
	  return res;		
  }	
}

public static Tolist tolist(String name,String listvarname) {
  return new Tolist(name,listvarname);
}


public static class Addrow {
  private FTValList args = new FTValList();
  public Addrow set(String name, String value) { args.setValString(name,value); return this; }


  public Addrow(String name) {
args.setValString("NAME", name);
  }

  public int run(ICS ics) { 
      ics.runTag("LISTOBJECT.ADDROW", args); 
      if(debug) {
         java.lang.System.err.println(ftValList2String("Addrow", args));
         log.finest(ftValList2String("Addrow", args));
      }
      return ics.GetErrno(); 
  }
  
  private java.util.Random rnd = new java.util.Random();
  public String run(ICS ics, String output) {
	  String tmp = "_OUT_"+(rnd.nextInt());
	  args.setValString(output, tmp);
	  run(ics);
	  String res = ics.GetVar(tmp);
	  ics.RemoveVar(tmp);
	  return res;		
  }	
}

public static Addrow addrow(String name) {
  return new Addrow(name);
}

}
