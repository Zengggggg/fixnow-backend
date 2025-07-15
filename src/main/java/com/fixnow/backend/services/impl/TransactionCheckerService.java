package com.fixnow.backend.services.impl;

import com.fixnow.backend.models.User;
import com.fixnow.backend.services.UserService;
import com.fixnow.backend.services.WalletService;
import jakarta.mail.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionCheckerService {

    private final UserService userService;
    private final WalletService walletService;

    @Value("${email.username}")
    private String emailUsername;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${email.host}")
    private String emailHost;

    @Value("${email.port}")
    private String emailPort;

    // Mỗi 5 phút kiểm tra 1 lần
    @Scheduled(fixedRate = 30000)
    public void checkEmails() {
        log.info("🔄 Đang kiểm tra email giao dịch...");

        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore();
            store.connect(emailHost, emailUsername, emailPassword);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // Chỉ đọc 10 email mới nhất để tránh xử lý lặp
            Message[] messages = inbox.getMessages(Math.max(1, inbox.getMessageCount() - 10), inbox.getMessageCount());

            for (Message msg : messages) {
                String content = getTextFromMessage(msg);
                if (content.contains("fixnow")) {
                    String username = extractUsername(content);
                    double amount = extractAmount(content);

                    if (username != null && amount > 0) {
                        User user = userService.findByEmail(username);
                        walletService.topUp(user, amount);

                        log.info("✅ Giao dịch xác thực thành công: +{} VNĐ cho người dùng {}", amount, username);
                        msg.setFlag(Flags.Flag.SEEN, true); // Đánh dấu đã xử lý
                    }
                }
            }

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            log.error("❌ Lỗi khi kiểm tra email giao dịch: {}", e.getMessage(), e);
        }
    }

    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            StringBuilder result = new StringBuilder();
            Multipart multipart = (Multipart) message.getContent();

            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (part.isMimeType("text/plain")) {
                    result.append(part.getContent());
                }
            }
            return result.toString();
        }
        return "";
    }

    // 🧠 Trích username từ: fixnow-giang
    private String extractUsername(String content) {
        Pattern pattern = Pattern.compile("fixnow([a-zA-Z0-9_]+)");
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(1) : null;
    }

    // 🧠 Trích số tiền từ nội dung như: +20,000đ hoặc 20000 VND
    private double extractAmount(String content) {
        Pattern pattern = Pattern.compile("(\\+)?([0-9]{1,3}(?:[,\\.][0-9]{3})*)(\\s?đ|\\s?VND)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String rawAmount = matcher.group(2).replace(",", "").replace(".", "");
            try {
                return Double.parseDouble(rawAmount);
            } catch (NumberFormatException e) {
                log.warn("⚠️ Không parse được số tiền từ: {}", rawAmount);
            }
        }
        return 0;
    }

}
