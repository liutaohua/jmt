package cc.tpark.session;

import cc.tpark.actor.ActorConnAPi;
import cc.tpark.api.ConnectionAPI;
import cc.tpark.netty.NettyUtil;
import cc.tpark.session.protocol.MessageVistor;
import cc.tpark.session.protocol.mqtt.MqttMsgElement;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttMessage;

public class MqttInboundHander extends SimpleChannelInboundHandler<MqttMessage> {
    private static final MessageVistor vistor = new MessageVistor();
    private static final ConnectionAPI connApi = new ActorConnAPi();
    private final int MaxConnNum = 1000;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        String id = NettyUtil.getChannelId(ctx);
        //最大连接数限制
        int connNum = connApi.getConnNum();
        if (connNum > MaxConnNum) {
            ctx.close();
            return;
        }

        if (!connApi.createConn(id, ctx)) {
            ctx.close();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        String id = NettyUtil.getChannelId(ctx);
        MqttMsgElement mqttMsg = new MqttMsgElement(msg, id);
        mqttMsg.accept(vistor);
    }
}
