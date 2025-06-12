package com.ruott.rpc.retry;

import com.ruott.rpc.spi.SpiLoader;

/**
 * 重试工厂
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    public static RetryStrategy getInstance(String retryKey) {
        return SpiLoader.getInstance(retryKey, RetryStrategy.class);
    }
}
