package com.ruott.rpc.retry;

import com.ruott.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重拾策略 - 不重试
 */

public class NoRetryStrategy implements RetryStrategy {

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
