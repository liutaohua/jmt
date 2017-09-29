package cc.tpark.api;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;

public interface ConnectionAPI {
    boolean createConn(String id, ChannelHandlerContext ctx);

    int getConnNum();

    void sendMesssage(String id, MqttConnAckMessage mqttConnAckMessage);

    boolean isAlive(String id);

    void delConn(String id);

    boolean checkServerStatus();
}