/*
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.compute.function;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Properties;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 发送邮件函数
 *
 * @author dingqianwen
 * @date 2020/8/18
 * @since 1.0.0
 */
@Slf4j
@Function
public class SendEmailFunction {

    /**
     * 发送邮件，失败1次后3秒后重新发送
     * 平均发邮件耗时一秒，建议有异步发送参数
     *
     * @param sendEmailInfo 发送邮件配置信息以及消息内容
     * @return true发送成功
     */
    @Executor(maxAttempts = 1, delay = 1000 * 3)
    public boolean executor(@Valid SendEmailInfo sendEmailInfo) throws MessagingException {
        log.info("开始发送邮件,发送人:{},接收人:{},邮件标题:{},邮件内容:{}", sendEmailInfo.getUser(), sendEmailInfo.getTos(), sendEmailInfo.getTitle(), sendEmailInfo.getText());
        Properties props = new Properties();
        // 设置发送邮件的邮件服务器的属性
        props.put("mail.smtp.host", sendEmailInfo.getMailSmtpHost());
        props.setProperty("mail.smtp.port", sendEmailInfo.getMailSmtpHost());
        // 阿里linux服务器端口号问题....com.sun.mail.util.MailConnectException: Couldn't connect to host, port: smtp
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.socketFactory.port", sendEmailInfo.getMailSmtpHost());
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");
        // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);
        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            message.setFrom(new InternetAddress(sendEmailInfo.getUser()));
            // 加载收件人地址
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", sendEmailInfo.getTos())));
            // 抄送
            if (CollUtil.isNotEmpty(sendEmailInfo.getCc())) {
                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(String.join(",", sendEmailInfo.getCc())));
            }
            // 加载标题
            message.setSubject(sendEmailInfo.getTitle());
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendEmailInfo.getText(), "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            message.saveChanges(); // 保存变化
            // 连接服务器的邮箱
            Transport transport = session.getTransport("smtp");
            // 把邮件发送出去
            transport.connect(sendEmailInfo.getMailSmtpHost(), sendEmailInfo.getUser(), sendEmailInfo.getPassword());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            log.error("邮件发送异常", e);
            throw e;
        }
        return true;
    }

    @Data
    public static class SendEmailInfo {

        @NotBlank
        private String mailSmtpHost;

        @NotNull
        private Integer mailSmtpPort;

        @Email
        private String user;

        @NotBlank
        private String password;

        /**
         * 邮件接受人，可以有多个
         */
        @Size(min = 1, max = 1000)
        private List<String> tos;

        /**
         * 抄送邮件接受人，可以有多个
         */
        @Size(min = 0, max = 1000)
        private List<String> cc;

        /**
         * 邮件标题
         */
        @NotBlank
        private String title;

        /**
         * 发送内容，可以为html
         */
        @NotBlank
        private String text;

    }

}
