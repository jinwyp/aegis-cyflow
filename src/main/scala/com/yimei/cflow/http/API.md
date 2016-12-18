// 参与方类别管理
GET  /party?limit=10&offset=20         参与方类别列表
POST /party/:class                     创建参与方类别
GET  /party/:class                     查询参与方类别
PUT  /party/:class                     更新参与方类别

// 参与方运营组管理
GET    /pgroup?limit=10&offset=20  参与方运营组列表
POST   /pgroup/:gname              创建参与方运营组
DELETE /pgroup/:gname              删除参与方运营组
PUT    /pgroup/:gname              更新参与方运营组

// 参与方实例管理
POST /inst/:class/:class_id           创建参与方实例
PUT  /inst/:class/:class_id           更新参与方实例
GET  /inst/:class/:class_id           查询参与方实例

// 参与方用户管理(:class + :class_id = userType)
POST /user/:class/:class_id/:userId                 创建用户
GET  /user/:class/:class_id/:userId                 查询用户  -- 应该拿到: 1. 用户的基本信息, 2. 用户的任务
GET  /user/:class/:class_id?limit=10&offset=20      用户列表  -- 拿到用户的列表信息
PUT  /user/:class/:class_id:/:userId                更新用户  -- 更新用户的基本信息

// 用户任务管理
GET /utask/:class/:class_id/:userId?history                   -- 查询用户任务, 如果有history参数, 则也包含history信息
PUT /utask/:class/:class_id/:userId/:taskId                   -- 提交用户提交任务

// 运营组任务管理
GET /gtask/:class/:class_id/:userId?limit=10&offset=20        -- 查询用户组任务列表 - 只要是用户所在的组, 任务都查出来
PUT /gtask/:class/:class_id/:userId/:taskId                   -- claim任务

// 自动任务管理
POST /auto/:flowType/:flowId/:autoTask   -- 手动触发指定流程的指定任务

// 流程管理
POST /flow/user/:class/:class_id/:userId?flowType=:flowType                     -- 创建流程
GET  /flow/user/:class/:class_id/:userId?flowType=:flowType&limit=10&offset=20  -- 查询用户流程
GET  /flow/:flowId                                                              -- 查询指定流程
PUT  /flow/admin/:flowId?action=:action                                         -- 流程admin, action = fail(让流程失败), action = trigger(触发流程在当且点继续执行)