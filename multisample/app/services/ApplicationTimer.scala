package services

import java.text.SimpleDateFormat
import java.time.{Clock, Instant}
import java.util.Locale
import javax.inject._
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, ISODateTimeFormat}
import play.api.Logger
import play.api.inject.ApplicationLifecycle
import play.api.libs.json.{Format, Json, Reads, Writes}
import scala.concurrent.Future

/**
 * This class demonstrates how to run code when the
 * application starts and stops. It starts a timer when the
 * application starts. When the application stops it prints out how
 * long the application was running for.
 *
 * This class is registered for Guice dependency injection in the
 * [[Module]] class. We want the class to start when the application
 * starts, so it is registered as an "eager singleton". See the code
 * in the [[Module]] class to see how this happens.
 *
 * This class needs to run code when the server stops. It uses the
 * application's [[ApplicationLifecycle]] to register a stop hook.
 */
@Singleton
class ApplicationTimer @Inject() (clock: Clock, appLifecycle: ApplicationLifecycle) {

  private val logger = org.slf4j.LoggerFactory.getLogger(classOf[ApplicationTimer])

  // This code is called when the application starts.
  private val start: Instant = clock.instant
  logger.info(s"ApplicationTimer demo: Starting application at $start.")

  // When the application starts, register a stop hook with the
  // ApplicationLifecycle object. The code inside the stop hook will
  // be run when the application stops.
  appLifecycle.addStopHook { () =>
    val stop: Instant = clock.instant
    val runningTime: Long = stop.getEpochSecond - start.getEpochSecond
    logger.info(s"ApplicationTimer demo: Stopping application at ${clock.instant} after ${runningTime}s.")
    Future.successful(())
  }


  implicit val jodaDateReads = Reads.jodaDateReads("yyyyMMdd'T'HHmmss.SSS'Z'")
  implicit val jodaDateWrites = Writes.jodaDateWrites("yyyyMMdd'T'HHmmss.SSS'Z'")
  implicit val dateFormat: Format[DateTime] = Format(jodaDateReads, jodaDateWrites)
  implicit val dateTimeFormat: Format[(String, DateTime)] = Json.format[(String, DateTime)]

  val dtr = DateTimeFormat.from
  val formatter= new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSz", Locale.ENGLISH);

  val s = "20200406T145511.067Z"
  val dtr = DateTime.parse(s, ISODateTimeFormat.dateTimeParser());

  def dateTimeCall(id: String): Future[(String, DateTime)] = {

    val result: Future[(String, DateTime)] = getDateTime(id)
    result

    JodaTime.now

  }

}
