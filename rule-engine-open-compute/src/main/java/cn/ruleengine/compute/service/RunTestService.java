package cn.ruleengine.compute.service;


import cn.ruleengine.compute.vo.RunTestRequest;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/26
 * @since 1.0.0
 */
public interface RunTestService {

    /**
     * 模拟运行
     *
     * @param runTestRequest 参数信息
     * @return result
     */
    Object run(RunTestRequest runTestRequest);

}
