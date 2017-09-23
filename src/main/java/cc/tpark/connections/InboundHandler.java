package cc.tpark.connections;

import cc.tpark.router.Router;
import cc.tpark.router.SimpleRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttSubscribePayload;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;

import java.util.List;

public class InboundHandler extends SimpleChannelInboundHandler<MqttMessage> {
    Router router = new SimpleRouter(SimpleConnections.INSTENCE);

    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        String id = ctx.channel().id().asLongText();
        SimpleConnections.INSTENCE.addConnect(id, ctx);

        MqttMessageType mqttMessageType = msg.fixedHeader().messageType();
        if (mqttMessageType == MqttMessageType.CONNECT) {
            // todo: 发送回复
            System.out.println("发送回复");
        } else if (mqttMessageType == MqttMessageType.SUBSCRIBE) {
            // todo: 订阅方法
            // todo: 回复订阅结果
            MqttSubscribePayload payload = (MqttSubscribePayload) msg.payload();
            List<MqttTopicSubscription> mqttTopicSubscriptions = payload.topicSubscriptions();
            for (MqttTopicSubscription topic : mqttTopicSubscriptions) {
                router.subscribe(topic.topicName(), id);
            }
            System.out.println("订阅方法");
        } else if (mqttMessageType == MqttMessageType.UNSUBSCRIBE) {
            // todo: 退订方法
            // todo: 回复退订结果
            System.out.println("退订方法");
        } else if (mqttMessageType == MqttMessageType.PUBLISH) {
            // todo: 发布信息方法
            System.out.println("发布信息方法");
        }
        System.out.println(msg.toString());
    }

//    private void subscribe(InetSocketAddress insocket, MqttMessage msg) {
//        MqttSubscribePayload payload = (MqttSubscribePayload) msg.payload();
//        List<MqttTopicSubscription> mqttTopicSubscriptions = payload.topicSubscriptions();
//        for (MqttTopicSubscription mts : mqttTopicSubscriptions) {
//            String topicName = mts.topicName();
//            //            MqttQoS mqttQoS = mts.qualityOfService();
//            //            router.subscribe(topicName,);
//        }
//    }
}
