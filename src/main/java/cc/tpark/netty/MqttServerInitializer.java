package cc.tpark.netty;

import cc.tpark.session.MqttInboundHander;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;


public class MqttServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(MqttEncoder.INSTANCE);
        pipeline.addLast(new MqttDecoder());

        pipeline.addLast(new MqttInboundHander());
    }
}
