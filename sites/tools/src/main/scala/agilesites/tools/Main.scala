package agilesites.tools

import scalafx.application.JFXApp

object Main extends JFXApp {
  
  println(parameters.raw.headOption.getOrElse("hello"))
  
  stage = parameters.raw.headOption.getOrElse("hello") match {
    
  	case "Downloader" => DownloaderStage
    
    case "Worker" => WorkerStage
    
    case _ => new InfoStage("command not recognized")
  }

}