package com.ruott.rpc.config.retry;

import com.ruott.rpc.retry.RetryStrategyKeys;
import lombok.Data;

/**
 * 重试配置
 */
@Data
public class RetryConfig {

    //是否开起重试
    private String strategy = RetryStrategyKeys.NO;

    //固定时间重试
    private FixedTimeRetry fixedTimeRetry = new FixedTimeRetry();

}
