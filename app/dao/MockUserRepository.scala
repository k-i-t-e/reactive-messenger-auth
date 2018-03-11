package dao

import javax.inject.{Inject, Singleton}

import entities.User

import scala.concurrent.Future

@Singleton
class MockUserRepository @Inject() {
  def loadUsers(): Future[Seq[User]] = {
    Future.successful(List(User(Some(1), "Foo")))
  }
}
