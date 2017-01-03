package com.yimei.cflow.test

import java.sql.Timestamp
import java.time.Instant

import com.yimei.cflow.api.http.models.AdminModel.{AdminProtocol, HijackEntity}
import com.yimei.cflow.api.http.models.TaskModel.{TaskProtocol, UserSubmitMap}
import com.yimei.cflow.api.http.models.UserModel.UserModelProtocol
import com.yimei.cflow.graph.cang.models.CangFlowModel.{TraderAudit, _}

/**
  * Created by wangqi on 16/12/21.
  */
import spray.json._

object ClientMain extends App with AdminProtocol with TaskProtocol with UserModelProtocol{
  //println(Map[String,String]("wang"->"qi","wang2"->"qi2").toJson.prettyPrint)

  //注意：此处调用方应该使用wrap
  //println(UserSubmitEntity("ying!rz-1!haryId1!1","TKPU1",Map("KPU1"->DataPoint("zj-1!wangqiId1",None,Some("wang"),UUID.randomUUID().toString, 0L, false))).toJson.prettyPrint)

 // println(UserSubmitEntity("ying!rz-1!haryId1!1","PU",Map(("PU1"->DataPoint("50",None,Some("wang"),UUID.randomUUID().toString, 0L, false)),
                                                           // ("PU2"->DataPoint("50",None,Some("wang"),UUID.randomUUID().toString, 0L, false)))).toJson.prettyPrint)

 // println(Map("LoanReceipt"->UserSubmitMap(Some("pdf"),"http://www.pdf995.com/samples/pdf.pdf")).toJson.prettyPrint)

  val fileObj1 = FileObj("文件1","www.baidu.com","12345")

  val fileObj2 = FileObj("文件2","www.baidu.com","23456")



  val sf = StartFlowBasicInfo("1111","wangqi","13000000001","11111111","阿里巴巴","123",Timestamp.from(Instant.now),Timestamp.from(Instant.now),"企鹅",BigDecimal(1000),30,
    BigDecimal(0.5),"不知道",1,BigDecimal(0.01),BigDecimal(0.02),"heheh",BigDecimal(0.01),"没合同","24678",List(fileObj1,fileObj2))


  val sfi = StartFlowInvestigationInfo("阿里巴巴","阿里巴巴","企鹅","百度","heheh ","hahah","123","hhwh","wwww",BigDecimal(1000),1,BigDecimal(0.01),Timestamp.from(Instant.now),
  "wawa","sdasda","11ss","adas","qqqq","asdsa")

  val sfs = StartFlowSupervisorInfo("上海","天空","1000弄","hehe","heihei","haha","Bukan","ssss","没意见")

  val sflow = StartFlow(sf,sfi,sfs)

 // println(sflow.toJson.prettyPrint)


  val a = "hello"

  //println(a)
  //println(a.toJson.toString)

  val t = UploadContract("1234","12345",List(fileObj1,fileObj2))

 // println(t.toJson.toString)

  val t1 = HarborUploadContract("123","123",1000.12,List(fileObj1,fileObj2))
 // println(t1.toJson.toString)

  val t3 = TraderAudit("123","123",1,1024.1)
  //println(t3.toJson.toString)

  val t4 = TraffickerNoticePortReleaseGoods("123","123",1024.1,"腾讯",List(fileObj1,fileObj2))
  println(t4.toJson.toString)

}