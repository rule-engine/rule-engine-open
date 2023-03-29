package cn.ruleengine.web.service.impl;

import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.value.ValueType;
import cn.ruleengine.web.service.SymbolService;
import cn.ruleengine.web.vo.symbol.SymbolResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Service
public class SymbolServiceImpl implements SymbolService {

    /**
     * 规则引擎运算符
     *
     * @param value 例如：CONTROLLER
     * @return >,<,=..
     */
    @Override
    public List<SymbolResponse> getByType(String value) {
        ValueType valueType = ValueType.getByValue(value);
        List<Operator> symbol = valueType.getSymbol();
        return symbol.stream().map(m -> {
            SymbolResponse symbolResponse = new SymbolResponse();
            symbolResponse.setName(m.name());
            symbolResponse.setExplanation(m.getExplanation());
            symbolResponse.setSymbol(m.getSymbol());
            return symbolResponse;
        }).collect(Collectors.toList());
    }
}
