package com.yimei.cflow.asset.db

import java.sql.Timestamp

import com.yimei.cflow.api.models.database.AssetDBModel.AssetEntity
import com.yimei.cflow.config.DatabaseConfig._

/**
  * Created by hary on 16/12/31.
  */
trait AssetTable {

  import driver.api._

  class AssetClass(tag:Tag) extends Table[AssetEntity](tag,"asset"){
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def asset_id = column[String]("asset_id")
    def file_type = column[Int]("file_type")
    def description = column[String]("description")
    def uri = column[String]("uri")
    def ts_c = column[Timestamp]("ts_c")

    def * = (id, asset_id, file_type, description, uri, ts_c)<>(AssetEntity.tupled,AssetEntity.unapply)
  }

  protected val assetClass = TableQuery[AssetClass]

}