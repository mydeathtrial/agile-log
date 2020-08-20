package cloud.agileframework.log;

import org.springframework.web.method.HandlerMethod;

/**
 * @author 佟盟
 * 日期 2020/8/00020 10:32
 * 描述 TODO
 * @version 1.0
 * @since 1.0
 */
public interface RecordOperationManager {
    /**
     * 记录操纵
     *
     * @param handlerMethod 请求的目标方法
     * @param executionInfo 执行信息
     */
    void record(HandlerMethod handlerMethod, ExecutionInfo executionInfo);
}
