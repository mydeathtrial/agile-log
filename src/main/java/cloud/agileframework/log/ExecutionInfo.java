package cloud.agileframework.log;

import java.util.Map;

/**
 * @author 佟盟
 * 日期 2019/5/7 14:50
 * 描述 请求信息
 * @version 1.0
 * @since 1.0
 */
public class ExecutionInfo {
    private static final String ANONYMOUS = "anonymous";
    private final String ip;
    private final String url;
    private final String username;
    private final long startTime;
    private final long endTime;
    private final Map<String, Object> inParam;
    private final String outParam;

    public ExecutionInfo(Builder builder) {
        ip = builder.ip;
        url = builder.url;
        username = builder.username;
        startTime = builder.startTime;
        endTime = builder.endTime;
        inParam = builder.inParam;
        outParam = builder.outParam;

    }

    public static class Builder {
        private String ip;
        private String url;
        private long startTime;
        private String username;
        private long endTime;
        private Map<String, Object> inParam;
        private String outParam;

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder startTime(long startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder endTime(long endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder inParam(Map<String, Object> inParam) {
            this.inParam = inParam;
            return this;
        }

        public Builder outParam(String outParam) {
            this.outParam = outParam;
            return this;
        }

        public String getIp() {
            return ip;
        }

        public String getUrl() {
            return url;
        }

        public long getStartTime() {
            return startTime;
        }

        public String getUsername() {
            return username;
        }

        public long getEndTime() {
            return endTime;
        }

        public Map<String, Object> getInParam() {
            return inParam;
        }

        public String getOutParam() {
            return outParam;
        }

        public ExecutionInfo build() {
            return new ExecutionInfo(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getIp() {
        return ip;
    }

    public String getUrl() {
        return url;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getUsername() {
        return username == null ? ANONYMOUS : username;
    }

    public long getEndTime() {
        return endTime;
    }

    public Map<String, Object> getInParam() {
        return inParam;
    }

    public String getOutParam() {
        return outParam;
    }

    @Override
    public String toString() {
        return "ExecutionInfo{" +
                "ip='" + ip + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", inParam=" + inParam +
                ", outParam='" + outParam + '\'' +
                '}';
    }
}
