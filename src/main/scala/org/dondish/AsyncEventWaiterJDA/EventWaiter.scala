package org.dondish.AsyncEventWaiterJDA

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit, TimeoutException}

import net.dv8tion.jda.core.events.{Event, ShutdownEvent}
import net.dv8tion.jda.core.hooks.EventListener

import scala.collection.mutable
import scala.concurrent.{Future, Promise}
import scala.util.control.Breaks._


/**
  * The Event Waiter
  * @param ec used to schedule cancellation of promises of events when timed out. we need only one thread.
  */
class EventWaiter(val ec: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()) extends EventListener {

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

    if (event.getClass == classOf[ShutdownEvent]) {
      ec.shutdown()
    }
  }

  /**
    * Waits for an event
    * @param classType the event to wait for
    * @param condition the condition to run as
    * @tparam T the event to wait for
    * @return the future
    */
  def waitFor[T <: Event](classType: Class[T], condition: Function[T, Boolean]): Future[T] = {
    val ev = events.getOrElseUpdate(classType, mutable.Set[Entry[_ <: Event]]())

    val p = Promise[T]()

    val entry = new Entry[T](condition, p)

    ev.add(entry)

    p.future
  }

  /**
    * Wait for an event but with timeouts
    * @param classType the class type
    * @param condition the condition to execute by
    * @param timeout the timeout length
    * @param unit the time unit of the timeout
    * @tparam T the type of the event
    * @return the future of the event
    */
  def waitFor[T <: Event](classType: Class[T], condition: Function[T, Boolean], timeout: Long, unit: TimeUnit): Future[T] = {
    val ev = events.getOrElseUpdate(classType, mutable.Set[Entry[_ <: Event]]())

    val p = Promise[T]()

    val entry = new Entry[T](condition, p)

    ev.add(entry)

    ec.schedule(()=>{
      entry.p.failure(new TimeoutException())
      events(classType).remove(entry)
    }, timeout, unit)

    p.future
  }

  /**
    * The entry used to save events
    * @param condition the predicate
    * @param p the promise to send results to
    * @tparam T the Event type
    */
  private class Entry[T <: Event] (val condition: Function[T, Boolean], val p: Promise[T]) {}
}
