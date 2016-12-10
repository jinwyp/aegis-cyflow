package com.yimei.cflow

/**
  * Created by hary on 16/12/10.
  */
object PkgTest extends App {


  // lens test
  def lensTest() = {
    import com.softwaremill.quicklens._

    case class State(tasks: Map[String, Map[String, Map[String, Int]]])
    var state = State(Map())


    // 入队
    def enqueue(userType: String, group: String, taskId: String, task: Int): State = {
      if (state.tasks.contains(userType)) {
        if (state.tasks(userType).contains(group)) {
          state.modify(_.tasks.at(userType).at(group)).setTo(
            state.tasks(userType)(group) + (taskId -> task)
          )
        } else {
          state.modify(_.tasks.at(userType)).setTo(Map(group -> Map(taskId -> task)))
        }
      } else {
        state.copy(tasks = state.tasks + (userType -> Map(group -> Map(taskId -> task))))
      }
    }

    state = enqueue("u1", "g1", "t1", 2)
    state = enqueue("u1", "g1", "t2", 3)
    state = enqueue("u1", "g1", "t3", 4)
    state = enqueue("u1", "g2", "t1", 4)
    state = enqueue("u2", "g2", "t1", 4)

    println(s"$state")
  }

  // neo4j test
  def neo4j() = {



  }

  lensTest()
  neo4j()

}

object Neo4jTest {
//  import sys.ShutdownHookThread
//  import org.neo4j.scala.{TypedTraverser, SingletonEmbeddedGraphDatabaseServiceProvider, Neo4jWrapper}
//  trait MatrixBase {
//    val name: String
//    val profession: String
//  }
//  case class Matrix(name: String, profession: String) extends MatrixBase
//  case class NonMatrix(name: String, profession: String) extends MatrixBase

}
