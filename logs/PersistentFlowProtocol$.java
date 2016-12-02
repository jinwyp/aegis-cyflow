package com.yimei.cflow.core;

/**
  * Created by hary on 16/12/1.
  */
object PersistentFlowProtocol extends DefaultJsonProtocol {

  // 日期
  implicit object DateJsonFormat extends RootJsonFormat[Date] {
    val formatter = new SimpleDateFormat("yyyyMMdd")
    override def write(obj: Date) = JsString(formatter.format(obj))
    override def read(json: JsValue) : Date = json match {
      case JsString(s) => formatter.parse(s)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }

  // 序列化Decision
  implicit  object DecisionFormat extends RootJsonFormat[Decision] {
    def write(c: Decision) = JsString(c.toString)
    def read(value: JsValue) = null
  }

  // 序列Edge
  implicit object EdgeFormat extends RootJsonFormat[Edge] {
    def write(c: Edge) = JsString(c.toString)
    def read(value: JsValue) = null
  }

  // 数据点
  implicit val dataPointFormat = jsonFormat4(DataPoint)

  // 状态
  implicit val stateFormat = jsonFormat5(State)
}