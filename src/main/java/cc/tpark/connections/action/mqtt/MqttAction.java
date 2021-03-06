package cc.tpark.connections.action.mqtt;

import cc.tpark.commons.InnerMsg;
import cc.tpark.connections.SimpleConnections;
import cc.tpark.connections.action.JMSAction;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

import java.util.List;

import static io.netty.handler.codec.mqtt.MqttQoS.*;

public class MqttAction extends JMSAction {

    /**
     * 申请链接并保存
     *
     * @param ctx
     * @param msg
     */
    public void connection(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        String id = "mqtt:" + ctx.channel().id().asLongText();
        if (SimpleConnections.INSTENCE.addConnect(id, ctx)) {

            MqttFixedHeader header =
                    new MqttFixedHeader(MqttMessageType.CONNACK, false, AT_MOST_ONCE, false, 0);
            MqttConnAckVariableHeader mqttConnAckVariableHeader =
                    new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, false);
            MqttConnAckMessage mqttConnAckMessage =
                    new MqttConnAckMessage(header, mqttConnAckVariableHeader);

            ctx.channel().writeAndFlush(mqttConnAckMessage);
        } else {
            ctx.close();
            System.out.println("连接数已达上限");
        }
    }

    /**
     * 断开链接移除信息
     *
     * @param ctx
     * @param msg
     */
    public void disconnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        String id = "mqtt:" + ctx.channel().id().asLongText();
        ctx.close();
        SimpleConnections.INSTENCE.removeConnect(id);
    }

    /**
     * 订阅主题
     *
     * @param ctx
     * @param msg
     */
    public void subscribe(ChannelHandlerContext ctx, MqttSubscribeMessage msg) {
        String id = "mqtt:" + ctx.channel().id().asLongText();
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
    public void unsubscribe(ChannelHandlerContext ctx, MqttUnsubscribeMessage msg) {
        String id = "mqtt:" + ctx.channel().id().asLongText();
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
    public void publish(ChannelHandlerContext ctx, MqttPublishMessage msg) {
        String id = "mqtt:" + ctx.channel().id().asLongText();
        String topicName = msg.variableHeader().topicName();
        int messageId = msg.variableHeader().messageId();
        ByteBuf byteBuf = msg.payload();
        InnerMsg innerMsg = new InnerMsg();
        //消息来源的IP
        innerMsg.setIp(id);
        innerMsg.setTopic(topicName);
        innerMsg.setMsg(byteBuf.copy());

        MqttFixedHeader mqttFixedHeader = null;
        switch (msg.fixedHeader().qosLevel()) {
            case AT_LEAST_ONCE:
                router.publish(innerMsg);
                mqttFixedHeader =
                        new MqttFixedHeader(MqttMessageType.PUBACK, false, AT_LEAST_ONCE, false, 0);
                break;
            case EXACTLY_ONCE:
                mqttFixedHeader =
                        new MqttFixedHeader(MqttMessageType.PUBREC, false, EXACTLY_ONCE, false, 0);
                if (!msgMap.containsKey(id + "-" + messageId)) {
                    msgMap.put(id + "-" + messageId, innerMsg);
                }
                break;
            case AT_MOST_ONCE:
            default:
                router.publish(innerMsg);
        }
        if (mqttFixedHeader == null) {
            return;
        }

        MqttMessage mqttMessage = MqttMessageFactory
                .newMessage(mqttFixedHeader, MqttMessageIdVariableHeader.from(messageId), null);
        ctx.channel().writeAndFlush(mqttMessage);
    }

    /**
     * 心跳回复
     *
     * @param ctx
     */
    public void pingreq(ChannelHandlerContext ctx) {
        String id = "mqtt:" + ctx.channel().id().asLongText();
        MqttMessage mqttMessage = new MqttMessage(
                new MqttFixedHeader(MqttMessageType.PINGRESP, false, EXACTLY_ONCE, false, 0));
        ctx.writeAndFlush(mqttMessage);
        SimpleConnections.INSTENCE.restartBreaker(id);
    }

    /**
     * 发布确认（QOS == 2）
     *
     * @param ctx
     * @param msg
     */
    public void pubrel(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        String id = "mqtt:" + ctx.channel().id().asLongText();
        int messageId = ((MqttMessageIdVariableHeader) msg.variableHeader()).messageId();
        InnerMsg innerMsg = null;
        if ((innerMsg = (InnerMsg) msgMap.get(id + "-" + messageId)) != null) {
            router.publish(innerMsg);
            msgMap.remove(id + "-" + messageId);
        }
        MqttMessage mqttMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBCOMP, false, EXACTLY_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(messageId), null);
        ctx.channel().writeAndFlush(mqttMessage);
    }
}
