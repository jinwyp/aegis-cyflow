# aegis-cyflow
cy flow



```
  ///////////////////////////////////////////////////////////////////////////////////////
  //      \ ----------------------> VoidEdge
  //      V0
  //       \ ---------------------> A(data_A) B(data_B) C(data_C)   E1  : data gather
  //       V1
  //         \--------------------> [D, E, F](data_DEF)             E2  : data gather
  //         v2
  //          \-------------------> [D, E, F]                       E3  : data gather
  //          V3
  //           \------------------> [G, H, K]                       E4  : data gather
  //            V4
  //           /  \---------------> [UA1, UA2](task_A)              E5  : user gather
  //          V7   V5 
  //         /      \-------------> [UB1, UB2](task_B)              E6  : user gather
  //        V8       V6
  ///////////////////////////////////////////////////////////////////////////////////////
```

## todo list

1. add flowType to message to support        todo   done
2. add userType to message                   todo   maybe not needed  could create multi userMaster
   just like flowType 
   to support multiple user-types  
3. change serialization to custom            todo
4. change serialization to protobuf          todo
5. angular2 + D3 for operation visualization todo
6. generic type support for DataPoint        todo
7. cluster sharding                          todo
8. add swagger documentation                 todo 
10. schema migration                         todo
11. neo4j for permission management          todo
12. load graph from configuration file       todo
13. log optimization                         todo
14. websocket push                           todo
15. sbt-package-native                       todo
16. multi-edge support                       todo
17. edge description refurbishment           todo
18. service-proxy dispatcher configuration   todo
19. rest api                                 todo   doing

## rest api

创建用户
1. POST /user/:userId?userType=:userType
   Optional -  HierarchyInfo(superior: Option[String], subordinates: Option[Array[String]])  - 组织关系  

更新用户组织关系
2. PUT /usrer/:userType/:userId
   Optional -  HierarchyInfo(superior: Option[String], subordinates: Option[Array[String]])  - 组织关系  

查询用户
2. GET  /user/:userId?type=01

提交用户任务
3. POST /user/:userType/:userId/task/:taskId

创建流程流程
3. POST /flow?userId=:userId&userType=:userType&flowType=:flowType

查询用户流程
4. GET  /flow/:flowId  查询用户流程

更新流程的数据点, 并触发流程继续
5. PUT  /flow/:flowId?key1=v1&k2=v2...kn=vn

重新触发决策点运行
6. PUT /flow/:flowId/decision

流程参与方更新 ???
7. POST /flow/:flowId/party

重新让采集点为流程flowId重新采集数据
8. POST /data/:name?flowId=:flowId

让采集点:name获取数据返回浏览器
9. GET /data/:name

##

