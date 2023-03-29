package cn.ruleengine.web.vo.function;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/11
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateFunction extends AddFunction{

    @NotNull
    private Integer id;

}
