package cc.tpark.connections;

import cc.tpark.commons.InnerMsg;
import io.netty.channel.ChannelHandlerContext;

public interface Connections {
    /**
     * 向ip发送消息
     *
     * @param ip
     * @param msg
     */
    void sendMsg(String ip, InnerMsg msg);

    /**
     * 新增连接
     *
     * @param ip
     * @param ctx
     */
    boolean addConnect(String ip, ChannelHandlerContext ctx);

    /**
     * 删除连接
     *
     * @param ip
     */
    void removeConnect(String ip);

}
