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
package cn.ruleengine.core.cache;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 函数是否缓存
 * <p>
 * 注意：目前只有发布后的规则执行会触发函数缓存，模拟运行则不会
 *
 * @author dingqianwen
 * @date 2020/8/6
 * @since 1.0.0
 */
public interface FunctionCache {

    /**
     * 函数添加缓存的方法
     *
     * @param key     缓存key
     * @param value   缓存的value
     * @param timeout 缓存的有效时间
     */
    void put(String key, Object value, long timeout);

    /**
     * 获取函数缓存的方法
     *
     * @param key 缓存的key
     * @return 缓存的value
     */
    Object get(String key);

    /**
     * 清除所有缓存信息
     */
    void clear();
}
