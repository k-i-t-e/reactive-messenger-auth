package dao.tables

import entities.User

import slick.jdbc.PostgresProfile.api._

class UserTable(tag: Tag) extends Table[User](tag, "messenger_user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def password = column[String]("password")

  override def * = (id.?, name, password.?) <> (User.tupled, User.unapply)
}
