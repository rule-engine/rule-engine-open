package cn.ruleengine.web.service;


import cn.ruleengine.web.vo.symbol.SymbolResponse;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
public interface SymbolService {

    /**
     * 规则引擎运算符
     *
     * @param value 例如：CONTROLLER
     * @return >,<,=..
     */
    List<SymbolResponse> getByType(String value);

}
