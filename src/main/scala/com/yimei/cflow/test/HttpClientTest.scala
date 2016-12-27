package com.yimei.cflow.test

import java.sql.Timestamp
import java.time.Instant

import com.yimei.cflow.api.http.models.AdminModel.AdminProtocol
import com.yimei.cflow.api.http.models.TaskModel.{TaskProtocol, UserSubmitMap}
import com.yimei.cflow.api.http.models.UserModel.UserModelProtocol
import com.yimei.cflow.graph.cang.models.CangFlowModel._

/**
  * Created by wangqi on 16/12/21.
  */
//class HttpClientTest extends Actor with ActorLogging with CoreConfig{
//
//  import akka.pattern.pipe
//  import context.dispatcher
//
//  val http = Http(context.system)
//  val url = "http://localhost:9000"
//
//
//  override def receive: Receive = {
//    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
//      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
//        log.info("Got response, body: " + body.utf8String)
//      }
//    case resp @ HttpResponse(code, _, _, _) =>
//      log.info("Request failed, response code: " + code)
//      resp.discardEntityBytes()
//  }
//}
import spray.json._

object ClientMain extends App with AdminProtocol with TaskProtocol with UserModelProtocol{
  //println(Map[String,String]("wang"->"qi","wang2"->"qi2").toJson.prettyPrint)

  //注意：此处调用方应该使用wrap
  //println(UserSubmitEntity("ying!rz-1!haryId1!1","TKPU1",Map("KPU1"->DataPoint("zj-1!wangqiId1",None,Some("wang"),UUID.randomUUID().toString, 0L, false))).toJson.prettyPrint)

 // println(UserSubmitEntity("ying!rz-1!haryId1!1","PU",Map(("PU1"->DataPoint("50",None,Some("wang"),UUID.randomUUID().toString, 0L, false)),
                                                           // ("PU2"->DataPoint("50",None,Some("wang"),UUID.randomUUID().toString, 0L, false)))).toJson.prettyPrint)

 // println(Map("LoanReceipt"->UserSubmitMap(Some("pdf"),"http://www.pdf995.com/samples/pdf.pdf")).toJson.prettyPrint)

  val fileObj1 = FileObj("文件1","www.baidu.com","12345","h",None)

  val fileObj2 = FileObj("文件2","www.baidu.com","23456","h",Some(Timestamp.from(Instant.now)))



  val sf = StartFlowBasicInfo(1111,"wangqi","13000000001",11111111,"阿里巴巴","123",Timestamp.from(Instant.now),Timestamp.from(Instant.now),"企鹅",BigDecimal(1000),30,
    BigDecimal(0.5),"不知道",1,BigDecimal(0.01),BigDecimal(0.02),"heheh",BigDecimal(0.01),"没合同","24678",List(fileObj1,fileObj2))


  val sfi = StartFlowInvestigationInfo("阿里巴巴","阿里巴巴","企鹅","百度","heheh ","hahah","123","hhwh","wwww",BigDecimal(1000),1,BigDecimal(0.01),Timestamp.from(Instant.now),
  "wawa","sdasda","11ss","adas","qqqq","asdsa")

  val sfs = StartFlowSupervisorInfo("上海","天空","1000弄","hehe","heihei","haha","Bukan","ssss","没意见")

  val sflow = StartFlow(sf,sfi,sfs)

  println(sflow.toJson.prettyPrint)



}