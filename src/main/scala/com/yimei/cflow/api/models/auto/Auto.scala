package com.yimei.cflow.api.models.auto

import com.yimei.cflow.api.models.flow.State


/**
  *
  * @param state     State
  * @param actorName actorName
  */
case class CommandAutoTask(state: State, flowType: String, actorName: String)

