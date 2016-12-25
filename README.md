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
20. subflow support                          todo   doing

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

def createUser(classType: string) = {
   // 1> 从IdGenerator 取得 classType的next Id
   // 2> 返回classType$id
}

def createGroup(classType: string, group: string, group: string) = {
   // 1> 从IdGenerator 取得 classType的next Id
   // 2> 返回classType$id
}

create user  应该返回userId
create group 应该返回ggid

##

1.创建class(class_name：pname，description：hahahaha)
curl -XPOST  -H "Content-Type: application/json" http://localhost:9000/api/party/pname/hahahaha

2.创建公司（inst）
curl -XPOST  -H "Content-Type: application/json" http://localhost:9000/api/inst/pname/1/yimei -d '{"id": 3,"ts_c":"2016-12-24 22:06:03","party_name":"yimei","party_class":"pname","instance_id":"1"}'

3.创建3个用户，Id分别为husbandId，wifeId，friendId，
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/user/pname/1/husbandId -d '{"password":"123456","phone":"1310000001","email":"wangqi@123.com","name":"wang"}'
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/user/pname/1/wifeId -d '{"password":"123456","phone":"1310000001","email":"wangqi@123.com","name":"wang"}'
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/user/pname/1/friendId -d '{"password":"123456","phone":"1310000001","email":"wangqi@123.com","name":"wang"}'

4.创建流程(以husband的身份创建的流程)
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/flow/user/pname/1/husbandId?flowType=money -d '{"Wife":"pname-1!wifeId"}'
注意:这里初始值为妻子的Id，第一个任务为自动任务，有可能失败（算概率的）,失败以后就自动结束。


-----一上就完成了创建流程的步骤，下面打开
http://localhost:9000/mng/graph.html?id=money!rz-1!husbandId!4
（id为流程Id）就可以观察流程执行的情况。

打开：http://localhost:9000/mng/flow/index.html 在类别，公司Id，用户Id上输入相关人员的信息就可以查询该用户任务。
查询完成后，点击右侧操作按钮，就可以进入做任务的界面。

（其中有3个任务比较特殊）
1.AssignFriend 要填写friendId --- 在上述步骤下为：pname-1!friendId
2.UploadReceipt 这里value为任意pdf资源地址 ，memo为pdf
3.WifeApprove 这里只有wife填写为yes时流程才能顺利结束，否则就是循环最后两个节点。

