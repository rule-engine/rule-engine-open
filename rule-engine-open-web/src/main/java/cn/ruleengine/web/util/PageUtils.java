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

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.common.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈主要解决分页查询时重复代码开发〉
 *
 * @author v-dingqianwen.ea
 * @date 2019/8/11
 * @since 1.0.0
 */
public class PageUtils {

    private static final String DEFAULT_COLUMN_NAME = "create_time";

    /**
     * 主要简化分页查询使用,减少往后开发量,依赖于mybatis plus
     * <p>
     * 使用案例:
     * 不进行类型转换直接返回结果例子:
     * PageResult<BootUser> page = PageUtils.page(bootUserManager, pageRequest.getPage(), queryWrapper, e -> e)
     * <p>
     * 类型转换例子:
     * <blockquote>
     * <pre>
     * PageResult<BootUserResponse> page = PageUtils.page(bootUserManager, pageRequest.getPage(), queryWrapper, e -> {
     *      BootUserResponse vo = new BootUserResponse();
     *      vo.setId(e.getId());
     *      vo.setUsername(e.getUsername());
     *      vo.setPassword(e.getPassword());
     *      return vo;
     * });
     * </pre>
     * </blockquote>
     *
     * @param manager      Manager对象,例如:UserManager
     * @param pageBase     分页数据
     * @param queryWrapper 插叙条件
     * @param mapper       类型转换使用
     * @param <T>          t Manager对象泛型
     * @param <Q>          q pojo对象泛型
     * @param <R>          r通过lambda map转换后的类型
     * @return 查询的PageResult
     * @author dqw
     */
    public static <T extends IService<Q>, Q, R> PageResult<R> page(T manager, PageBase pageBase, QueryWrapper<Q> queryWrapper, Function<? super Q, ? extends R> mapper) {
        PageResult<R> pageResult = new PageResult<>();
        //调用查询数据库方法
        IPage<Q> iPage = manager.page(new Page<>(pageBase.getPageIndex(), pageBase.getPageSize()), queryWrapper);
        List<Q> records = iPage.getRecords();
        //如果没有查询到数据,直接返回
        if (CollUtil.isEmpty(records)) {
            return pageResult;
        }
        //类型转换
        List<R> collect = records.stream().map(mapper).collect(Collectors.toList());
        //设置返回数据/分页数据
        pageResult.setData(new Rows<>(collect, getPageResponse(iPage)));
        return pageResult;
    }

    /**
     * 使用案例:
     * <blockquote>
     * <pre>
     *        PageResult<BootUserResponse> page = page(bootUserManager, pageBase, () -> {
     *             QueryWrapper<BootUserResponse> wrapper = new QueryWrapper<>();
     *             //查询条件
     *             if (Validator.isNotEmpty(username)) {
     *                 wrapper.lambda().eq(BootUserResponse::getUserName, username);
     *             }
     *             //选择排序
     *             defaultOrder(orders, wrapper);
     *             return wrapper;
     *        }, m -> m);
     * </pre>
     * </blockquote>
     * 更多使用方法查看 {@link PageUtils#page(IService, PageBase, QueryWrapper, Function)}
     *
     * @param manager      Manager对象,例如:UserManager
     * @param pageBase     分页数据
     * @param queryWrapper 插叙条件
     * @param mapper       类型转换使用
     * @param <T>          t Manager对象泛型
     * @param <Q>          q pojo对象泛型
     * @param <R>          r通过lambda map转换后的类型
     * @return 查询的PageResult
     * @author dqw
     */
    public static <T extends IService<Q>, Q, R> PageResult<R> page(T manager, PageBase pageBase, QueryWrapperFunction<Q> queryWrapper, Function<? super Q, ? extends R> mapper) {
        return page(manager, pageBase, queryWrapper.wrapper(), mapper);
    }

    @FunctionalInterface
    public interface QueryWrapperFunction<Q> {

        /**
         * 简化查询使用
         * 具体使用场景查看 {@link PageUtils#page(com.baomidou.mybatisplus.extension.service.IService, PageBase, QueryWrapperFunction, Function)}
         *
         * @return QueryWrapper
         */
        QueryWrapper<Q> wrapper();
    }

    /**
     * 默认排序
     *
     * @param orders       排序条件
     * @param queryWrapper 查询条件
     * @param <T>          T
     */
    public static <T> void defaultOrder(List<PageRequest.OrderBy> orders, QueryWrapper<T> queryWrapper) {
        defaultOrder(orders, queryWrapper, null);
    }

    /**
     * 默认排序,可以指定默认排序字段
     *
     * @param orders            排序条件
     * @param queryWrapper      查询条件
     * @param defaultColumnName 指定默认排序字段列
     * @param <T>               T
     */
    public static <T, R> void defaultOrder(List<PageRequest.OrderBy> orders, QueryWrapper<T> queryWrapper, SFunction<T, R> defaultColumnName) {
        if (CollUtil.isEmpty(orders)) {
            defaultOrderProcess(queryWrapper, defaultColumnName);
            return;
        }
        orders.forEach(orderBy -> {
            //默认时间倒序
            if (StringUtils.EMPTY.equals(orderBy.getColumnName())) {
                defaultOrderProcess(queryWrapper, defaultColumnName);
            } else {
                if (orderBy.isDesc()) {
                    queryWrapper.orderByDesc(orderBy.getColumnName());
                } else {
                    queryWrapper.orderByAsc(orderBy.getColumnName());
                }
            }
        });
    }

    /**
     * @param queryWrapper      查询条件
     * @param defaultColumnName 默认排序列名
     * @param <T>               t
     * @param <R>               r
     */
    private static <T, R> void defaultOrderProcess(QueryWrapper<T> queryWrapper, SFunction<T, R> defaultColumnName) {
        if (defaultColumnName != null) {
            queryWrapper.lambda().orderByDesc(defaultColumnName);
        } else {
            queryWrapper.orderByDesc(DEFAULT_COLUMN_NAME);
        }
    }

    /**
     * 分页参数类型转换
     *
     * @param pageResult pageResult
     * @return PageResponse
     */
    public static PageResponse getPageResponse(IPage<?> pageResult) {
        return new PageResponse(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
    }

}
