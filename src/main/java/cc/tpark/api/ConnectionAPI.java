package cc.tpark.api;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttMessage;

public interface ConnectionAPI {
    boolean createConn(String id, ChannelHandlerContext ctx);

    int getConnNum();

    void sendMesssage(String id, MqttMessage mqttMessage);

    boolean isAlive(String id);

    void delConn(String id);

    boolean checkServerStatus();
}
