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

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 获取集合中第几个规则参数,index从0开始
 *
 * @author dingqianwen
 * @date 2020/8/29
 * @since 1.0.0
 */
@Slf4j
@Function
public class GetCollectionElementsFunction {

    @Executor
    public String executor(@Valid Params params) {
        List<String> list = params.getList();
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        if (list.size() - 1 < params.getIndex()) {
            return null;
        }
        return list.get(params.getIndex());
    }

    @Data
    public static class Params {
        private List<String> list;
        @NotNull
        private Integer index;
    }

}
