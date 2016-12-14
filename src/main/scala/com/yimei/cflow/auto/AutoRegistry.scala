package com.yimei.cflow.auto

import akka.actor.{ActorRef, Props}

/**
  * Created by hary on 16/12/13.
  */
//object AutoRegistry {
//
//  var propMap: Map[String, Map[String, ActorRef] => Props] = Map()
//
//  var actorMap: Map[String, Map[String, Array[String]]] = Map()
//
//  /**
//    *
//    * @param name
//    * @param f
//    */
//  def register(flowType:String, name: String, f: Map[String, ActorRef] => Props, actorMapping: Map[String, Array[String]]) = {
//    propMap = propMap + ( flowType+"."+name -> f)
//
//    actorMap = actorMap + (flowType -> actorMapping)
//  }
//
//}
