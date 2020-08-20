package cloud.agileframework.log;

/**
 * @author 佟盟
 * 日期 2020/8/00017 19:49
 * 描述 TODO
 * @version 1.0
 * @since 1.0
 */
public interface ExecutionObjectProvider {
    /**
     * 请求执行过程，可以用于记录操作日志
     *
     * @param executionInfo 执行信息
     */
    void pass(ExecutionInfo executionInfo);
}
