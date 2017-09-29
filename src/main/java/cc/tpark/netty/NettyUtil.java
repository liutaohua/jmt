package cc.tpark.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

import static io.netty.handler.codec.mqtt.MqttQoS.AT_MOST_ONCE;
import static io.netty.handler.codec.mqtt.MqttQoS.EXACTLY_ONCE;

public class NettyUtil {
    public static String getChannelId(ChannelHandlerContext ctx) {
        return "mqtt:" + ctx.channel().id().asLongText();
    }

    public static synchronized MqttConnAckMessage getConnAckMsg(MqttConnectReturnCode type, boolean session) {
        MqttFixedHeader header =
                new MqttFixedHeader(MqttMessageType.CONNACK, false, AT_MOST_ONCE, false, 0);
        MqttConnAckVariableHeader mqttConnAckVariableHeader =
                new MqttConnAckVariableHeader(type, false);
        return new MqttConnAckMessage(header, mqttConnAckVariableHeader);
    }

    public static synchronized MqttMessage getHeartbeatMsg() {
        return new MqttMessage(
                new MqttFixedHeader(MqttMessageType.PINGRESP, false, EXACTLY_ONCE, false, 0));
    }
}
