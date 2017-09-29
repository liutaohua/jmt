package cc.tpark.session.protocol;

import cc.tpark.session.protocol.mqtt.MqttAction;
import cc.tpark.session.protocol.mqtt.MqttMsgElement;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;

public class MessageVistor implements IVistor {
    private final IJMSAction action = new MqttAction();

    @Override
    public void visit(MqttMsgElement msgElement) {
        MqttMessage msg = msgElement.getMsg();
        String id = msgElement.getId();
        MqttMessageType mqttMessageType = msg.fixedHeader().messageType();
        switch (mqttMessageType) {
            case CONNECT:
                System.out.println("连接成功");
                action.connection(id, (MqttConnectMessage) msg);
                break;
            case SUBSCRIBE:
                System.out.println("成功订阅");
                break;
            case UNSUBSCRIBE:
                System.out.println("退订方法");
                break;
            case PUBLISH:
                System.out.println("发布信息方法");
                break;
            case DISCONNECT:
                System.out.println("发布信息方法");
                break;
            case PINGREQ:
                System.out.println("回复心跳");
                break;
            case PUBREL:
                System.out.println("PUBREL回复");
                break;
            default:
                System.out.println(mqttMessageType);
                break;
        }
    }

}
