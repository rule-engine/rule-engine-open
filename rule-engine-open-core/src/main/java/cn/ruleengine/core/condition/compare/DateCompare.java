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
package cn.ruleengine.core.condition.compare;


import cn.hutool.core.util.StrUtil;
import cn.ruleengine.core.condition.Compare;
import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.exception.ConditionException;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/2/18
 * @since 1.0.0
 */
public class DateCompare implements Compare {

    private DateCompare() {
    }

    private static final DateCompare DATE_COMPARE = new DateCompare();

    public static DateCompare getInstance() {
        return DATE_COMPARE;
    }

    @Override
    public boolean compare(Object leftValue, Operator operator, Object rightValue) {
        if (leftValue == null || rightValue == null) {
            return false;
        }
        if (!(leftValue instanceof Date) || !(rightValue instanceof Date)) {
            throw new ConditionException("左值/右值必须是Date");
        }
        Date lValue = (Date) leftValue;
        Date rValue = (Date) rightValue;
        int compare = lValue.compareTo(rValue);
        switch (operator) {
            case EQ:
                return compare == 0;
            case GT:
                return compare > 0;
            case NE:
                return compare != 0;
            case LT:
                return compare < 0;
            case GE:
                return compare == 0 || compare > 0;
            case LE:
                return compare == 0 || compare < 0;
            default:
                throw new IllegalStateException("Unexpected value: " + operator);
        }
    }


    /**
     * 增强 Date
     */
    public static class DateTime extends Date {

        private static final long serialVersionUID = -2186215242020570057L;

        /**
         * 默认格式
         */
        public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
        /**
         * 支持以下格式日期
         */
        public static final String[] PARSE_PATTERNS = {
                "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM",
                "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
                "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

        /**
         * 时间戳格式
         */
        private static final Pattern TIMESTAMP_PATTERN = Pattern.compile("\\d{13}");


        private String pattern;

        /**
         * 给定日期的构造
         *
         * @param pattern;
         * @param date     日期
         */
        public DateTime(Date date, String pattern) {
            this(date.getTime());
            this.pattern = pattern;
        }

        public DateTime(Date date) {
            this(date.getTime());
        }

        public DateTime(long timeMillis, String pattern) {
            super(timeMillis);
            this.pattern = pattern;
        }

        public DateTime(long timeMillis) {
            super(timeMillis);
        }

        /**
         * 转换JDK date为 DateTime
         *
         * @param date JDK Date
         * @return DateTime
         */
        public static DateTime of(Date date) {
            if (date instanceof DateTime) {
                return (DateTime) date;
            }
            return new DateTime(date);
        }

        /**
         * 时间戳 转为 DateTime
         *
         * @param timeMillis 时间戳
         * @return DateTime
         */
        public static DateTime of(long timeMillis) {
            return new DateTime(timeMillis);
        }

        /**
         * 兼容以下类型：Date，DateTime，【PARSE_PATTERNS】，时间戳
         *
         * @param object 日期时间
         * @return DateTime
         */
        public static DateTime of(Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Date) {
                return of((Date) object);
            }
            // 时间戳
            if (object instanceof Long) {
                return of((long) object);
            }
            // 2021-01-01 00:00:00
            String dateString = String.valueOf(object);
            if (object instanceof String) {
                // {@link DateCompare#PARSE_PATTERNS}
                try {
                    Date date = DateUtils.parseDate(dateString, PARSE_PATTERNS);
                    return new DateTime(date);
                } catch (ParseException ignored) {
                    // ignored
                }
            }
            // 判断字符串是否为时间戳
            if (TIMESTAMP_PATTERN.matcher(dateString).matches()) {
                return new DateTime(Long.parseLong(dateString));
            }
            return null;
        }

        /**
         * 转为"yyyy-MM-dd HH:mm:ss " or pattern 格式字符串
         *
         * @return 默认返回 "yyyy-MM-dd HH:mm:ss " 格式字符串
         */
        @Override
        public String toString() {
            if (StrUtil.isNotBlank(pattern)) {
                return this.toString(pattern);
            }
            return this.toString(DEFAULT_PATTERN);
        }

        public String toString(String pattern) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(this);
        }

    }


}
