package cn.ruleengine.web.vo.workspace;

import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/11/21
 * @since 1.0.0
 */
@Data
public class Workspace implements Serializable {

    private static final long serialVersionUID = 8313752582019183771L;

    private Integer id;

    private String name;

    private String code;

}
