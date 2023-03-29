package cn.ruleengine.web.vo.common;

import lombok.Data;


/***
 * 数据类型和id
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/7/12 1:37 下午
 **/
@Data
public class DataTypeAndId {

    private Integer dataType;

    /**
     * 数据库id
     * 如果data_type=0 则此data_id为规则的id
     */
    private Integer dataId;

}
