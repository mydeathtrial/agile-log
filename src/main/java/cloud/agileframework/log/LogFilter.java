package cloud.agileframework.log;

import cloud.agileframework.common.constant.Constant;
import cloud.agileframework.spring.util.BeanUtil;
import cloud.agileframework.spring.util.RequestWrapper;
import cloud.agileframework.spring.util.SecurityUtil;
import cloud.agileframework.spring.util.ServletUtil;
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
        HttpServletResponse responseToUse;

        if (isFirstRequest && !(request instanceof RequestWrapper)) {
            requestToUse = new RequestWrapper(request);
            beforeRequest((RequestWrapper) requestToUse);
        }

        responseToUse = init(response);

        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {

            if (!isAsyncStarted(request)) {
                afterRequest(requestToUse, responseToUse);
            }
            ContentCachingResponseWrapper nativeResponse = WebUtils.getNativeResponse(responseToUse, ContentCachingResponseWrapper.class);
            if (nativeResponse != null) {
                nativeResponse.copyBodyToResponse();
            }
        }
    }

    /**
     * 如果没包装过，新增包装
     */
    private HttpServletResponse init(HttpServletResponse response) {
        ContentCachingResponseWrapper temp = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (temp == null) {
            return new ContentCachingResponseWrapper(response);
        }
        return response;
    }

    protected void beforeRequest(RequestWrapper request) {
        request = WebUtils.getNativeRequest(request, RequestWrapper.class);

        if (request != null) {
            request.setAttribute(AGILE_BUSINESS_LOG, ExecutionInfo.builder()
                    .ip(ServletUtil.getRequestIP(request))
                    .method(request.getMethod())
                    .url(request.getRequestURI())
                    .inParam(request.getInParam())
                    .startTime(System.currentTimeMillis())
            );
        }
    }

    protected void afterRequest(HttpServletRequest request, HttpServletResponse response) {
        Object currentInfo = request.getAttribute(AGILE_BUSINESS_LOG);
        if (!(currentInfo instanceof ExecutionInfo.Builder)) {
            return;
        }
        ContentCachingResponseWrapper contentCachingResponseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (contentCachingResponseWrapper != null) {
            Exception e = (Exception) request.getAttribute(WebUtils.ERROR_EXCEPTION_TYPE_ATTRIBUTE);
            if (e == null) {
                e = (Exception) request.getAttribute(Constant.RequestAttributeAbout.ERROR_EXCEPTION);
            }


            String username = null;
            try {
                Class.forName("org.springframework.security.core.userdetails.UserDetails");
                username = SecurityUtil.currentUsername();
            } catch (Exception ignored) {
            }
            ExecutionInfo info = ((ExecutionInfo.Builder) currentInfo)
//                    .outParam(new String(contentCachingResponseWrapper.getContentAsByteArray()))
                    .endTime(System.currentTimeMillis())
                    .username(username)
                    .e(e)
                    .build();

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
