package org.ruott.rpcstarter.config.retry;


import com.ruott.rpc.retry.FixedTimeRetryParams;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 固定时间重试
 */

@Data
@Component
@ConfigurationProperties(prefix = "ruott.rpc.retry-config.fixed-time-retry")
public class FixedTimeRetry {

    //重试次数
    private Integer retryNumber = FixedTimeRetryParams.RETRY_NUMBER;

    //重试时间
    private Integer retryTimes = FixedTimeRetryParams.RETRY_TIMES;

}
