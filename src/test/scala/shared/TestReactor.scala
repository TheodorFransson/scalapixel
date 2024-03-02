package shared

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.{Future, Promise}
import scala.reflect.ClassTag
import scala.swing.Reactor
import scala.swing.event.Event

class TestReactor extends Reactor:
	private val events: mutable.ListBuffer[Event] = mutable.ListBuffer.empty[Event]
	private val promises: mutable.Map[Class[_], Promise[Event]] = mutable.Map.empty

	reactions += {
		case event: Event =>
			events += event
			promises.get(event.getClass).foreach(_.trySuccess(event))
	}

	def future[T <: Event : ClassTag]: Future[T] =
		val eventClass = implicitly[ClassTag[T]].runtimeClass
		val promise = promises.getOrElseUpdate(eventClass, Promise[Event]()).asInstanceOf[Promise[T]]
		promise.future

	def allEventsOfType[T <: Event : ClassTag]: List[T] =
		val eventClass = implicitly[ClassTag[T]].runtimeClass
		events.collect { case e if e.getClass == eventClass => e.asInstanceOf[T] }.toList


	def receivedExpectedEvent[T <: Event : ClassTag]: Boolean =
		allEventsOfType[T].nonEmpty