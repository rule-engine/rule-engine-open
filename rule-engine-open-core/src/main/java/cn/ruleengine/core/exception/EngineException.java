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
package cn.ruleengine.core.exception;

import cn.hutool.core.text.StrFormatter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/9
 * @since 1.0.0
 */
public class EngineException extends RuntimeException {

    private static final long serialVersionUID = -3830935567722595556L;

    public EngineException(String message) {
        super(message);
    }

    public EngineException(String message, Object... args) {
        super(StrFormatter.format(message, args));
    }

    public EngineException(Throwable e) {
        super(e);
    }

}
