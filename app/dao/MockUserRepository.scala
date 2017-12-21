package dao

import javax.inject.{Inject, Singleton}

import entities.User

import scala.concurrent.Future

@Singleton
class MockUserRepository @Inject()(implicit context: DatabaseExecutionContext) {
  def loadUsers(): Future[Seq[User]] = {
    Future {
      List(User(Some(1), "Foo"))
    }
  }
}
