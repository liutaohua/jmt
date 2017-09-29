package cc.tpark.session.protocol;

import cc.tpark.session.protocol.mqtt.MqttAction;
import cc.tpark.session.protocol.mqtt.MqttMsgElement;
import io.netty.handler.codec.mqtt.*;

public class MessageVistor implements IVistor {
    private final IJMSAction action = new MqttAction();

    @Override
    public void visit(MqttMsgElement msgElement) {
        MqttMessage msg = msgElement.getMsg();
        String id = msgElement.getId();
        MqttFixedHeader header = msg.fixedHeader();
        if (header == null) {
            return;
        }
        MqttMessageType mqttMessageType = header.messageType();
        switch (mqttMessageType) {
            case CONNECT:
                action.connection(id, (MqttConnectMessage) msg);
                break;
            case SUBSCRIBE:
                action.subscribe(id, (MqttSubscribeMessage) msg);
                break;
            case UNSUBSCRIBE:
                action.unsubscribe(id, (MqttUnsubscribeMessage) msg);
                break;
            case PUBLISH:
                action.publish(id, (MqttPublishMessage) msg);
                break;
            case DISCONNECT:
                action.disconnect(id, (MqttConnectMessage) msg);
                break;
            case PINGREQ:
                action.pingreq(id);
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
