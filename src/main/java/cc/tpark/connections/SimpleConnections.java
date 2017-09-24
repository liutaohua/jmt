package cc.tpark.connections;

import cc.tpark.commons.InnerMsg;
import io.netty.channel.ChannelHandlerContext;

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
        System.out.println("send msg [ ip: " + ip + " msg:" + msg.getMsg() + "]");
    }

    public void addConnect(String ip, ChannelHandlerContext ctx) {
        cons.put(ip, ctx);
    }

    public void removeConnect(String ip){
        cons.remove(ip);
    }
}
