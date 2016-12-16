translator: 

就是将用户状态, 组状态 + 流程状态转换为 视图模型

(Option[User.State], Option[Group.State], Option[Flow.State]) => T

提升后成为
(
Future[Option[User.State]], 
Future[Option[Group.State]], 
Future[Option[Flow.State]]
) => Future[T]     T是我们的view models, 位于views包下


