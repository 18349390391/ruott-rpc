package com.ruott.rpc.server.processor;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

/**
 * 抽象装饰类 - 半包粘包处理器
 */
public abstract class VertxTcpPacketHandlerAbstract implements Handler<Buffer> {

    private final Handler<Buffer> bufferHandler;

    public VertxTcpPacketHandlerAbstract(Handler<Buffer> buffer) {
        this.bufferHandler = buffer;
    }

    @Override
    public void handle(Buffer buffer) {
        bufferHandler.handle(buffer);
    }

}
