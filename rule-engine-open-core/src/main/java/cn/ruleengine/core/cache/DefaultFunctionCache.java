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

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;

/**
 * 〈一句话功能简述〉<br>
 * 〈基于内存的缓存实现类〉
 *
 * @author dingqianwen
 * @date 2020/8/13
 * @since 1.0.0
 */
public class DefaultFunctionCache implements FunctionCache {

    /**
     * 默认使用LRUCache 存储函数缓存信息
     */
    private final LRUCache<String, Object> cache;

    public DefaultFunctionCache(int capacity) {
        cache = CacheUtil.newLRUCache(capacity);
    }

    public DefaultFunctionCache() {
        cache = CacheUtil.newLRUCache(1600);
    }

    @Override
    public void put(String key, Object value, long timeout) {
        this.cache.put(key, value, timeout);
    }

    @Override
    public Object get(String key) {
        return this.cache.get(key);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

}
