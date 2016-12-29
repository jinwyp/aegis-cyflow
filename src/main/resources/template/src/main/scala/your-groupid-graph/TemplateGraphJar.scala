



object TemplateGraphJar {


  // 决策点
  @Description("this is V1")
  def <V1>(state: State): Seq[Arrow]  = ???

  // 自动任务
  def <A>(task: CommandAutoTask): Future[Map[String, String]] = ???

  // 任务路由get -- 产生视图模型, 可利用proxy获取所有所需Flow.State, User.State, Group.State, 以及自定义的来自其他任务地方的数据, 来组织任务视图模型
  def get<UA>(proxy: ActorRef): Route = ???

  // 任务路由post -- 负责将用户提交转化为数据点, 并调用user模块, 提交任务, (将来会在这里注入 组织模块 org: Organization)
  def post<UA>(proxy: ActorRef): Route = ???

}
