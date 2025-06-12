package com.ruott.rpc.server.processor;

import com.ruott.rpc.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * 具体装饰者 - 半包粘包处理器
 */
public class VertxTcpPacketHandler extends VertxTcpPacketHandlerAbstract {

    private final RecordParser recordParser;

    public VertxTcpPacketHandler(Handler<Buffer> buffer) {
        super(buffer);
        this.recordParser = this.initRecordParser(buffer);
    }
    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        //先读取请求头
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.PROTOCOL_HEAD_LENGTH);
        parser.setOutput(new Handler<Buffer>() {
            //定义下标
            int size = -1;
            //全部数据
            Buffer resultBuffer = Buffer.buffer();

            //接收数据包
            @Override
            public void handle(Buffer buffer) {
                if(size == -1){
                    //获取数据长度
                    size = buffer.getInt(ProtocolConstant.DATA_LENGTH_ADDRESS);
                    //读取指定长度
                    parser.fixedSizeMode(size);
                    //存入请求头数据
                    resultBuffer.appendBuffer(buffer);
                } else {
                    //存入请求体
                    resultBuffer.appendBuffer(buffer);

                    //将数据传递出去
                    bufferHandler.handle(resultBuffer);
                    //重制一轮
                    size = -1;
                    parser.fixedSizeMode(ProtocolConstant.PROTOCOL_HEAD_LENGTH);
                    resultBuffer = Buffer.buffer();
                }
            }
        });

        return parser;

    }


}
