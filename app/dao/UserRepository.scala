package dao

import java.sql.Timestamp
import java.util.Date
import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import dao.tables.{ContactsTable, UserTable}
import entities.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends DelegableAuthInfoDAO[PasswordInfo] with HasDatabaseConfigProvider[PostgresProfile] {
  import profile.api._

  private val users = TableQuery[UserTable]
  private val contacts = TableQuery[ContactsTable]

  def getUser(id: Long): Future[Option[User]] = db.run(users.filter(_.id === id).result.headOption)
    .map(u => u.map(_.hidePassword))

  def findUser(user: User): Future[Option[User]] = db.run {
    users
      .filter(row => row.name === user.userName && row.password === user.password)
      .result
      .headOption
  }

  def findUserByName(userName: String): Future[Option[User]] = db.run {
    users.filter(r => r.name === userName).result.headOption
  }

  def createUser(user: User): Future[User] = {
    val action = (
      for {
        newId <- users.returning(users.map(_.id)) += user
        newUser <- users.filter(_.id === newId).result.head
      } yield newUser
    ).transactionally

    db.run(action).map(_.hidePassword)
  }

  def getContacts(userId: Long): Future[Seq[User]] = {
    val join = for {
      user <- users if user.id === userId
      contact <- contacts if contact.userId === user.id
      contactUser <- users if contactUser.id === contact.contactId
    } yield contactUser
    db.run(join.result).map(u => u.map(_.hidePassword))
  }

  def createContact(userId: Long, contactId: Long, createdDate: Date): Future[_] = {
    db.run {
      (
        contacts += (userId, contactId, new Timestamp(createdDate.getTime))
      ).transactionally
    }
  }

  def deleteContact(userId: Long, contactId: Long): Future[_] = {
    db.run(contacts.filter(c => c.userId === userId && c.contactId === contactId).delete.transactionally)
  }

  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    findUserByName(loginInfo.providerKey).map({
      case Some(u) => Some(PasswordInfo("bcrypt", u.password.get, None))
      case None => None
    })
  }

  override def add(loginInfo: LoginInfo,
                   authInfo: PasswordInfo): Future[PasswordInfo] = ???

  override def update(loginInfo: LoginInfo,
                      authInfo: PasswordInfo): Future[PasswordInfo] = ???

  override def save(loginInfo: LoginInfo,
                    authInfo: PasswordInfo): Future[PasswordInfo] = {
    val u = User(None, loginInfo.providerKey, Some(authInfo.password))
    val q = for {
      oldUser <- users if oldUser.name === u.userName
    } yield oldUser.password

    db.run(q.update(authInfo.password).transactionally).map(i => authInfo)
  }

  override def remove(loginInfo: LoginInfo): Future[Unit] = ???
}
