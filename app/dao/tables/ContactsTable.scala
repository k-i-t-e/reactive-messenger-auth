package dao.tables

import java.sql.Timestamp
import slick.jdbc.PostgresProfile.api._

class ContactsTable(tag: Tag) extends Table[(Long, Long, Timestamp)](tag, "contact_list") {
  val users = TableQuery[UserTable]

  def userId = column[Long]("user_id")
  def contactId = column[Long]("contact_id")
  def createdDate = column[Timestamp]("created_date")

  def pk = primaryKey("pk_user_id_contact_id", (userId, contactId))
  def userIdFk = foreignKey("user_id", userId, users)(_.id)
  def contactIdFk = foreignKey("user_id", contactId, users)(_.id)

  override def * = (userId, contactId, createdDate)
}