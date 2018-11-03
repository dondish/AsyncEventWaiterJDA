package org.dondish.AsyncEventWaiterJDA

import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.hooks.EventListener

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

import util.control.Breaks._


/**
  * The Event Waiter
  */
class EventWaiter extends EventListener {

  /**
    * A <mutable.HashMap> from the classes to the entries
    */
  private val events = new mutable.HashMap[Class[_], mutable.Set[Entry[_ <: Event]]]

  /**
    * Event listening
    * @param event the event
    */
  override def onEvent(event: Event): Unit = {
    val ev = events.get(event.getClass)

    if (ev.isDefined) { // whether we have events to wait for
      for (entry <- ev.get) {
        breakable { // being able to continue the loop if entry already exists
          entry match {
            case a: Entry[event.type] => // casting to Entry[T]
              if (a.p.isCompleted) { // if the promise was already completed (timedout)
                ev.get.remove(entry)
                break
              }

              if (a.condition(event)) { // if condition is met
                a.p.success(event) // return the value
                ev.get.remove(entry) // remove the entry
              }
          }
        }

      }
    }
  }

  def waitFor[T <: Event](classType: Class[T], condition: Function[T, Boolean]): Future[T] = {
    val ev = events.getOrElseUpdate(classType, mutable.Set[Entry[_ <: Event]]())

    val p = Promise[T]()

    val entry = new Entry[T](condition, p)

    ev.add(entry)

    p.future
  }

  private class Entry[T <: Event] (val condition: Function[T, Boolean], val p: Promise[T]) {}
}
