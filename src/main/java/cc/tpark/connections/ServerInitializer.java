package cc.tpark.connections;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;


public class ServerInitializer extends ChannelInitializer<SocketChannel> {
//    private final static int readerIdleTimeSeconds = 40;//读操作空闲30秒
//    private final static int writerIdleTimeSeconds = 50;//写操作空闲60秒
//    private final static int allIdleTimeSeconds = 100;//读写全部空闲100秒

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(MqttEncoder.INSTANCE);
        pipeline.addLast(new MqttDecoder());

        pipeline.addLast(new InboundHandler());
//        pipeline.addLast(new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds));
    }
}
