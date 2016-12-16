package com.yimei.cflow.graph.cang

import com.yimei.cflow.graph.cang.models.FileObj
import spray.json.DefaultJsonProtocol

/**
  * Created by hary on 16/12/15.
  */
trait RestJsonProtocol extends DefaultJsonProtocol {

  implicit val fileObjFormat = jsonFormat3(FileObj)

}
