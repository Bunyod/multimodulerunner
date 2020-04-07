package controllers

import javax.inject._
import akka.actor.ActorSystem
import java.util.UUID
import play.api.mvc._
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future, Promise}

/**
  * This controller creates an `Action` that demonstrates how to write
  * simple asynchronous code in a controller. It uses a timer to
  * asynchronously delay sending a response for 1 second.
  *
  * @param cc standard controller components
  * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
  * run code after a delay.
  * @param exec We need an `ExecutionContext` to execute our
  * asynchronous code.  When rendering content, you should use Play's
  * default execution context, which is dependency injected.  If you are
  * using blocking operations, such as database or network access, then you should
  * use a different custom execution context that has a thread pool configured for
  * a blocking API.
  */
@Singleton
class AsyncController @Inject()(
  cc: ControllerComponents,
  actorSystem: ActorSystem
)(implicit exec: ExecutionContext)
    extends AbstractController(cc) {

  /**
    * Creates an Action that returns a plain text message after a delay
    * of 1 second.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/message`.
    */
  def message = Action.async {
    getFutureMessage(1.second).map { msg =>
      Ok(msg)
    }
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Hi!")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  case class UserToken(userKayes: UserTokenKey)
  case class User(value: String)
  case class UserTokenKey(value: UUID)

  val result = for {
    tokenOption: Option[UserToken] <-Future(Some(UserToken(UserTokenKey(UUID.randomUUID()))))
    userOption: Option[User] <- if (tokenOption.isDefined) {
      Future(Some(User("something")))
    } else { Future.successful(None) }
    modifiedUser: Option[User] <- if (userOption.isDefined) {
      Future(Some(User("anotheruser")))
    } else Future.successful(None)
    deletedToken: Option[UserTokenKey] <- if (modifiedUser.isDefined) {
      Future(Some(UserTokenKey(UUID.fromString("adsatoken"))))
    } else Future.successful(None)
  } yield deletedToken

  println(Await.result(result, 2.seconds))
}
