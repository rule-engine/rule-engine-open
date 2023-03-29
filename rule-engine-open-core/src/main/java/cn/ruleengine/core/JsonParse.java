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
package cn.ruleengine.core;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/4
 * @since 1.0.0
 */
public interface JsonParse {

    /**
     * fastjson存在bug 替换fastjson
     */
    ObjectMapper OBJECT_MAPPER = new ObjectMapper() {

        private static final long serialVersionUID = -5640476057715217573L;

        {
            this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            this.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.EVERYTHING, JsonTypeInfo.As.WRAPPER_OBJECT);
        }
    };

    /**
     * 规则json字符串转为rule对象
     *
     * @param jsonString 规则json字符串
     */
    default void fromJson(String jsonString) {
        throw new UnsupportedOperationException();
    }

    /**
     * 规则信息转为json
     *
     * @return 规则json字符串
     */
    @SneakyThrows
    @NonNull
    default String toJson() {
        return OBJECT_MAPPER.writeValueAsString(this);
    }

}
