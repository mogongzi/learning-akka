package me.ryan.learnakka.agent

import akka.actor.ActorSystem
import akka.agent.Agent

object ScalaAgentExample {
  val system = ActorSystem()
  implicit val ec = system.dispatcher

  def apply() = {
    val account = Agent(25)
    val amountToWithDraw = 20

    account.send { i =>
      if (amountToWithDraw < i) {
        i - 20
      } else i
    }

    Thread.sleep(1000)
    println(account.get)

    account.send { i =>
      if (amountToWithDraw < i) {
        i - 20
      } else i
    }

    Thread.sleep(1000)
    println(account.get)

    system.terminate()
  }

  def multipleTransaction() = {
    import scala.concurrent.stm._
    val wifeAccount = Agent(25)
    val husbandAccount = Agent(0)
    val wasSuccess1 = atomic { txn =>
      if (wifeAccount() >= 20) {
        wifeAccount.send(_ - 20)
        husbandAccount.send(_ + 20)
        true
      } else false
    }
    println("success?: " + wasSuccess1)

    val wasSuccess2 = atomic { txn =>
      if (wifeAccount() >= 20) {
        wifeAccount.send(_ - 20)
        husbandAccount.send(_ + 20)
        true
      } else false
    }
    println("success?: " + wasSuccess2)
  }
}
