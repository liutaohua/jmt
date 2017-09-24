package cc.tpark.connections;

import cc.tpark.commons.InnerMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

import java.util.HashMap;
import java.util.Map;

public enum SimpleConnections implements Connections {
    INSTENCE;

    private Map<String, ChannelHandlerContext> cons = new HashMap<>();

    @Override
    public void sendMsg(String ip, InnerMsg msg) {
        ChannelHandlerContext channelHandlerContext = cons.get(ip);
        if (channelHandlerContext == null) {
            return;
        }

        MqttFixedHeader header = new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader(msg.getTopic(), -1);
//        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
//        buffer.writeBytes("aaa".getBytes());

        channelHandlerContext.writeAndFlush(new MqttPublishMessage(header, mqttPublishVariableHeader, (ByteBuf) msg.getMsg()));
        System.out.println("send msg [ ip: " + ip + " msg:" + msg.getMsg() + "]");
    }

    public void addConnect(String ip, ChannelHandlerContext ctx) {
        cons.put(ip, ctx);
    }

    public void removeConnect(String ip) {
        cons.remove(ip);
    }
}
