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
package cn.ruleengine.core.event;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 事件模型
 * <p>
 * 主要作用： 组建事件模型，条件满足触发对应的事件
 * 例： 当用户登录次数大于3次 1分钟 或者...锁定
 * 概述： 当什么什么条件满足，或者什么什么条件满足，触发什么什么
 *
 * @author 丁乾文
 * @date 2020/12/12
 * @since 1.0.0
 */
@Data
public class Event {

    /**
     * 事件名称
     */
    private String name;


    private int features;

    public void enabled(EventFeature eventEndurance) {
        this.features |= eventEndurance.getMark();
    }

    public boolean isEnabled(EventFeature eventEndurance) {
        return (this.features & eventEndurance.getMark()) != 0;
    }

}
