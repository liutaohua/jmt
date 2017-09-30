package cc.tpark.actor.api;

import akka.actor.ActorRef;
import cc.tpark.actor.manager.TopicManager;
import cc.tpark.api.TopicAPI;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static akka.pattern.PatternsCS.ask;

public class ActorTopicAPI implements TopicAPI {
    private final ActorRef topicManager;
    private static final int DEFAULT_TIMEOUT = 3000;

    public ActorTopicAPI(ActorRef topicManager) {
        this.topicManager = topicManager;
    }

    @Override
    public boolean createTopic(String topic) {
        CompletableFuture<Object> feture = ask(topicManager, TopicManager.CreateTopic.getInstence(topic), DEFAULT_TIMEOUT)
                .toCompletableFuture();
        boolean isOk;
        try {
            isOk = (boolean) feture.get();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
        return isOk;
    }

    @Override
    public boolean delTopic(String topic) {
        CompletableFuture<Object> feture = ask(topicManager, TopicManager.DeleteTopic.getInstence(topic), DEFAULT_TIMEOUT)
                .toCompletableFuture();
        boolean isOk;
        try {
            isOk = (boolean) feture.get();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
        return isOk;
    }

    @Override
    public void getTopics() {

    }

    @Override
    public void containsTopic(String topic) {

    }

    @Override
    public boolean publishMsg(String topic, MqttMessage msg) {
        topicManager.tell(TopicManager.PubMessage.getInstence(topic, msg), ActorRef.noSender());
//        CompletableFuture<Object> feture = ask(topicManager, TopicManager.PubMessage.getInstence(topic, msg), DEFAULT_TIMEOUT)
//                .toCompletableFuture();
//        boolean isOk;
//        try {
//            isOk = (boolean) feture.get();
//        } catch (InterruptedException | ExecutionException e) {
//            return false;
//        }
//        return isOk;
        return true;
    }
}
