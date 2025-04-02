/*
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

import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import cn.ruleengine.core.annotation.Param;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 获取集合随机值
 *
 * @author dingqianwen
 * @date 2020/9/8
 * @since 1.0.0
 */
@Slf4j
@Function
public class CollectionRandomValueFunction {

    /**
     * 获取集合随机值
     *
     * @param list 集合
     * @return 集合中随机值
     */
    @Executor
    public String executor(@Param(value = "list") List<String> list) {
        int index = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index);
    }

}
