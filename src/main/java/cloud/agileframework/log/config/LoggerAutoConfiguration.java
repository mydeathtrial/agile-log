package cloud.agileframework.log.config;

import cloud.agileframework.log.LogFilter;
import cloud.agileframework.log.PrintLogProvider;
import cloud.agileframework.log.RecordOperationManager;
import cloud.agileframework.log.RecordOperationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 佟盟
 * 日期 2020/8/00017 11:19
 * 描述 TODO
 * @version 1.0
 * @since 1.0
 */
@ConditionalOnProperty(value = "enabled", prefix = "agile.log", matchIfMissing = true)
@Configuration
public class LoggerAutoConfiguration implements WebMvcConfigurer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${agile.log.enabled:false}")
    private Boolean enabled;

    @Bean
    @ConditionalOnClass(LogFilter.class)
    public FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> corsFilter = new FilterRegistrationBean<>();
        corsFilter.setFilter(new LogFilter());
        corsFilter.addUrlPatterns("/*");
        corsFilter.addInitParameter("is", enabled ? "true" : "false");
        return corsFilter;
    }

    @Bean
    @Order
    public PrintLogProvider printLogProvider() {
        return new PrintLogProvider();
    }

    @Bean
    @Order(1)
    @ConditionalOnProperty(value = "enabled", prefix = "agile.log.operation")
    public RecordOperationProvider recordOperationProvider() {
        return new RecordOperationProvider();
    }

    @Bean
    @ConditionalOnMissingBean(RecordOperationManager.class)
    public RecordOperationManager recordOperationManager() {
        return (handlerMethod, executionInfo) -> logger.warn("not fount bean of RecordOperationManager");
    }

}
