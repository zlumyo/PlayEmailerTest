package controllers

import javax.inject.Inject

import play.api._
import play.api.http.HttpEntity
import play.api.mvc._
import utils.MailerSender

/**
  * External API for email notifications.
  */
class MailApi @Inject() (mailSender: MailerSender) extends Controller {

    def send(email: Option[String], name: Option[String], emailType: Option[String]) = Action {

      var result = checkAvailability(email)
      result = checkAvailability(name)
      result = checkAvailability(emailType)

      if (result.header.status == Ok.header.status) {
        val body = emailType.get match {
          case "PasswordReminder"  => Some(views.html.Mails.PasswordReminder(email.get, name.get).toString)
          case "TraderAccountRegistered"  => Some(views.html.Mails.TraderAccountRegistered(email.get, name.get).toString)
          case "UserRegistered"  => Some(views.html.Mails.UserRegistered(email.get, name.get).toString)
          case _ => None
        }
        result = body match {
          case Some(content) => if (mailSender.sendEmail(content, email.get, name.get)) GoodResult else BadResult
          case None => BadResult
        }
      }

      result
    }

    private def checkAvailability[T](param: Option[T]): Result = {
      param match {
        case Some(_) => GoodResult
        case None => BadResult
      }
    }

  private val BadResult = Result(
    header = ResponseHeader(400, Map.empty),
    body = HttpEntity.NoEntity
  )

  private val GoodResult = Ok("")
}
