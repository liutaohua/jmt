package cc.tpark.session.protocol.mqtt;

import cc.tpark.session.protocol.IMsgElement;
import cc.tpark.session.protocol.IVistor;
import io.netty.handler.codec.mqtt.MqttMessage;

public class MqttMsgElement implements IMsgElement {
    private MqttMessage msg;
    private String id;

    public MqttMsgElement(MqttMessage msg, String id) {
        this.msg = msg;
        this.id = id;
    }

    public MqttMessage getMsg() {
        return msg;
    }

    public String getId() {
        return id;
    }

    @Override
    public void accept(IVistor vistor) {
        vistor.visit(this);
    }

//    CONNECT(1),
//
//    CONNACK(2),
//
//    PUBLISH(3),
//
//    PUBACK(4),
//
//    PUBREC(5),
//
//    PUBREL(6),
//
//    PUBCOMP(7),
//
//    SUBSCRIBE(8),
//
//    SUBACK(9),
//
//    UNSUBSCRIBE(10),
//
//    UNSUBACK(11),
//
//    PINGREQ(12),
//
//    PINGRESP(13),
//
//    DISCONNECT(14);

}
