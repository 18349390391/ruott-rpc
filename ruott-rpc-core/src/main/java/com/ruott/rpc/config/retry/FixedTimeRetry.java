package com.ruott.rpc.config.retry;


import com.ruott.rpc.retry.FixedTimeRetryParams;
import lombok.Data;

/**
 * 固定时间重试
 */

@Data
public class FixedTimeRetry {

    //重试次数
    private Integer retryNumber = FixedTimeRetryParams.RETRY_NUMBER;

    //重试时间
    private Integer retryTimes = FixedTimeRetryParams.RETRY_TIMES;

}
