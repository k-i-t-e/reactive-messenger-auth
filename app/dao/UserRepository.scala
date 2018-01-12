package dao

import javax.inject.{Inject, Singleton}

import entities.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile

import scala.concurrent.Future

@Singleton
class UserRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit context: DatabaseExecutionContext) extends HasDatabaseConfigProvider[PostgresProfile] {
  import profile.api._

  private class UserTable(tag: Tag) extends Table[User](tag, "messenger_user") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def password = column[String]("password")

    override def * = (id.?, name, password.?) <> (User.tupled, User.unapply)
  }

  private val users = TableQuery[UserTable]

  def getUser(id: Long): Future[User] = db.run(users.filter(_.id === id).result.head).map(_.hidePassword)

  def getUser(userName: String, password: String): Future[Option[User]] = {
    val query = users.filter(u => u.name === userName && u.password === password).result.headOption
    db.run(query)
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

  def getUsers(): Future[Seq[User]] = db.run(users.result).map(u => u.map(_.hidePassword)) // just for testing
}
