package cn.ruleengine.web.vo.workspace;

import lombok.Data;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/12/11
 * @since 1.0.0
 */
@Data
public class AccessKey {

    private Integer id;
    private String accessKeyId;
    private String accessKeySecret;

    /**
     * 对比AccessKey
     *
     * @param accessKeyId     id
     * @param accessKeySecret Secret
     */
    public boolean equals(String accessKeyId, String accessKeySecret) {
        if (accessKeyId == null || accessKeySecret == null) {
            return false;
        }
        if (!this.accessKeyId.equals(accessKeyId)) {
            return false;
        }
        return this.accessKeySecret.equals(accessKeySecret);
    }
}
