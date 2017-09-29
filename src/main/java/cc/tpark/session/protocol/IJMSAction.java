package cc.tpark.session.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

public interface IJMSAction {
    void connection(String id, MqttConnectMessage msg);

    void disconnect(ChannelHandlerContext ctx, MqttConnectMessage msg);

    void subscribe(ChannelHandlerContext ctx, MqttSubscribeMessage msg);

    void unsubscribe(ChannelHandlerContext ctx, MqttUnsubscribeMessage msg);

    void publish(ChannelHandlerContext ctx, MqttPublishMessage msg);

    void pingreq(String id);

    void pubrel(ChannelHandlerContext ctx, MqttMessage msg) throws Exception;
}
