// 用户任务管理  and 运营组任务管理
GET /utask/:party/:instance_id/:userId                           -- 查询用户任务(!)
GET /utask/:party/:instance_id/:userId?history=1                 -- 查询用户任务history(!)
PUT /utask/:party/:instance_id/:userId/:taskId                   -- 提交用户提交任务(!)

GET /gtask/:party/:instance_id/:userId?limit=10&offset=20        -- 查询用户组任务列表 - 只要是用户所在的组, 任务都查出来(!)
PUT /gtask/:party/:instance_id/:userId/:taskId/:gid                   -- claim任务(!)

// 自动任务管理
POST /auto/:flowType/:flowId/:autoTask   -- 手动触发指定流程的指定任务

// 流程管理
POST /flow/user/:party/:instance_id/:userId?flowType=:flowType                     -- 创建流程(!)
GET  /flow/user/:party/:instance_id/:userId?flowType=:flowType&status=:status&limit=10&offset=20  -- 查询用户流程, 如果没有指定类型就是全部类型流程  !!!
GET  /flow/:flowId                                                              -- 查询指定流程(!)
GET  /flow?
flowId=:flowId&
flowType=:flowType&
userType=:userType&
userId=:userId&
status=:status&
limit=:limit&
offset=:offset&

PUT  /flow/admin/hijack/:flowId                                                 --  hijack 流程
