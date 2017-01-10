package com.yimei.cflow.asset.service

import com.yimei.cflow.api.models.database.AssetDBModel.AssetEntity
import com.yimei.cflow.asset.db.AssetTable
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.api.util.DBUtils.dbrun
import com.yimei.cflow.config.CoreConfig._

import scala.concurrent.Future

/**
  * Created by wangqi on 17/1/6.
  */
object AssetService extends AssetTable {
  import driver.api._

  def getFiles(fileIds:List[String]): Future[Seq[AssetEntity]] = {

    dbrun((for(
      f <- assetClass if f.asset_id inSetBind(fileIds)
    ) yield {
      f
    }).result)

  }

}
