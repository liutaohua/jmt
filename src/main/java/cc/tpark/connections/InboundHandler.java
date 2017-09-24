package cc.tpark.connections;

import cc.tpark.commons.InnerMsg;
import cc.tpark.router.Router;
import cc.tpark.router.SimpleRouter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;

import java.util.List;

import static io.netty.handler.codec.mqtt.MqttQoS.AT_MOST_ONCE;

public class InboundHandler extends SimpleChannelInboundHandler<MqttMessage> {
    Router router = new SimpleRouter(SimpleConnections.INSTENCE);


//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        super.userEventTriggered(ctx, evt);
//        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state() == IdleState.WRITER_IDLE) {
//                MqttMessage mqttMessage = new MqttMessage(
//                        new MqttFixedHeader(MqttMessageType.PINGRESP, false,
//                                MqttQoS.EXACTLY_ONCE, false, 0));
//
//                ctx.writeAndFlush(mqttMessage);
//            }
////            if (event.state() == IdleState.READER_IDLE)
////                System.out.println("read idle");
////            else if (event.state() == IdleState.WRITER_IDLE)
////                System.out.println("write idle");
////            else if (event.state() == IdleState.ALL_IDLE)
////                System.out.println("all idle");
//        }
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        MqttMessageType mqttMessageType = msg.fixedHeader().messageType();
        switch (mqttMessageType) {
            case CONNECT:
                // todo: 发送回复
                connection(ctx, (MqttConnectMessage) msg);
                System.out.println("发送回复");
                break;
            case SUBSCRIBE:
                // todo: 订阅方法
                // todo: 回复订阅结果
                subscribe(ctx, (MqttSubscribeMessage) msg);
                System.out.println("成功订阅");
                break;
            case UNSUBSCRIBE:
                // todo: 退订方法
                // todo: 回复退订结果
                unsubscribe(ctx, (MqttUnsubscribeMessage) msg);
                System.out.println("退订方法");
                break;
            case PUBLISH:
                // todo: 发布信息方法
                publish(ctx, (MqttPublishMessage) msg);
                System.out.println("发布信息方法");
                break;
            case DISCONNECT:
                // todo: 发布信息方法
                disconnect(ctx, (MqttConnectMessage) msg);
                System.out.println("发布信息方法");
                break;
            case PINGREQ:
                MqttMessage mqttMessage = new MqttMessage(
                        new MqttFixedHeader(MqttMessageType.PINGRESP, false,
                                MqttQoS.EXACTLY_ONCE, false, 0));

                ctx.writeAndFlush(mqttMessage);
                System.out.println("回复心跳");
                break;
            default:
                System.out.println(mqttMessageType);
                break;
        }
    }

    /**
     * 申请链接并保存
     *
     * @param ctx
     * @param msg
     */
    private void connection(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        String id = ctx.channel().id().asLongText();
        SimpleConnections.INSTENCE.addConnect(id, ctx);

        MqttFixedHeader header =
                new MqttFixedHeader(MqttMessageType.CONNACK, false, AT_MOST_ONCE, false, 0);
        MqttConnAckVariableHeader mqttConnAckVariableHeader =
                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, false);
        MqttConnAckMessage mqttConnAckMessage =
                new MqttConnAckMessage(header, mqttConnAckVariableHeader);

        ctx.channel().writeAndFlush(mqttConnAckMessage);
    }

    /**
     * 断开链接移除信息
     *
     * @param ctx
     * @param msg
     */
    private void disconnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        String id = ctx.channel().id().asLongText();
        SimpleConnections.INSTENCE.removeConnect(id);
    }

    /**
     * 订阅主题
     *
     * @param ctx
     * @param msg
     */
    private void subscribe(ChannelHandlerContext ctx, MqttSubscribeMessage msg) {
        String id = ctx.channel().id().asLongText();
        MqttSubscribePayload payload = msg.payload();
        List<MqttTopicSubscription> mqttTopicSubscriptions = payload.topicSubscriptions();
        int messageId = msg.variableHeader().messageId();
        for (MqttTopicSubscription mts : mqttTopicSubscriptions) {
            String topicName = mts.topicName();
            //            MqttQoS mqttQoS = mts.qualityOfService();
            router.subscribe(topicName, id);
        }

        //todo: modify suback (the MqttSubAckPayload )
        MqttMessage mqttSubackMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.SUBACK, false, AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(messageId), new MqttSubAckPayload(0));

        ctx.channel().writeAndFlush(mqttSubackMessage);
    }

    /**
     * 退订
     *
     * @param ctx
     * @param msg
     */
    private void unsubscribe(ChannelHandlerContext ctx, MqttUnsubscribeMessage msg) {
        String id = ctx.channel().id().asLongText();
        MqttUnsubscribePayload payload = msg.payload();
        List<String> unsubTopics = payload.topics();
        int messageId = msg.variableHeader().messageId();
        for (String unsubTopic : unsubTopics) {
            //            MqttQoS mqttQoS = mts.qualityOfService();
            router.desubscribe(unsubTopic, id);
        }

        MqttMessage mqttMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.UNSUBACK, false, AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(messageId),
                new MqttUnsubscribePayload(unsubTopics));

        ctx.channel().writeAndFlush(mqttMessage);
    }

    /**
     * 发布消息
     *
     * @param ctx
     * @param msg
     */
    private void publish(ChannelHandlerContext ctx, MqttPublishMessage msg) {
        String id = ctx.channel().id().asLongText();
        String topicName = msg.variableHeader().topicName();
        int messageId = msg.variableHeader().messageId();
        ByteBuf byteBuf = msg.payload();
        InnerMsg innerMsg = new InnerMsg();
        //消息来源的IP
        innerMsg.setIp(id);
        innerMsg.setTopic(topicName);
        innerMsg.setMsg(byteBuf.copy());

        router.publish(innerMsg);
        if (msg.fixedHeader().qosLevel() != MqttQoS.AT_MOST_ONCE) {
            MqttMessage mqttMessage = MqttMessageFactory.newMessage(
                    new MqttFixedHeader(MqttMessageType.PUBACK, false, AT_MOST_ONCE, false, 0),
                    MqttMessageIdVariableHeader.from(messageId), null);

            ctx.channel().writeAndFlush(mqttMessage);
        }
    }
}
