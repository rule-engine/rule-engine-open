package cn.ruleengine.core;


import cn.ruleengine.core.exception.EngineException;
import cn.ruleengine.core.rule.GeneralRule;
import cn.ruleengine.core.scorecard.ScoreCard;
import org.springframework.lang.NonNull;

import java.io.Closeable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈Container〉
 *
 * @author 丁乾文
 * @date 2021/7/22 10:49 上午
 * @since 1.0.0
 */
public class Container implements Closeable {

    private final Body<GeneralRule> generalRuleContainer = new Body<>();

    private final Body<ScoreCard> scoreCardContainer = new Body<>();


    public Body<GeneralRule> getGeneralRuleContainer() {
        return this.generalRuleContainer;
    }


    public Body<ScoreCard> getScoreCardContainer() {
        return scoreCardContainer;
    }

    @Override
    public void close() {
        this.generalRuleContainer.close();
        this.scoreCardContainer.close();
    }

    public static class Body<T extends DataSupport> implements Closeable {

        /**
         * 启动时加载的规则/决策表
         */
        private final Map<String, Map<String, T>> workspaceMap;

        public Body() {
            this.workspaceMap = new ConcurrentHashMap<>();
        }

        public Body(Map<String, Map<String, T>> workspaceMap) {
            this.workspaceMap = workspaceMap;
        }

        public Map<String, Map<String, T>> getWorkspaceMap() {
            return Collections.unmodifiableMap(workspaceMap);
        }

        /**
         * 从引擎中根据决策表code查询一个决策表/规则
         *
         * @param workspaceCode 工作空间code
         * @param ruleCode      决策表/规则code
         * @return DecisionTable
         */
        public T get(String workspaceCode, String ruleCode) {
            Objects.requireNonNull(workspaceCode);
            Objects.requireNonNull(ruleCode);
            Map<String, T> workspaceMap = this.workspaceMap.get(workspaceCode);
            if (workspaceMap == null) {
                throw new EngineException("Can't find this workspace：" + workspaceCode);
            }
            return workspaceMap.get(ruleCode);
        }

        /**
         * 是否存在某规则/决策表
         *
         * @param workspaceCode 工作空间code
         * @param ruleCode      规则/决策表code
         * @return true存在
         */
        public boolean isExists(String workspaceCode, String ruleCode) {
            if (workspaceCode == null || ruleCode == null) {
                return false;
            }
            if (!this.workspaceMap.containsKey(workspaceCode)) {
                return false;
            }
            return this.workspaceMap.get(workspaceCode).containsKey(ruleCode);
        }

        /**
         * 添加单个
         *
         * @param dataSupport 配置信息
         */
        public synchronized void add(T dataSupport) {
            Objects.requireNonNull(dataSupport);
            String workspaceCode = Objects.requireNonNull(dataSupport.getWorkspaceCode());
            String ruleSetCode = Objects.requireNonNull(dataSupport.getCode());
            if (!this.workspaceMap.containsKey(workspaceCode)) {
                this.workspaceMap.put(workspaceCode, new ConcurrentHashMap<>());
            }
            this.workspaceMap.get(workspaceCode).put(ruleSetCode, dataSupport);
        }

        /**
         * 添加多个
         *
         * @param dataSupports 配置信息列表
         */
        public void addMultiple(List<T> dataSupports) {
            Objects.requireNonNull(dataSupports);
            dataSupports.forEach(this::add);
        }

        /**
         * 从规则引擎删除一个规则集
         *
         * @param ruleSetCode 规则集code
         */
        public void remove(String workspaceCode, @NonNull String ruleSetCode) {
            if (this.workspaceMap.containsKey(workspaceCode)) {
                this.workspaceMap.get(workspaceCode).remove(ruleSetCode);
            }
        }

        @Override
        public void close() {
            this.workspaceMap.clear();
        }
    }

}
