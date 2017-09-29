package cc.tpark.actor.manager;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Kill;
import akka.actor.Props;
import cc.tpark.actor.router.TopicRouter;
import io.netty.handler.codec.mqtt.MqttMessage;
import scala.Option;

public class TopicManager extends AbstractActor {
    public static final String TopicPath = "akka://jmt/user/topics/";

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateTopic.class, (topic) -> {
                    if (getContext().child(topic.topic).isEmpty()) {
                        ActorRef actorRef = getContext().actorOf(Props.create(TopicRouter.class), topic.topic);
                        getContext().watch(actorRef);
                    }
                }).match(DeleteTopic.class, (topic) -> {
                    Option<ActorRef> actorRef = getContext().child(topic.topic);
                    if (!actorRef.isEmpty()) {
                        actorRef.get().tell(Kill.getInstance(), getSelf());
                        getContext().unwatch(actorRef.get());
                    }
                }).match(PubMessage.class, pubMessage -> {
                    Option<ActorRef> actorRef = getContext().child(pubMessage.topic);
                    if (!actorRef.isEmpty()) {
                        System.out.println("发布消息到" + actorRef.get().path());
                        actorRef.get().tell(pubMessage.msg, getSelf());
                    }
                })
                .build();
    }

    public static class CreateTopic {
        private String topic;

        public static CreateTopic getInstence(String topic) {
            return new CreateTopic(topic);
        }

        private CreateTopic(String topic) {
            this.topic = topic;
        }
    }

    public static class DeleteTopic {
        private String topic;

        public static DeleteTopic getInstence(String topic) {
            return new DeleteTopic(topic);
        }

        public DeleteTopic(String topic) {
            this.topic = topic;
        }
    }

    public static class PubMessage {
        private String topic;
        private MqttMessage msg;


        public static PubMessage getInstence(String topic, MqttMessage msg) {
            return new PubMessage(topic, msg);
        }

        private PubMessage(String topic, MqttMessage msg) {
            this.topic = topic;
            this.msg = msg;
        }
    }
}
