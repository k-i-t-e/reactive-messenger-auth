package controllers

import javax.inject.{Inject, Singleton}

import dao.MockUserRepository
import entities.{RestResult, User}
import play.api.mvc._
import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(cc: ControllerComponents, userRepository: MockUserRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "userName").write[String] and
        (JsPath \ "password").writeNullable[String]
  )(unlift(User.unapply))

  implicit val userReads: Reads[User]= (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "userName").read[String] and
      (JsPath \ "password").readNullable[String]
  )(User.apply _)

  implicit val resultBooleanWrites = Json.writes[RestResult[Boolean]]

  def getContactsList(userId: Long) = Action.async {
    _ => {
      userRepository.loadUsers().map(u => Ok(Json.toJson(u)))
    }
  }

  def validateUser = parse.json.validate(v => v.validate.asEither.left.map(e => BadRequest(JsError.toJson(e))))

  def createUser = Action(validateUser) {
    request => {
      val user = request.body
      Ok(Json.toJson(RestResult[Boolean](true)))
    }
  }
}

