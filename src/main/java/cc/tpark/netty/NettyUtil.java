package cc.tpark.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

import java.util.List;

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

    public static synchronized MqttSubAckMessage getSubAckMsg(int messageId, boolean isFail) {
        MqttFixedHeader header = new MqttFixedHeader(MqttMessageType.SUBACK, false, AT_MOST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        MqttSubAckPayload mqttSubAckPayload;
        if (isFail) {
            mqttSubAckPayload = new MqttSubAckPayload(0, 0x80);
        } else {
            mqttSubAckPayload = new MqttSubAckPayload(0);
        }
        return new MqttSubAckMessage(header, variableHeader, mqttSubAckPayload);
    }

    public static synchronized MqttUnsubAckMessage getUnsubAckMsg(int messageId) {
        MqttFixedHeader header = new MqttFixedHeader(MqttMessageType.UNSUBACK, false, AT_MOST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        return new MqttUnsubAckMessage(header, variableHeader);
    }
}
