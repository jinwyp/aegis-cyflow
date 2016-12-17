package com.yimei.cflow.core

import java.io.File

/**
  * Created by hary on 16/12/17.
  */
object GraphLoader {

  def kload: FlowGraph = {
    var module = this.getClass.getClassLoader.loadClass("com.yimei.cflow.graph.ying.YingGraph$")
    module.getField("MODULE$").get(null).asInstanceOf[FlowGraph]
  }


  def load() = {

    /*
   * need to specify parent, so we have all class instances
   * in current context
   */
    var classLoader = new java.net.URLClassLoader(Array(new File("module.jar").toURI.toURL),
      this.getClass.getClassLoader)

    /*
   * please note that the suffix "$" is for Scala "object",
   * it's a trick
   */
    var clazzExModule = classLoader.loadClass("com.yimei.graph.ying.Ying" + "$")

    /*
   * currently, I don't know how to check if clazzExModule is instance of
   * Class[Module], because clazzExModule.isInstanceOf[Class[_]] always
   * returns true,
   * so I use try/catch
   */
    try {
      //"MODULE$" is a trick, and I'm not sure about "get(null)"
      var module = clazzExModule.getField("MODULE$").get(null).asInstanceOf[FlowGraph]
    } catch {
      case e: java.lang.ClassCastException =>
        printf(" - %s is not Module\n", clazzExModule)
    }
  }
}
