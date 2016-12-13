package com.yimei.cflow.auto

import akka.actor.{ActorRef, Props}

/**
  * Created by hary on 16/12/13.
  */
object AutoRegistry {

  var propMap: Map[String, Map[String, ActorRef] => Props] = Map()

  /**
    *
    * @param name
    * @param f
    */
  def register(name: String, f: Map[String, ActorRef] => Props) = {
    propMap = propMap + ( name -> f)
  }

}
