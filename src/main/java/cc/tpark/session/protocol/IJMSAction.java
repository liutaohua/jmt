package cc.tpark.session.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

public interface IJMSAction {
    void connection(String id, MqttConnectMessage msg);

    void disconnect(String id, MqttConnectMessage msg);

    void subscribe(String id, MqttSubscribeMessage msg);

    void unsubscribe(String id, MqttUnsubscribeMessage msg);

    void publish(String id, MqttPublishMessage msg);

    void pingreq(String id);

    void pubrel(String id, MqttMessage msg) throws Exception;
}
