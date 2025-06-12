package org.ruott.rpcstarter.config.retry;

import com.ruott.rpc.retry.RetryStrategyKeys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 重试配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "ruott.rpc.retry-config")
public class RetryConfig {

    //是否开起重试
    private String strategy = RetryStrategyKeys.NO;

    //固定时间重试
    private FixedTimeRetry fixedTimeRetry = new FixedTimeRetry();

}
