package com.kakao.nills.akka.stream

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing}
import akka.util.ByteString

import scala.concurrent.Future

object HttpClientTest {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val dispatcher = system.dispatcher
    implicit val materializer = ActorMaterializer()

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "https://akka.io"))

    responseFuture.map{
      response =>
        println(response)
        response.entity.dataBytes.via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 256)).map(s => s ++ ByteString("\n"))
      .runWith(FileIO.toPath(new File("./ninnede.out").toPath))
    }
  }
}
