package cn.ruleengine.web.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Slf4j
@Component
public class AliOSSClient {

    @Getter
    private Properties properties;
    private OSSClient ossClient;

    @Autowired(required = false)
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Autowired(required = false)
    public void setOssClient(OSSClient ossClient) {
        this.ossClient = ossClient;
    }

    @Data
    @Component
    @ConditionalOnProperty(prefix = "aliyun.oss", name = "enable", havingValue = "true")
    @ConfigurationProperties("aliyun.oss")
    public static class Properties {
        /**
         * 是否启用oss
         */
        private boolean enable;
        private String endPoint;
        private String accessKeyId;
        private String accessKeySecret;
        private String bucketName;
        private String defaultFolder;
    }

    /**
     * 初始化OSSClient,由spring创建单例的bean进行维护
     *
     * @return OSSClient
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnBean(Properties.class)
    private OSSClient ossClient(Properties properties) {
        log.info("init ossClient");
        ClientConfiguration configuration = new ClientConfiguration();
        return new OSSClient(properties.getEndPoint(), properties.getAccessKeyId(), properties.getAccessKeySecret(), configuration);
    }


    /**
     * 上传文件,使用默认文件夹
     *
     * @param filePath 文件路径
     * @return r
     */
    public String upload(String filePath) {
        return this.upload(filePath, null);
    }


    /**
     * 上传文件,使用默认文件夹
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @return r
     */
    public String upload(String filePath, String fileName) {
        File file = new File(filePath);
        try (BufferedInputStream inputStream = FileUtil.getInputStream(file)) {
            return this.upload(inputStream, fileName == null ? file.getName() : fileName, properties.getDefaultFolder());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传文件,使用默认文件夹
     *
     * @param is       文件数据
     * @param fileName 文件名称
     * @return url连接
     */
    public String upload(InputStream is, String fileName) {
        return this.upload(is, fileName, properties.getDefaultFolder());
    }

    /**
     * 上传文件
     *
     * @param is       文件数据
     * @param fileName 文件名称
     * @param folder   文件夹
     * @return url连接
     */
    public String upload(InputStream is, String fileName, String folder) {
        try {
            String updateFilePath = Validator.isEmpty(folder) ? fileName : folder + "/" + fileName;
            // 此处上传文件
            String bucketName = properties.getBucketName();
            this.ossClient.putObject(bucketName, updateFilePath, is);
            // 生成URL
            String url = "https://" + bucketName + "." + properties.getEndPoint() + "/" + updateFilePath;
            log.info("上传{}文件成功,URL:{}", fileName, url);
            return url;
        } catch (Exception e) {
            log.error("{1}", e);
            throw new ValidationException(ErrorCodeEnum.RULE10011036.getMsg());
        }
    }

    /**
     * 根据路径获取文件
     *
     * @param key 文件路径
     * @return input
     */
    public InputStream download(String key) {
        String bucketName = this.properties.getBucketName();
        OSSObject object = this.ossClient.getObject(bucketName, key);
        return object.getObjectContent();
    }

    /**
     * 下载为byte数组，并关闭流
     */
    public byte[] downloadBytes(String key) {
        InputStream inputStream = this.download(key);
        if (inputStream == null) {
            return null;
        }
        try {
            // 转base64
            return IoUtil.readBytes(inputStream);
        } finally {
            IoUtil.close(inputStream);
        }
    }

    /**
     * 根据路径删除文件
     *
     * @param key 文件路径
     */
    public void delete(String key) {
        String bucketName = this.properties.getBucketName();
        this.ossClient.deleteObject(bucketName, key);
    }


    /**
     * 生成文件访问地址
     *
     * @param key       文件key
     * @param dateField 过期时间单位
     * @param offset    过期时间
     * @return 文件访问地址
     */
    public String generateUrl(String key, DateField dateField, int offset) {
        String bucketName = this.properties.getBucketName();
        DateTime dateTime = DateUtil.offset(new Date(), dateField, offset);
        URL url = this.ossClient.generatePresignedUrl(bucketName, key, dateTime);
        return url.toString();
    }

    /**
     * 生成文件访问地址
     * <p>
     * 默认10小时
     *
     * @param key 文件key
     * @return 文件访问地址
     */
    public String generateUrl(String key) {
        String bucketName = this.properties.getBucketName();
        DateTime dateTime = DateUtil.offset(new Date(), DateField.MINUTE, 60 * 10);
        URL url = this.ossClient.generatePresignedUrl(bucketName, key, dateTime);
        return url.toString();
    }


}
