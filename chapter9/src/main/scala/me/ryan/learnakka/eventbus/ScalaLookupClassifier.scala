package me.ryan.learnakka.eventbus

import akka.actor.ActorRef
import akka.event.{EventBus, LookupClassification}

class ScalaLookupClassifier extends EventBus with LookupClassification {
  type Event = EventBusMessage
  type Classifier = String
  type Subscriber = ActorRef

  override protected def classify(event: EventBusMessage): String = event.getTopic

  override protected def publish(event: EventBusMessage, subscriber: ActorRef): Unit = {
    subscriber ! event.getMsg
  }

  override protected def compareSubscribers(a: ActorRef, b: ActorRef): Int = a.compareTo(b)

  override protected def mapSize(): Int = 128
}