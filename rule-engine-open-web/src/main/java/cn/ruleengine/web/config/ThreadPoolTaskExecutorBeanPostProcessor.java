package cn.ruleengine.web.config;

import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/29
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ThreadPoolTaskExecutorBeanPostProcessor implements BeanPostProcessor {


    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof ThreadPoolTaskExecutor) {
            ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) bean;
            Field taskDecoratorField = ThreadPoolTaskExecutor.class.getDeclaredField("taskDecorator");
            taskDecoratorField.setAccessible(true);
            TaskDecorator taskDecorator = (TaskDecorator) taskDecoratorField.get(threadPoolTaskExecutor);
            TracerTaskDecorator tracerTaskDecorator = new TracerTaskDecorator(taskDecorator);
            threadPoolTaskExecutor.setTaskDecorator(tracerTaskDecorator);
            return threadPoolTaskExecutor;
        }
        return bean;
    }


    @RequiredArgsConstructor
    public static class TracerTaskDecorator implements TaskDecorator {

        private final TaskDecorator delegate;

        @NonNull
        @Override
        public Runnable decorate(@NonNull Runnable runnable) {
            Map<String, String> context = MDC.getCopyOfContextMap();
            UserData userData = Context.getCurrentUser();
            Workspace workspace = Context.getCurrentWorkspace();
            Runnable finalRunnable = Objects.nonNull(delegate) ? delegate.decorate(runnable) : runnable;
            return () -> {
                try {
                    if (context != null) {
                        MDC.setContextMap(context);
                    }
                    if (userData != null) {
                        Context.setCurrentUser(userData);
                    }
                    if (workspace != null) {
                        Context.setCurrentWorkspace(workspace);
                    }
                    finalRunnable.run();
                } finally {
                    MDC.clear();
                    Context.clearAll();
                }
            };
        }

    }

}
