package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.util.Random
import play.api.libs.json._
import models.Eikaiwa
import com.fasterxml.jackson.annotation.JsonValue

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController {

  /** Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def hello(name: String) = Action { implicit request =>
    Ok(views.html.hello(name))
  }

  def randNorm(loc: Double, scale: Double, size: Int) = Action {
    implicit request =>
      val randList: List[Double] =
        List.fill(size)(loc + scale * Random.nextGaussian())
      Ok(Json.obj("randomNormVariates" -> randList))
  }

  def oneInstructor(
      avgWeekday: Double,
      stdvWeekday: Double,
      avgSat: Double,
      stdvSat: Double,
      avgSun: Double,
      stdvSun: Double,
      weekDays: Int,
      satDays: Int,
      sunDays: Int
  ) = Action { implicit request =>
    val sched = Eikaiwa.simInstructor(
      avgWeekday,
      stdvWeekday,
      avgSat,
      stdvSat,
      avgSun,
      stdvSun,
      weekDays,
      satDays,
      sunDays
    )
    Ok(Json.obj("singleInstructorSchedule" -> sched))
  }

  def oneMonthStudio() = Action(parse.json) { request: Request[JsValue] =>
    val result = Eikaiwa.oneMonthStudio(request.body)
    Ok(Json.obj("result" -> result))
  }
}
