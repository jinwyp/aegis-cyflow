
```scala
object MyGraphJar {

   // 决策点
   @Description("this is V1")
   def v1(state: State): Seq[Arrow]  = ???
   
   // 自动任务
   def auto_1(task: CommandAutoTask): Future[Map[String, String]] = ???
   
   // 任务路由get -- 产生视图模型, 可利用proxy获取所有所需Flow.State, User.State, Group.State, 以及自定义的来自其他任务地方的数据, 来组织任务视图模型
   def getTask_1(proxy: ActorRef): Route = ??? 
   
   // 任务路由post -- 负责将用户提交转化为数据点, 并调用user模块, 提交任务
   def postTask_1(proxy: ActorRef): Route = ???
   
   // 其他非流程的视图
   def getOther_1(proxy: ActorRef): Route = ???
   
}
```