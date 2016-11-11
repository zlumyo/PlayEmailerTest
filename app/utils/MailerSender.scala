package utils

import play.api.libs.mailer._
import play.api.Configuration
import javax.inject.Inject

/**
  * Utility for sending emails.
  */
class MailerSender @Inject() (mailerClient: MailerClient, configuration: Configuration) {

  /**
    * Sends email.
    * @param body Body of email.
    * @param address Address which email sends to.
    * @return True if email sent, False otherwise.
    */
  def sendEmail(body: String, address: String, name: String): Boolean = {
    val mailConfig = configuration.getConfig("play.mailer").get
    val email = Email(
      "Simple email",
      s"%companyname% <${mailConfig.getString("user").get}>",
      Seq(s"$name <$address>"),
      bodyHtml = Some(body)
    )

    try {
      mailerClient.send(email)
      true
    } catch {
      case ex: Exception =>
        false
    }
  }
}
