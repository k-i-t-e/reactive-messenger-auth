package dao

import javax.inject.{Inject, Singleton}

import entities.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile

@Singleton
class UserRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[PostgresProfile] {
  import profile.api._

  private class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def userName = column[String]("NAME")
    def password = column[String]("PASSWORD")

    override def * = (id.?, userName, password.?) <> (User.tupled, User.unapply)
  }
}
