package cc.tpark.session.protocol;

import cc.tpark.session.protocol.mqtt.MqttMsgElement;

public interface IVistor {
    void visit(MqttMsgElement msg);
}
