package cn.ruleengine.web.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户数据权限表
 * </p>
 *
 * @author dqw
 * @since 2021-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RuleEngineDataPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 0：规则  1：规则集  2：决策表
     */
    private Integer dataType;

    /**
     * 如果data_type=0 则此data_id为规则的id
     */
    private Integer dataId;


    /**
     * 0有写权限
     */
    private Integer writeAuthority;

    /**
     * 0有发布规则权限
     */
    private Integer publishAuthority;

    /**
     * 谁添加的这个权限，可以是这个规则的创建人，也可以是管理
     */
    private Integer createUserId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}
