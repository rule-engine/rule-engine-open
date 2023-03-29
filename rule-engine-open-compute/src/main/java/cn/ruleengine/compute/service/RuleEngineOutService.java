package cn.ruleengine.compute.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.ruleengine.compute.service.impl.BatchExecuteTask;
import cn.ruleengine.compute.vo.*;
import cn.ruleengine.core.Container;
import cn.ruleengine.core.DefaultInput;
import cn.ruleengine.core.Engine;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.exception.EngineException;
import cn.ruleengine.core.exception.ValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/22
 * @since 1.0.0
 */
@Slf4j
public abstract class RuleEngineOutService {


    private final Engine engine;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final WorkspaceService workspaceService;
    private final Container.Body<?> containerBody;

    public RuleEngineOutService(Engine engine,
                                Container.Body<?> containerBody,
                                ThreadPoolTaskExecutor threadPoolTaskExecutor, WorkspaceService workspaceService) {
        this.engine = engine;
        this.containerBody = containerBody;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.workspaceService = workspaceService;
    }

    /**
     * 执行单个规则，获取执行结果
     *
     * @param executeRequest 执行规则入参
     * @return 规则执行结果
     */
    public Object execute(ExecuteRequest executeRequest) {
        log.info("开始执行，入参：{}", executeRequest);
        String workspaceCode = executeRequest.getWorkspaceCode();
        // 后期改为jwt方式校验 这个后期有多远就看以后某一天心情好了，或者有时间了就可以编写。。。。。。。
        AccessKey accessKey = this.workspaceService.accessKey(workspaceCode);
        if (!accessKey.equals(executeRequest.getAccessKeyId(), executeRequest.getAccessKeySecret())) {
            throw new ValidException("AccessKey Verification failed");
        }
        Input input = new DefaultInput(executeRequest.getParam());
        return this.engine.execute(input, workspaceCode, executeRequest.getCode());
    }


    /**
     * 默认批量执行器
     * <p>
     * 批量执行多个(一次最多1000个)，获取执行结果
     *
     * @param batchExecuteRequest 批量数据
     * @return 执行结果
     */
    public Object batchExecute(BatchExecuteRequest batchExecuteRequest) {
        String workspaceCode = batchExecuteRequest.getWorkspaceCode();
        AccessKey accessKey = this.workspaceService.accessKey(workspaceCode);
        if (!accessKey.equals(batchExecuteRequest.getAccessKeyId(), batchExecuteRequest.getAccessKeySecret())) {
            throw new ValidException("AccessKey Verification failed");
        }
        List<BatchExecuteRequest.ExecuteInfo> executeInfos = batchExecuteRequest.getExecuteInfos();
        Integer threadSegNumber = batchExecuteRequest.getThreadSegNumber();
        log.info("批量执行数量：{},单个线程执行{}条", executeInfos.size(), threadSegNumber);
        List<BatchExecuteResponse> outputs = new CopyOnWriteArrayList<>();
        int countDownNumber = executeInfos.size() % threadSegNumber == 0 ? executeInfos.size() / threadSegNumber : (executeInfos.size() / threadSegNumber) + 1;
        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(countDownNumber);
        // 批量插入，执行一次批量
        for (int fromIndex = 0; fromIndex < executeInfos.size(); fromIndex += threadSegNumber) {
            int toIndex = Math.min(fromIndex + threadSegNumber, executeInfos.size());
            List<BatchExecuteRequest.ExecuteInfo> infoList = executeInfos.subList(fromIndex, toIndex);
            BatchExecuteTask batchExecuteRuleTask = new BatchExecuteTask(workspaceCode, countDownLatch, outputs, this.engine, infoList);
            this.threadPoolTaskExecutor.execute(batchExecuteRuleTask);
        }
        // 等待线程处理完毕
        try {
            Long timeout = batchExecuteRequest.getTimeout();
            if (timeout.equals(-1L)) {
                countDownLatch.await();
            } else {
                if (!countDownLatch.await(timeout, TimeUnit.MILLISECONDS)) {
                    throw new EngineException("Execution timeout:{}ms", timeout);
                }
            }
        } catch (InterruptedException e) {
            throw new EngineException("Execution failed, rule execution thread was interrupted", e);
        }
        return outputs;
    }

    /**
     * 引擎中是否存在这个规则
     *
     * @param isExistsRequest 参数
     * @return true存在
     */
    public Boolean isExists(IsExistsRequest isExistsRequest) {
        String workspaceCode = isExistsRequest.getWorkspaceCode();
        AccessKey accessKey = this.workspaceService.accessKey(workspaceCode);
        if (!accessKey.equals(isExistsRequest.getAccessKeyId(), isExistsRequest.getAccessKeySecret())) {
            throw new ValidException("AccessKey Verification failed");
        }
        return this.containerBody.isExists(isExistsRequest.getWorkspaceCode(), isExistsRequest.getCode());
    }

}
