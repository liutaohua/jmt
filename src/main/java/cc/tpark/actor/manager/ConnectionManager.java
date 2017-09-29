package cc.tpark.actor.manager;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import cc.tpark.actor.Client;
import cc.tpark.actor.router.TopicRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import scala.Option;

public class ConnectionManager extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AddConnection.class, addConnection -> {
                    ActorRef actorRef = getContext().actorOf(Client.props(addConnection.ctx), addConnection.id);
                    getContext().watch(actorRef);
                    getSender().tell(true, getSelf());
                }).match(SendMessage.class, sendMessage -> {
                    Option<ActorRef> child = getContext().child(sendMessage.id);
                    if (!child.isEmpty()) {
                        child.get().tell(sendMessage.msg, getSelf());
                        getSender().tell(true, getSelf());
                    }
                }).match(DelConnection.class, delConnection -> {
                    Option<ActorRef> child = getContext().child(delConnection.id);
                    if (!child.isEmpty()) {
                        getContext().stop(child.get());
                        getContext().unwatch(child.get());
                    }
                }).match(SubTopic.class, subTopic -> {
                    Option<ActorRef> child = getContext().child(subTopic.id);
                    if (!child.isEmpty()) {
                        ActorSelection topic = getContext().system().actorSelection(TopicManager.TopicPath + subTopic.topicName);
                        topic.tell(TopicRouter.AddClient.getInstence(child.get()), getSelf());
                    }
                }).match(IsAlive.class, isAlive -> {
                    Option<ActorRef> child = getContext().child(isAlive.id);
                    if (!child.isEmpty()) {
                        getSender().tell(true, getSelf());
                    } else {
                        getSender().tell(false, getSelf());
                    }
                })
                .build();
    }

    public static class AddConnection {
        private String id;
        private ChannelHandlerContext ctx;

        public static AddConnection getInstence(String id, ChannelHandlerContext ctx) {
            return new AddConnection(id, ctx);
        }

        private AddConnection(String id, ChannelHandlerContext ctx) {
            this.id = id;
            this.ctx = ctx;
        }
    }

    public static class DelConnection {
        private String id;
        private ChannelHandlerContext ctx;

        public static DelConnection getInstence(String id) {
            return new DelConnection(id);
        }

        private DelConnection(String id) {
            this.id = id;
        }
    }

    public static class SubTopic {
        private String id;
        private String topicName;

        private SubTopic(String id, String topicName) {
            this.id = id;
            this.topicName = topicName;
        }

        public static SubTopic getInstence(String id, String topicName) {
            return new SubTopic(id, topicName);
        }
    }

    public static class SendMessage {
        private String id;
        private MqttMessage msg;

        private SendMessage(String id, MqttMessage msg) {
            this.id = id;
            this.msg = msg;
        }

        public static SendMessage getInstence(String id, MqttMessage msg) {
            return new SendMessage(id, msg);
        }
    }

    public static class IsAlive {
        private String id;

        private IsAlive(String id) {
            this.id = id;
        }

        public static IsAlive getInstence(String id) {
            return new IsAlive(id);
        }
    }
}
