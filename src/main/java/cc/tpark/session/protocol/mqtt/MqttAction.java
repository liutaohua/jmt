package cc.tpark.session.protocol.mqtt;

import cc.tpark.ApplicationContext;
import cc.tpark.api.ConnectionAPI;
import cc.tpark.api.TopicAPI;
import cc.tpark.netty.NettyUtil;
import cc.tpark.session.protocol.IJMSAction;
import io.netty.handler.codec.mqtt.*;

import java.util.List;

public class MqttAction implements IJMSAction {
    private final ConnectionAPI connectionAPI = ApplicationContext.instence.getConnectionAPI();
    private final TopicAPI topicAPI = ApplicationContext.instence.getTopicAPI();


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
            System.out.println(id + " 已经成功连接");
            return;
        }
        //cleansession 为false，这时候判断是否缓存了session
        if (connectionAPI.isAlive(id)) {
            connectionAPI.sendMesssage(id, NettyUtil.getConnAckMsg(MqttConnectReturnCode.CONNECTION_ACCEPTED, true));
        } else {
            connectionAPI.sendMesssage(id, NettyUtil.getConnAckMsg(MqttConnectReturnCode.CONNECTION_ACCEPTED, false));
        }
        System.out.println(id + " 已经成功连接");
    }

    @Override
    public void disconnect(String id, MqttConnectMessage msg) {
        connectionAPI.delConn(id);
        System.out.println(id + " 已经成功断开连接");
    }

    @Override
    public void subscribe(String id, MqttSubscribeMessage msg) {
        int messageId = msg.variableHeader().messageId();
        List<MqttTopicSubscription> topics = msg.payload().topicSubscriptions();
        boolean isFail = false;
        for (MqttTopicSubscription t : topics) {
            if (!topicAPI.createTopic(t.topicName())) {
                isFail = true;
                break;
            }
        }
        if (isFail) {
            connectionAPI.sendMesssage(id, NettyUtil.getSubAckMsg(messageId, isFail));
            System.out.println(id + " 订阅失败... ");
            return;
        }
        String topicLString = "";
        for (MqttTopicSubscription t : topics) {
            connectionAPI.subTopic(id, t.topicName());
            topicLString += t.toString() + ", ";
        }
        connectionAPI.sendMesssage(id, NettyUtil.getSubAckMsg(messageId, false));
        System.out.println(id + " 已订阅 " + topicLString.substring(1, topicLString.length() - 1));
    }

    @Override
    public void unsubscribe(String id, MqttUnsubscribeMessage msg) {
        int messageId = msg.variableHeader().messageId();
        List<String> topics = msg.payload().topics();
        for (String t : topics) {
            connectionAPI.unsubTopic(id, t);
        }
        connectionAPI.sendMesssage(id, NettyUtil.getUnsubAckMsg(messageId));
        System.out.println(id + " 已取消订阅 ");
    }

    @Override
    public void publish(String id, MqttPublishMessage msg) {
        String topic = msg.variableHeader().topicName();
        if (topicAPI.publishMsg(topic, msg.retain())) {
            System.out.println("已经成功向[" + topic + "] 发送消息");
        } else {
            System.out.println("向[" + topic + "] 发送消息失败");
        }
    }

    @Override
    public void pingreq(String id) {
        connectionAPI.sendMesssage(id, NettyUtil.getHeartbeatMsg());
        System.out.println(id + " 心跳一次");
    }

    @Override
    public void pubrel(String id, MqttMessage msg) throws Exception {

    }
}
