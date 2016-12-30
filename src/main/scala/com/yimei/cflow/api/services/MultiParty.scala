package com.yimei.cflow.api.services

import com.yimei.cflow.organ.db.{PartyClassTable, PartyGroupTable, PartyUserTable, UserGroupTable}


/**
  * Created by hary on 16/12/29.
  */
object MultiParty extends PartyClassTable with PartyGroupTable with PartyUserTable with UserGroupTable {

}
