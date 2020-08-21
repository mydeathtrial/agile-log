package cloud.agileframework.log;

import cloud.agileframework.spring.util.MappingUtil;
import cloud.agileframework.spring.util.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

/**
 * @author 佟盟
 * 日期 2020/8/00020 10:16
 * 描述 TODO
 * @version 1.0
 * @since 1.0
 */
public class RecordOperationProvider implements ExecutionObjectProvider {

    @Autowired
    private RecordOperationManager recordOperationManager;


    @Override
    public void pass(ExecutionInfo executionInfo) {
        HandlerMethod handlerMethod = MappingUtil.matching(ServletUtil.getCurrentRequest());

        recordOperationManager.record(handlerMethod, executionInfo);
    }

}
