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

    private String is;

    public String getIs() {
        return is;
    }

    public void setIs(String is) {
        this.is = is;
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return "true".equalsIgnoreCase(is);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean shouldLog = shouldLog(request);
        if (!shouldLog) {
            filterChain.doFilter(request, response);
            return;
        }
        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        HttpServletResponse responseToUse = response;

        if (isFirstRequest && !(request instanceof RequestWrapper)) {
            requestToUse = new RequestWrapper(request);
            beforeRequest((RequestWrapper) requestToUse);
        }

        if (!(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }


        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {

            if (!isAsyncStarted(request)) {
                afterRequest((RequestWrapper) requestToUse, (ContentCachingResponseWrapper) responseToUse);
            }
            ((ContentCachingResponseWrapper) responseToUse).copyBodyToResponse();
        }
    }

    protected void beforeRequest(RequestWrapper request) {
        request = WebUtils.getNativeRequest(request, RequestWrapper.class);

        if (request != null) {
            request.setAttribute(AGILE_BUSINESS_LOG, ExecutionInfo.builder()
                    .ip(ServletUtil.getRequestIP(request))
                    .url(request.getMethod() + ":" + request.getRequestURI())
                    .inParam(request.getInParam())
                    .startTime(System.currentTimeMillis())
                    .username(request.getRemoteUser())
            );
        }
    }

    protected void afterRequest(RequestWrapper request, ContentCachingResponseWrapper response) {
        Object currentInfo = request.getAttribute(AGILE_BUSINESS_LOG);
        if (!(currentInfo instanceof ExecutionInfo.Builder)) {
            return;
        }
        if (response != null) {
            ExecutionInfo info = ((ExecutionInfo.Builder) currentInfo)
                    .outParam(new String(response.getContentAsByteArray()))
                    .endTime(System.currentTimeMillis()).build();

            //调用钩子函数
            ObjectProvider<ExecutionObjectProvider> provider = BeanUtil.getApplicationContext().getBeanProvider(ExecutionObjectProvider.class);
            provider.orderedStream().forEach(s -> s.pass(info));
        }
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
    }
}
