package com.yimei.cflow.asset.db

import java.sql.Timestamp

import com.yimei.cflow.api.models.database.AssetDBModel.AssetEntity
import com.yimei.cflow.config.DatabaseConfig._

/**
  * Created by hary on 16/12/31.
  */
trait AssetTable {

  import driver.api._

  class AssetClass(tag:Tag) extends Table[AssetEntity](tag, "asset"){
    def id          = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def asset_id    = column[String]("asset_id")
    def file_type   = column[Int]("file_type")
    def busi_type   = column[Int]("busi_type")
    def username    = column[String]("username")
    def gid         = column[Option[String]]("gid")
    def description = column[Option[String]]("description")
    def url         = column[String]("url")
    def ts_c        = column[Timestamp]("ts_c")

    def * = (id, asset_id, file_type, busi_type, username, gid, description, url, ts_c) <> (AssetEntity.tupled,AssetEntity.unapply)
  }

  protected val assetClass = TableQuery[AssetClass]

}
