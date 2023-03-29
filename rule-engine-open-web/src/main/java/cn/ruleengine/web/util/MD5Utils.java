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
package cn.ruleengine.web.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.DigestUtils;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2019/8/14
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MD5Utils {

    /**
     * 盐，用于混交md5
     */
    private static final String SLAT = "&%5123***&&%%$$#@";

    /**
     * 生成md5
     *
     * @param str 需要生成的字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String str) {
        String base = str + StringPool.SLASH + SLAT;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

}
