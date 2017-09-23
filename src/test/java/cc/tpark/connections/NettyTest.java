package cc.tpark.connections;

import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.mqtt.*;
import org.junit.Test;
import sun.nio.ch.DirectBuffer;

public class NettyTest {

    @Test
    public void test() {
        MqttFixedHeader header = new MqttFixedHeader(MqttMessageType.PINGREQ, false,
                MqttQoS.EXACTLY_ONCE, false, 1);
        MqttMessage mqttMessage = new MqttMessage(header);

        EmbeddedChannel channel = new EmbeddedChannel(MqttEncoder.INSTANCE);
        channel.writeOutbound(mqttMessage);
        AbstractReferenceCountedByteBuf o = channel.readOutbound();

//        EmbeddedChannel inBoundChannel  = new EmbeddedChannel(new MqttDecoder());
//
//        inBoundChannel.writeInbound(o);
//        Object o1 = inBoundChannel.readInbound();
//        System.out.println(o1);
        EmbeddedChannel handler = new EmbeddedChannel(new MqttDecoder(), new InboundHandler());
        handler.writeInbound(o);

    }
}
