package cc.tpark.session.protocol.mqtt;

import cc.tpark.actor.ActorConnAPi;
import cc.tpark.api.ConnectionAPI;
import cc.tpark.netty.NettyUtil;
import cc.tpark.session.protocol.IJMSAction;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

public class MqttAction implements IJMSAction {
    private final ConnectionAPI connectionAPI = new ActorConnAPi();

    @Override
    public void connection(String id, MqttConnectMessage msg) {

        //连接前检查
        boolean isok = connectionAPI.checkServerStatus();
        if (!isok) {
            //清理垃圾会话
            connectionAPI.delConn(id);
            //告诉客户端，我不行
            connectionAPI.sendMesssage(id, NettyUtil.getConnAckMsg(MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE, false));
            return;
        }

        //客户端要求清理会话
        if (msg.variableHeader().isCleanSession() && connectionAPI.isAlive(id)) {
            //目前不实现session管理，直接同意客户端连接

            //connectionAPI.delConn(id);
            //connectionAPI.createConn(id);
            connectionAPI.sendMesssage(id, NettyUtil.getConnAckMsg(MqttConnectReturnCode.CONNECTION_ACCEPTED, false));
            return;
        }
        //cleansession 为false，这时候判断是否缓存了session
        if (connectionAPI.isAlive(id)) {
            connectionAPI.sendMesssage(id, NettyUtil.getConnAckMsg(MqttConnectReturnCode.CONNECTION_ACCEPTED, true));
        } else {
            connectionAPI.sendMesssage(id, NettyUtil.getConnAckMsg(MqttConnectReturnCode.CONNECTION_ACCEPTED, false));
        }
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {

    }

    @Override
    public void subscribe(ChannelHandlerContext ctx, MqttSubscribeMessage msg) {

    }

    @Override
    public void unsubscribe(ChannelHandlerContext ctx, MqttUnsubscribeMessage msg) {

    }

    @Override
    public void publish(ChannelHandlerContext ctx, MqttPublishMessage msg) {

    }

    @Override
    public void pingreq(ChannelHandlerContext ctx) {

    }

    @Override
    public void pubrel(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {

    }
}
