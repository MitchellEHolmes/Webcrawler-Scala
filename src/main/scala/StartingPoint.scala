package scala.crawler

import akka.actor.{ActorSystem, PoisonPill, Props}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object StartingPoint extends App {
  val system = ActorSystem()
  val supervisor = system.actorOf(Props(new Supervisor(system)))

  // send (!)
  supervisor !
    Start("https://play.google.com/store/apps/details?id=com.facebook.orca")

  Await.result(system.whenTerminated, 10 minutes)

  // send (!)
  supervisor ! PoisonPill
  system.terminate
}