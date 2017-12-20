package controllers

import javax.inject.{Inject, Singleton}

import entities.User
import play.api.mvc._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Writes}

@Singleton
class UserController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "userName").write[String]
  )(unlift(User.unapply))

  def getContactsList(userId: Long) = Action.apply{
    _ => {
      Ok(Json.toJson(List(User(1, "Foo"))))
    }
  }

  /*def createUser(user: User) = Action {
    _ => {

    }
  }*/
}

