package com.alvin.niagara.sparkservice

import akka.http.scaladsl.server.Directives._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.io.StdIn

/**
 * Created by JINC4 on 6/4/2016.
 *
 * An Akka-Http server starts Spark service
 */
object SparkServiceBootstrap extends App with RouteService{

  implicit val actorSystem = ActorSystem("spark-services")
  implicit val materializer = ActorMaterializer()

  import actorSystem.dispatcher //ExecutionContext
  // start the server
  val bindingFuture = Http().bindAndHandle(sparkRoutes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => actorSystem.shutdown()) // and shutdown when done


}
