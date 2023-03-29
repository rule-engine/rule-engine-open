package cn.ruleengine.web.timer;

import cn.ruleengine.web.store.manager.RuleEngineGeneralRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/3/18
 * @since 1.0.0
 */
@Slf4j
@Component
public class DeleteTestDataToBeEditTimer {

    private static final String TIMER_LOCK_NAME = "ruleengine-timer-lock:DeleteTestDataToBePublishedTimer";

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RuleEngineGeneralRuleManager ruleEngineGeneralRuleManager;

    /**
     * 这个定时任务需要删掉，只是个人项目体验环境清除一些脏数据用的
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void execute() {
        // 多实例只保证一台执行即可
        RLock lock = this.redissonClient.getLock(TIMER_LOCK_NAME);
        try {
            if (!lock.tryLock(0L, 60L, TimeUnit.SECONDS)) {
                return;
            }
            log.info("开始删除开发中的测试数据");
//            this.ruleEngineGeneralRuleManager.lambdaUpdate()
//                    .isNull(RuleEngineGeneralRule::getPublishVersion)
//                    .eq(RuleEngineGeneralRule::getStatus, DataStatus.DEV.getStatus())
//                    .remove();
//            this.ruleEngineRuleSetManager.lambdaUpdate()
//                    .isNull(RuleEngineRuleSet::getPublishVersion)
//                    .eq(RuleEngineRuleSet::getStatus, DataStatus.DEV.getStatus())
//                    .remove();
//            this.ruleEngineDecisionTableManager.lambdaUpdate()
//                    .isNull(RuleEngineDecisionTable::getPublishVersion)
//                    .eq(RuleEngineDecisionTable::getStatus, DataStatus.DEV.getStatus())
//                    .remove();
            log.info("删除完毕");
        } catch (InterruptedException e) {
            log.error("{0}", e);
        } finally {
            lock.unlock();
        }
    }

}
