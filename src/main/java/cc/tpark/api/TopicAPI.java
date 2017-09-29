package cc.tpark.api;

import io.netty.handler.codec.mqtt.MqttMessage;

public interface TopicAPI {
    boolean createTopic(String topic);

    boolean delTopic(String topic);

    void getTopics();

    void containsTopic(String topic);


    boolean publishMsg(String topic, MqttMessage msg);
}
