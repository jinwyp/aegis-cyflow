package com.yimei.cflow.engine

import com.yimei.cflow.engine.flow.FlowService
import com.yimei.cflow.engine.group.GroupService
import com.yimei.cflow.engine.user.UserService

/**
  * Created by hary on 17/1/6.
  */
trait EngineService extends FlowService with GroupService with UserService
