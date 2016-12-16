package com.yimei.cflow.graph.cang.models

import java.time.LocalDateTime
import BaseFormatter._

case class AddCompany(name: String, userType: String, createTime: LocalDateTime, createManId: BigInt, lastUpdateTime: LocalDateTime, lastUpdateManId: BigInt)

case class UpdateCompany(id: BigInt, name: String, userType: String, lastUpdateTime: LocalDateTime, lastUpdateManId: BigInt)
