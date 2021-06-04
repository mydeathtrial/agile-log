package cloud.agileframework.log;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 佟盟
 * 日期 2020/8/00018 18:11
 * 描述 控制台日志提供者
 * @version 1.0
 * @since 1.0
 */
public class PrintLogProvider implements ExecutionObjectProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void pass(ExecutionInfo executionInfo) {
        logger.debug("IP  地址: {}", executionInfo.getIp());
        logger.debug("URL 地址: {}", executionInfo.getUrl());
        logger.debug("帐    号: {}", executionInfo.getUsername());
        try {
            logger.debug("入    参: {}", JSON.toJSONString(executionInfo.getInParam()));
        } catch (Exception e) {
            logger.debug("入    参: {}", "非Json数据");
        }

        try {
            logger.debug("出    参: {}", JSON.parse(executionInfo.getOutParam()));
        } catch (Exception e) {
            logger.debug("出    参: {}", "非Json数据");
        }

        logger.debug("耗    时: {}", (executionInfo.getEndTime() - executionInfo.getStartTime()) + "ms");
        logger.debug("----------------------------------------------------------");
    }
}
