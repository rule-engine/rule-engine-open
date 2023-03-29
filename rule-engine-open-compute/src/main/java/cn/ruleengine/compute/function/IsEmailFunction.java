/**
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
package cn.ruleengine.compute.function;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.FailureStrategy;
import cn.ruleengine.core.annotation.Function;
import cn.ruleengine.core.annotation.Param;
import lombok.extern.slf4j.Slf4j;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/4/7
 * @since 1.0.0
 */
@Slf4j
@Function
public class IsEmailFunction {

    /**
     * 函数主方法，如果不存在抛出异常
     *
     * @param value 参数绑定，函数入参
     * @return true/false
     */
    @Executor
    public Boolean executor(@Param(value = "value") String value) {
        return Validator.isEmail(value);
    }

    /**
     * 此函数失败默认返回false；
     *
     * @param value 参数绑定，函数入参
     * @return false
     */
    @FailureStrategy
    public Boolean failureStrategy(@Param(value = "value") String value) {
        log.info("我是函数失败策略方法：" + value);
        return false;
    }
}
