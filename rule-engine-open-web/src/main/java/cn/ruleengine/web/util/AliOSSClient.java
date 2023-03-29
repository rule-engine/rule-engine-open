package cn.ruleengine.web.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import jodd.util.StringPool;
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
import java.io.InputStream;
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
        return new OSSClient(properties.getEndPoint(), properties.getAccessKeyId(), properties.getAccessKeySecret());
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
        if (this.ossClient == null) {
            log.warn("aliyun oss not configured！");
            return StringPool.EMPTY;
        }
        //创建OSS客户端
        try {
            String dir = Validator.isEmpty(folder) ? fileName : folder + "/" + fileName;
            //文件大小
            long fileSize = is.available();
            //创建上传文件的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传文件的长度
            metadata.setContentLength(fileSize);
            //指定该object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该object被下载时的内容编码方式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(FileTypeUtils.getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //此处上传文件
            this.ossClient.putObject(properties.getBucketName(), dir, is, metadata);
            //1000年
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 1000);
            //生成URL
            String url = this.ossClient.generatePresignedUrl(properties.getBucketName(), dir, expiration).toString();
            log.info("上传{}文件成功,URL:{}", fileName, url);
            return url;
        } catch (Exception e) {
            log.error("{1}", e);
            throw new ValidationException(ErrorCodeEnum.RULE10011036.getMsg());
        } finally {
            IoUtil.close(is);
        }
    }

}
