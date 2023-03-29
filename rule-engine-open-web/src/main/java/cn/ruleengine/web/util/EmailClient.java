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
package cn.ruleengine.web.util;

import cn.ruleengine.web.enums.ErrorCodeEnum;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;
import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Component
@Slf4j
public class EmailClient {

    @Value("${spring.mail.username}")
    private String from;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 异步发送邮件
     * 注意:出现错误信息不会被显示页面
     *
     * @param params       模板参数
     * @param title        邮箱标题
     * @param templateName 模板名称
     * @param tos          发送给谁,可以是多个
     */
    @Async
    public void sendSimpleMailAsync(Object params, String title, String templateName, String... tos) {
        sendSimpleMail(params, title, templateName, tos);
    }

    /**
     * 同步发送邮件
     *
     * @param params       模板参数
     * @param title        邮箱标题
     * @param templateName 模板名称
     * @param tos          发送给谁,可以是多个
     */
    public void sendSimpleMail(Object params, String title, String templateName, String... tos) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(tos);
            helper.setSubject(title);
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
            helper.setText(text, true);
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            String message = e.getMessage();
            if (message == null) {
                throw e;
            }
            //处理发送邮箱异常消息
            if (message.contains("550")) {
                throw new ValidationException(ErrorCodeEnum.RULE10011032.getMsg());
            }
            if (message.contains("501")) {
                throw new ValidationException(ErrorCodeEnum.RULE10011033.getMsg());
            }
            throw new ValidationException(ErrorCodeEnum.RULE10011034.getMsg(), e);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

}
