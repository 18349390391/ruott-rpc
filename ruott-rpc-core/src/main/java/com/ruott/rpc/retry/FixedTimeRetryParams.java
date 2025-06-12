package com.ruott.rpc.retry;

/**
 * 固定时间重试参数
 */
public interface FixedTimeRetryParams {

    //默认：重试次数
    int RETRY_NUMBER = 3;

    //默认：重试时间
    int RETRY_TIMES = 3;
}
