package cc.tpark.connections;

import cc.tpark.commons.InnerMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.sleep;

public enum SimpleConnections implements Connections {
    INSTENCE;

    private Map<String, ChannelHandlerContext> cons = new ConcurrentHashMap<>();

    private Map<String, Thread> breakers = new ConcurrentHashMap<>();

    @Override
    public void sendMsg(String ip, InnerMsg msg) {
        ChannelHandlerContext channelHandlerContext = cons.get(ip);
        if (channelHandlerContext == null) {
            return;
        }

        MqttFixedHeader header =
                new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttPublishVariableHeader mqttPublishVariableHeader =
                new MqttPublishVariableHeader(msg.getTopic(), -1);
        //        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        //        buffer.writeBytes("aaa".getBytes());

        channelHandlerContext.writeAndFlush(
                new MqttPublishMessage(header, mqttPublishVariableHeader, (ByteBuf) msg.getMsg()));
        System.out.println("send msg [ ip: " + ip + " msg:" + msg.getMsg() + "]");
    }

    public void addConnect(String ip, ChannelHandlerContext ctx) {
        cons.put(ip, ctx);
        createBreaker(ip);
    }

    public void removeConnect(String ip) {
        cons.remove(ip);
    }

    private void createBreaker(String ip) {

        Thread breaker = new Thread(() -> {
            int status = 0;
            while (status == 0) {
                status = 2;
                try {
                    sleep(60 * 1000);
                } catch (InterruptedException e) {
                    status = 0;
//                    System.out.println("breaker is restart");
                }
            }
            cons.get(ip).close();
            cons.remove(ip);
            breakers.remove(ip);
//            System.out.println("break");
        });
        breakers.put(ip, breaker);
        breaker.start();
    }

    public void restartBreaker(String ip) {
        breakers.get(ip).interrupt();
    }
}
