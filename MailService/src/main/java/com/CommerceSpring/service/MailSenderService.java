package com.CommerceSpring.service;

import com.CommerceSpring.RabbitMQ.Model.*;
import com.CommerceSpring.utilty.JwtTokenManager;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender javaMailSender;
    private final JwtTokenManager jwtTokenManager;

    @RabbitListener(queues = "queueSendVerificationEmail")
    public void sendMailVerifyAccount(EmailVerificationModel model) throws MessagingException {
        String Token = jwtTokenManager.createToken(model.getAuthId()).get();
        String activationLink = "http://localhost:3000/dev/v1/auth/verify-account?token=" + Token;
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlContent = "<html><body>" +
                "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" style=\"font-family:Arial,sans-serif;color:#333;background-color:#f9f9f9;padding:20px;\">" +
                "    <tr>" +
                "        <td style=\"background-color:#ffffff;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);\">" +
                "            <table cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">" +
                "                <tr>" +
                "                    <td style=\"text-align:center;\">" +
                "                        <h1 style=\"color:#333333;font-size:24px;margin:0;\">Hoş Geldiniz, " + model.getFirstName() + "!</h1>" +
                "                        <p style=\"font-size:16px;color:#555555;\">Hesabınızı aktive etmek için aşağıdaki bağlantıya tıklayın:</p>" +
                "                        <a href=\"" + activationLink + "\" style=\"display:inline-block;padding:10px 20px;font-size:16px;color:#ffffff;background-color:#007bff;border-radius:5px;text-decoration:none;\">Aktifleştir</a>" +
                "                    </td>" +
                "                </tr>" +
                "                <tr>" +
                "                    <td style=\"text-align:center;padding-top:20px;\">" +
                "                        <p style=\"font-size:14px;color:#888888;margin:0;\"> Lütfen bu e-postayı yanıtlamayın.</p>" +
                "                    </td>" +
                "                </tr>" +
                "            </table>" +
                "        </td>" +
                "    </tr>" +
                "</table>" +
                "</body></html>";


        helper.setText(htmlContent, true);
        helper.setTo(model.getEmail());
        helper.setSubject("Hoş Geldiniz, " + model.getFirstName() + "!");

        javaMailSender.send(mimeMessage);
    }

    @RabbitListener(queues = "queueForgetPassword")
    public void sendPasswordResetEmail(String email) throws MessagingException {

        String token = jwtTokenManager.createPasswordResetToken(email).get();

        String resetLink = "http://localhost:3000/dev/v1/auth/reset-password?token=" + token;


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlContent = "<html>" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;\">" +
                "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"background-color: #f4f4f4; padding: 20px;\">" +
                "    <tr>" +
                "        <td align=\"center\">" +
                "            <table cellpadding=\"0\" cellspacing=\"0\" width=\"600px\" style=\"background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">" +
                "                <tr>" +
                "                    <td style=\"text-align:center;\">" +
                "                        <h2 style=\"font-size: 24px; color: #333333; margin: 0;\">Şifre Sıfırlama Talebi</h2>" +
                "                        <p style=\"font-size: 16px; color: #666666; margin: 20px 0;\">" +
                "                            Şifrenizi değiştirmek için aşağıdaki bağlantıya tıklamanız yeterlidir:" +
                "                        </p>" +
                "                        <a href=\"" + resetLink + "\" style=\"display: inline-block; padding: 12px 24px; font-size: 16px; color: #ffffff; background-color: #007bff; border-radius: 4px; text-decoration: none;\">Şifre Yenileme Bağlantısı</a>" +
                "                        <p style=\"font-size: 14px; color: #999999; margin-top: 20px;\">" +
                "                            Eğer bu talebi siz yapmadıysanız, bu e-postayı yok sayabilirsiniz." +
                "                        </p>" +
                "                        <p style=\"font-size: 14px; color: #999999; margin-top: 10px;\">" +
                "                            İyi günler dileriz.<br>" +
                "                        </p>" +
                "                    </td>" +
                "                </tr>" +
                "            </table>" +
                "        </td>" +
                "    </tr>" +
                "</table>" +
                "</body>" +
                "</html>";

        helper.setText(htmlContent, true);
        helper.setTo(email);
        helper.setSubject("Şifrenizi Yenileyin");


        javaMailSender.send(mimeMessage);
    }

    @RabbitListener(queues = "queueSendMail")
    public void sendMail(EmailSendModel email) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(email.getMessage());
        helper.setTo(email.getEmail());
        helper.setSubject(email.getSubject());
        javaMailSender.send(mimeMessage);
    }

    @RabbitListener(queues = "queueSendStyledEmail")
    public void sendStyledEmail(EmailModel email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(email.getMessage(), email.getIsHtml());
        helper.setTo(email.getEmail());
        helper.setSubject(email.getSubject());
        javaMailSender.send(mimeMessage);
    }

    @RabbitListener(queues = "queueSaveCustomerSendMail")
    public void sendEmailExternalSourceCustomers(CustomerSaveMailModel model) throws MessagingException {
        String emailJson = model.getEmail();
        String email = emailJson.replace("{\"email\":\"", "").replace("\"}", "");

        UUID authId = UUID.nameUUIDFromBytes(model.getMemberId().toString().getBytes());
        String token = jwtTokenManager.createToken(authId).get();

        String rateLink = "http://localhost:3000/customer-save-from-link?token=" + token + "&email=" + email;

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = "<html><body>" +
                    "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" style=\"font-family:Arial,sans-serif;color:#333;background-color:#f9f9f9;padding:20px;\">" +
                    "    <tr>" +
                    "        <td style=\"background-color:#ffffff;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);\">" +
                    "            <table cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">" +
                    "                <tr>" +
                    "                    <td style=\"text-align:center;\">" +
                    "                        <h1 style=\"color:#333333;font-size:24px;margin:0;\">Merhaba!</h1>" +
                    "                        <p style=\"font-size:16px;color:#555555;\">Alışveriş deneyiminizi değerlendirmek için lütfen aşağıdaki bağlantıya tıklayın:</p>" +
                    "                        <a href=\"" + rateLink + "\" style=\"display:inline-block;padding:10px 20px;font-size:16px;color:#ffffff;background-color:#007bff;border-radius:5px;text-decoration:none;\">Değerlendirme Formu</a>" +
                    "                    </td>" +
                    "                </tr>" +
                    "                <tr>" +
                    "                    <td style=\"text-align:center;padding-top:20px;\">" +
                    "                        <p style=\"font-size:14px;color:#888888;margin:0;\"> Lütfen bu e-postayı yanıtlamayın.</p>" +
                    "                    </td>" +
                    "                </tr>" +
                    "            </table>" +
                    "        </td>" +
                    "    </tr>" +
                    "</table>" +
                    "</body></html>";

            helper.setText(htmlContent, true);
            helper.setTo(email);
            helper.setSubject("Alışveriş Değerlendirme Daveti");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "queueCustomerSendEmailAboutOpportunity")
    public void sendEmailAboutOpportunity(CustomerSendEmailAboutOpportunity model) throws MessagingException {
        String emailJson = model.getEmail();
        String email = emailJson.replace("{\"email\":\"", "").replace("\"}", "");

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = "<html><body>" +
                    "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" style=\"font-family:Arial,sans-serif;color:#333;background-color:#f9f9f9;padding:20px;\">" +
                    "    <tr>" +
                    "        <td style=\"background-color:#ffffff;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);\">" +
                    "            <table cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">" +
                    "                <tr>" +
                    "                    <td style=\"text-align:center;\">" +
                    "                        <h1 style=\"color:#333333;font-size:24px;margin:0;\">Merhaba " + model.getFirstName() + " " + model.getLastName() + "!</h1>" +
                    "                        <p style=\"font-size:16px;color:#555555;\">Sizin için özel bir fırsatımız var:</p>" +
                    "                    </td>" +
                    "                </tr>" +
                    "                <tr>" +
                    "                    <td style=\"padding:10px 20px;background-color:#f1f1f1;border-radius:8px;\">" +
                    "                        <h2 style=\"color:#007bff;font-size:20px;margin:0;\">" + model.getTitle() + "</h2>" +
                    "                        <p style=\"font-size:16px;color:#333333;margin:5px 0;\">" + model.getDescription() + "</p>" +
                    "                        <p style=\"font-size:18px;color:#333333;margin:10px 0;\"><strong>Değer: </strong>" + model.getValue() + " TL</p>" +
                    "                    </td>" +
                    "                </tr>" +
                    "                <tr>" +
                    "                    <td style=\"text-align:center;padding-top:20px;\">" +
                    "                        <p style=\"font-size:14px;color:#888888;margin:0;\">Bu fırsat hakkında daha fazla bilgi almak için bizimle iletişime geçebilirsiniz.</p>" +
                    "                        <p style=\"font-size:14px;color:#888888;margin:0;\">Lütfen bu e-postayı yanıtlamayın.</p>" +
                    "                    </td>" +
                    "                </tr>" +
                    "            </table>" +
                    "        </td>" +
                    "    </tr>" +
                    "</table>" +
                    "</body></html>";

            helper.setText(htmlContent, true);
            helper.setTo(email);
            helper.setSubject("Özel Fırsatınız: " + model.getTitle());

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @RabbitListener(queues = "queueSendMailNewPassword")
    public void sendNewPasswordChangedByAdmin(SendMailNewPasswordModel sendMailNewPasswordModel) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");
        String loginLink = "http://localhost:3000/login";
        String textHTML = "<html><body>" +
                "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" style=\"font-family:Arial,sans-serif;color:#333;background-color:#f9f9f9;padding:20px;\">" +
                "    <tr>" +
                "        <td style=\"background-color:#ffffff;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);\">" +
                "            <table cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">" +
                "                <tr>" +
                "                    <td style=\"text-align:center;\">" +
                "                        <h1 style=\"color:#333333;font-size:24px;margin:0;\">Hoş Geldiniz, Yeni Şifreniz !</h1>" +
                "                        <p style=\"font-size:16px;color:#555555;\">" + sendMailNewPasswordModel.getNewPassword() + "</p>" +
                "                        <a href=\"" + loginLink + "\" style=\"display: inline-block; padding: 12px 24px; font-size: 16px; color: #ffffff; background-color: #007bff; border-radius: 4px; text-decoration: none;\">Giriş Yap</a>" +

                "                    </td>" +
                "                </tr>" +
                "                <tr>" +
                "                    <td style=\"text-align:center;padding-top:20px;\">" +
                "                        <p style=\"font-size:14px;color:#888888;margin:0;\"> Lütfen bu e-postayı yanıtlamayın.</p>" +
                "                    </td>" +
                "                </tr>" +
                "            </table>" +
                "        </td>" +
                "    </tr>" +
                "</table>" +
                "</body></html>";

        message.setSubject("Yeni Şifreniz");
        message.setText(textHTML, true);
        message.setBcc("aslihanmertjava@gmail.com");
        message.setCc("ertugrulsaliher@gmail.com");
        message.setTo(sendMailNewPasswordModel.getEmail());
        javaMailSender.send(mimeMessage);
    }

}
