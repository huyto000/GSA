package com.example.GemSkillAssessment.service.email;

import com.example.GemSkillAssessment.config.ApplicationConstant;
import com.example.GemSkillAssessment.dao.UserRepository;
import com.example.GemSkillAssessment.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.UUID;

@Service
public class SendEmailService {
    private static final Logger logger = LoggerFactory.getLogger(SendEmailService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    @Qualifier("threadPoolExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private JavaMailSender mailSender;

    public void remindUser(User user) {
        String subject = "Invite User";
        String content = this.createdRemindUser(user);
        sendEmail(content, user, subject);
    }

    public void remindReviewUser(User user, User userReview) {
        String subject = "Invite User";
        String content = this.createdRemindReview(user, userReview);
        sendEmail(content, user, subject);
    }

    @Async("threadPoolExecutor")
    public void sendEmailAll(List<String> ids) {
        List<User> userList = userRepository.findAllByIdEmployeeIn(ids);
        String subject = "Invite User";
        for (User user : userList) {
            String token = UUID.randomUUID().toString();
            user.setTokenVerify(token);
            user.setInvited(true);
            userRepository.save(user);
            String content = this.createdInvitation(user);
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String subject = "Invite User";
                    sendEmail(content, user, subject);
                }
            });
        }
    }

    private void sendEmail(String content, User user, String subject) {
        try {
            MimeMessage messageEmail = mailSender.createMimeMessage();
            boolean multipart = true;
            MimeMessageHelper helper = new MimeMessageHelper(messageEmail, multipart, "utf-8");
            messageEmail.setContent(content, "text/html; charset=UTF-8");
            helper.setFrom("testw1605@gmail.com");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            mailSender.send(messageEmail);
        } catch (MessagingException e) {
            logger.error("Unable to email invitations to " + user.getEmail());
            e.printStackTrace();

        }
    }

    private String createdInvitation(User user) {
        String invitation = "\"<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\" />\n" +
                "    <title>Kích hoạt tài khoản</title>    \n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Kính gửi:" + user.getName() + "</p>\n" +
                "\n" +
                "\t<p>Anh/Chị đã được kích hoạt tài khoản truy cập tại địa chỉ: <a href=\"172.16.10.217\">172.16.10.217</a></p>\n" +
                "\n" +
                "\t<p>Anh/Chị vui lòng nhấn vào liên kết sau để cập nhật mật khẩu:" + ApplicationConstant.urlFrontEnd + "/SetPassword?token=" + user.getTokenVerify() + "&email=" + user.getEmail() + "</p>\n" +
                "\n" +
                "\t<p>(Trường hợp không thể mở được liên kết, Anh/Chị vui lòng chép đường dẫn trên vào thanh địa chỉ của trình duyệt rồi nhấn phím Enter.)</p>\n" +
                "\n" +
                "\t<p>Lưu ý: trường hợp Anh/Chị không phải là người yêu cầu kích hoạt tài khoản, vui lòng bỏ qua email này.</p>\n" +
                "\t\n" +
                "\t<p>Email này sẽ có hiệu lực trong vòng ? ngày.</p>\n" +
                "\n" +
                "\t<p>Trân trọng!</p>\n" +
                "</body>\n" +
                "</html>\n";
        return invitation;
    }

    private String createdRemindUser(User user) {
        String remind = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\" />\n" +
                "    <title>[GEM Skill Assessment]</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Kính gửi: Anh/Chị</p>\n" +
                "\n" +
                "\t<p>Tính đến #getDate#</p>\n" +
                "\t<p>Anh/Chị :" + user.getName() + "còn thiếu đánh giá:</p>" + ApplicationConstant.urlFrontEnd + "login" + "\n" +
                "\t<p>Anh/chị vui lòng hoàn thành báo cáo dữ liệu </p>\n" +
                "\t<p>Trân trọng!</p>\n" +
                "</body>\n" +
                "</html>\n";
        return remind;
    }

    private String createdRemindReview(User user, User userReview) {
        String remind = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\" />\n" +
                "    <title>[GEM Skill Assessment]</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Kính gửi: Anh/Chị</p>\n" +
                "\n" +
                "\t<p>Tính đến #getDate#</p>\n" +
                "\t<p>Anh/Chị :" + userReview.getName() + "còn thiếu đánh giá:</p>" + ApplicationConstant.urlFrontEnd + "login" + "\n" +
                "\t<p>Anh/Chị phụ trách đánh giá nhân viên" + user.getName() + " vui lòng hoàn thành báo cáo dữ liệu </p>\n" +
                "\t<p>Trân trọng!</p>\n" +
                "</body>\n" +
                "</html>\n";
        return remind;
    }
}
