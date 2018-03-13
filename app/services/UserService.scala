package services

import java.util.Date

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import dao.UserRepository
import entities.User
import exceptions.MessengerException
import javax.inject.{Inject, Singleton}
import security.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserService @Inject()(userRepository: UserRepository)(implicit ec: ExecutionContext) {
  def getContactList()(implicit request: SecuredRequest[DefaultEnv, _]): Future[Seq[User]] =
    userRepository.getContacts(request.identity.userId)

  def addContact(contactId: Long)(implicit request: SecuredRequest[DefaultEnv, _]): Future[_] =
    userRepository.getUser(contactId).map({
      case Some(_) => userRepository.createContact(request.identity.userId, contactId, new Date())
      case None => throw new MessengerException(s"User with ID = $contactId was not found")
    })

  def deleteContact(contactId: Long)(implicit request: SecuredRequest[DefaultEnv, _]): Future[_] =
    userRepository.deleteContact(request.identity.userId, contactId)
}
