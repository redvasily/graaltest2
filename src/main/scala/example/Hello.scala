package example

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.LazyLogging
import sttp.client3._

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

case class Record(id: Int, key: String, value: Option[String])

class Hello extends LazyLogging {
  def run(): Unit = {
    println("Hello")
    logger.info("Hello")

    val contents = io.Source.fromResource("foo.txt").mkString("")
    logger.info(s"Contents:\n$contents")

    val request = basicRequest.get(uri"https://example.com/")

    val backend = HttpURLConnectionBackend()
    val response = request.send(backend)

    logger.info(response.body.toString)

    implicit val actorSystem = ActorSystem()

    val done = Source(1 to 10)
      .runWith(Sink.foreach(x => logger.info(s"Processing: ${x}")))

    Await.result(done, 10.seconds)
    actorSystem.terminate()

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    val record = Record(1, "key", Some("value"))
    val serialized = mapper.writeValueAsString(record)
    logger.info(s"Serialized: $record -> $serialized")

    val deserialized = mapper.readValue(serialized, classOf[Record])
    logger.info(s"Deserialized: $deserialized")
  }
}

object Hello extends App {
  new Hello().run()
}
