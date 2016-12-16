package com.yimei.cflow.graph

/**
  * Created by hary on 16/12/15.
  */
class UserConfig {

  // 参与方大类
  val rzf_class = "rz" // 融资方
  val zjf_class = "zj" // 资金方
  val myf_class = "my" // 贸易方
  val gkf_class = "gk" // 港口方
  val jgf_class = "jg" // 监管方

  //系统中全部的参与方
  val all_classes = Array(rzf_class,zjf_class,myf_class,gkf_class,jgf_class)


  // 融资方运营组
  val rzf_yewu = "yw"  //融资方业务

  // 监管费运营组
  val jgf_yewu = "yw"  //监管方业务

  // 港口运营组
  val gkf_yewu = "yw"  //港口方业务
  // 贸易方运营组

  val myf_caiwu = "cw" // 贸易方财务
  val myf_yewu  = "yw" // 贸易方业务

  // 资金方运营组
  val zjf_caiwu = "cw" // 资金方财务
  val zjf_yewu  = "yw" // 资金方业务



  val roleList  = Map[String, String](
    jgf_yewu -> "监管业务员员",
    gkf_yewu -> "港口业务员员",
    zjf_yewu -> "资金方业务员",
    zjf_caiwu -> "资金方财务"
  );

}
