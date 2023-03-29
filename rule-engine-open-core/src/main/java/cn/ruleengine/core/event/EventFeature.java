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

import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/12/12
 * @since 1.0.0
 */
public enum EventFeature {

    /**
     * 事件模型实时生成的数据 持久化类型
     * <p>
     * Snapshot 快照 存在丢数据问题，数据一致性不能保证
     * RealTime 实时保存 性能稍微下降
     * 可同时开启，建议只开启其中一个，对与时效性没有要求的可以选择Snapshot
     */
    ENDURANCE_SNAPSHOT, ENDURANCE_REAL_TIME;

    @Getter
    private final int mark;

    EventFeature() {
        this.mark = (1 << this.ordinal());
    }

}
