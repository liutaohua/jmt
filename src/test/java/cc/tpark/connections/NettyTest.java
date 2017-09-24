package cc.tpark.connections;

import cc.tpark.connections.handler.mqtt.MqttInboundHandler;
import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.mqtt.*;
import org.junit.Test;

import java.util.ArrayList;

public class NettyTest {

    @Test
    public void test() {
        MqttFixedHeader header = new MqttFixedHeader(MqttMessageType.SUBSCRIBE, false,
                MqttQoS.AT_LEAST_ONCE, false, 1);

        ArrayList<MqttTopicSubscription> mqttTopicSubscriptions = new ArrayList<>();
        MqttTopicSubscription ms = new MqttTopicSubscription("aaa", MqttQoS.AT_LEAST_ONCE);
        mqttTopicSubscriptions.add(ms);

        MqttSubscribeMessage mqttMessage = new MqttSubscribeMessage(header, MqttMessageIdVariableHeader.from(11), new MqttSubscribePayload(mqttTopicSubscriptions));
        EmbeddedChannel channel = new EmbeddedChannel(MqttEncoder.INSTANCE);
        channel.writeOutbound(mqttMessage);
        AbstractReferenceCountedByteBuf o = channel.readOutbound();

//        EmbeddedChannel inBoundChannel  = new EmbeddedChannel(new MqttDecoder());
//
//        inBoundChannel.writeInbound(o);
//        Object o1 = inBoundChannel.readInbound();
//        System.out.println(o1);
        EmbeddedChannel handler = new EmbeddedChannel(new MqttDecoder(), new MqttInboundHandler());
        handler.writeInbound(o);

    }
}
