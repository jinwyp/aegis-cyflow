package com.yimei.cflow.api.http.client
import com.yimei.cflow.api.util.HttpUtil._

import scala.concurrent.Future


/**
  * Created by hary on 16/12/23.
  */
trait AdminClient {

  def createFlow(party:String,instance_id:String,user_id:String,flowType:String,initdata:Map[String,String]): Future[String] = {
    sendRequest(path = "api/flow",paramters = Map("flowType"->flowType),pathVariables = Array(party,instance_id,user_id),method = "post")
  }

}
