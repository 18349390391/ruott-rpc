package com.ruott.rpc.server;


import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.extern.slf4j.Slf4j;

/**
 * Vert.x TCP连接
 */

@Slf4j
public class VertxTcpServer implements Server {
    @Override
    public void doService(int port) {
        //获取实例
        Vertx vertx = Vertx.vertx();

        //获取服务端
        NetServer server = vertx.createNetServer();

        //创建连接
        server.connectHandler(new VertxTcpServerHandler());

        //启动tcp监听端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                log.info("http server started on port {}", port);
            } else {
                log.error("http server start failed", result.cause());
            }
        });
    }
}
