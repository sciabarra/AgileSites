import agilesites.build.scrivener._
import Playground._

import java.io.File

object ParserPlay extends  Common {

 println("Hello")                                 //> Hello

src                                               //> res0: java.io.File = A:\agilesites\2.0\book\content.scriv
 
 val p = new Parser(src)                          //> A:\agilesites\2.0\book\content.scriv\project.scrivx
                                                  //| EvElemStart(null,ScrivenerProject, Creator="SCRWIN-1.7.1.0" Version="1.0" Te
                                                  //| mplate="No",)
                                                  //| 
                                                  //|     
                                                  //| EvElemStart(null,Binder,,)
                                                  //| 
                                                  //|         
                                                  //| >>> push Node(0,DraftFolder,List(),,)
                                                  //| 
                                                  //|             
                                                  //| Bozza
                                                  //| 
                                                  //|             
                                                  //| EvElemStart(null,MetaData,,)
                                                  //| 
                                                  //|                 
                                                  //| EvElemStart(null,NotesTextSelection,,)
                                                  //| 0,0
                                                  //| EvElemEnd(null,NotesTextSelection)
                                                  //| 
                                                  //|             
                                                  //| EvElemEnd(null,MetaData)
                                                  //| 
                                                  //|             
                                                  //| EvElemStart(null,CorkboardAndOutliner,,)
                                                  //| 
                                                  //|                 
                                                  //| EvElemStart(null,CorkboardSettings,,)
                                                  //| 
                                                  //|                     
                                                  //| EvElemStart(null,FreeformMode,,)
                                                  //| Yes
                                                  //| EvElemEnd(null,FreeformMode)
                                                  //| 
                                                  //|                 
                                                  //| EvElemEnd(null,CorkboardSettings)
                                                  //| 
                                                  //|             
                                                  //| EvElemEnd(null,CorkboardAndOutliner)
                                                  //| 
                                                  //|             
                                                  //| EvElemStart(null,Children,,)
                                                  //| 
                                                  //|                 
                                                  //| 
                                                  //| Output exceeds cutoff limit.
 
 
 val st = p.stack                                 //> st  : List[ParserPlay.p.Node] = List(Node(-1,Home,List(Node(0,DraftFolder,Li
                                                  //| st(Node(4,Folder,List(Node(3,Text,List(Node(7,Text,List(),item,Item), Node(9
                                                  //| ,Text,List(),item2,Item2)),document,Document), Node(6,Folder,List(Node(5,Tex
                                                  //| t,List(),document,Document)),subsection,subsection)),section,section)),bozza
                                                  //| ,Bozza), Node(1,ResearchFolder,List(),ricerca,Ricerca), Node(2,TrashFolder,L
                                                  //| ist(),cestino,Cestino)),stato,Stato))
 
 st.size                                          //> res1: Int = 1

 // dump(st(0),1)
 
  p.map.keys                                      //> res2: Iterable[String] = Set(/stato, /stato/bozza, /stato/cestino, /stato/bo
                                                  //| zza/section/document/item2, /stato/bozza/section/document, /stato/ricerca, /
                                                  //| stato/bozza/section/subsection, /stato/bozza/section/document/item, /stato/b
                                                  //| ozza/section/subsection/document, /stato/bozza/section)
  
  
}