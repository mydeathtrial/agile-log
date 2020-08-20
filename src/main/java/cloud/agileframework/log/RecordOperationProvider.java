package cloud.agileframework.log;

import cloud.agileframework.spring.util.ServletUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.handler.MatchableHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author 佟盟
 * 日期 2020/8/00020 10:16
 * 描述 TODO
 * @version 1.0
 * @since 1.0
 */
public class RecordOperationProvider implements ExecutionObjectProvider {

    @Autowired
    private ObjectProvider<MatchableHandlerMapping> handlerMappings;

    @Autowired
    private RecordOperationManager recordOperationManager;

    private static final String HANDLE_METHOD = "$cloud.agileframework.handlerMethod";

    @Override
    public void pass(ExecutionInfo executionInfo) {
        HandlerMethod handlerMethod = identifying(ServletUtil.getCurrentRequest());

        recordOperationManager.record(handlerMethod, executionInfo);
    }

    /**
     * 根据请求，提取请求的目标方法
     *
     * @param request 请求
     * @return 目标方法标志
     */
    public HandlerMethod identifying(HttpServletRequest request) {
        Object handlerMethod = request.getAttribute(HANDLE_METHOD);
        if (handlerMethod instanceof HandlerMethod) {
            return (HandlerMethod) handlerMethod;
        }
        return handlerMappings.orderedStream().map(handlerMapping -> {
            try {
                HandlerExecutionChain handlerExecutionChain = handlerMapping.getHandler(request);
                Object handler;
                if (handlerExecutionChain != null) {
                    handler = handlerExecutionChain.getHandler();
                    if (handler instanceof HandlerMethod) {
                        return (HandlerMethod) handler;
                    }
                }
            } catch (Exception ignored) {
            }
            return null;
        }).filter(Objects::nonNull).findFirst().orElse(null);

    }
}
