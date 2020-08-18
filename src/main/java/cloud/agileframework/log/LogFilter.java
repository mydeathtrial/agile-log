package cloud.agileframework.log;

import cloud.agileframework.spring.util.RequestWrapper;
import cloud.agileframework.spring.util.ServletUtil;
import cloud.agileframework.spring.util.spring.BeanUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 佟盟
 * 日期 2020/8/00017 14:18
 * 描述 TODO
 * @version 1.0
 * @since 1.0
 */
public class LogFilter extends AbstractRequestLoggingFilter {

    protected static final String AGILE_BUSINESS_LOG = "$AGILE_BUSINESS_LOG";
    private static final ThreadLocal<HttpServletResponse> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
//        getEnvironment()
        return logger.isDebugEnabled();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!(request instanceof RequestWrapper)) {
            request = new RequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        THREAD_LOCAL.set(response);
        super.doFilterInternal(request, response, filterChain);
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        request = WebUtils.getNativeRequest(request, RequestWrapper.class);

        if (request != null) {
            request.setAttribute(AGILE_BUSINESS_LOG, ExecutionInfo.builder()
                    .ip(ServletUtil.getRequestIP(request))
                    .url(request.getMethod() + ":" + request.getRequestURI())
                    .inParam(((RequestWrapper) request).getInParam())
                    .startTime(System.currentTimeMillis())
                    .username(request.getRemoteUser())
            );
        }
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        Object currentInfo = request.getAttribute(AGILE_BUSINESS_LOG);
        if (!(currentInfo instanceof ExecutionInfo.Builder)) {
            return;
        }
        ContentCachingResponseWrapper response = WebUtils.getNativeResponse(THREAD_LOCAL.get(), ContentCachingResponseWrapper.class);
        if (response != null) {
            ExecutionInfo info = ((ExecutionInfo.Builder) currentInfo)
                    .outParam(new String(response.getContentAsByteArray()))
                    .endTime(System.currentTimeMillis()).build();

            //调用钩子函数
            ObjectProvider<ExecutionObjectProvider> provider = BeanUtil.getApplicationContext().getBeanProvider(ExecutionObjectProvider.class);
            provider.orderedStream().forEach(s -> s.pass(info));

            try {
                response.copyBodyToResponse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        THREAD_LOCAL.remove();
    }
}
