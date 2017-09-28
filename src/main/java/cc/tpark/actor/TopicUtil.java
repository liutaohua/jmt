package cc.tpark.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class TopicUtil extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateTopic.class, (topic) -> {
                    if (getContext().child(topic.topic).isEmpty()) {
                        ActorRef actorRef = getContext().actorOf(Props.create(Topic.class), topic.topic);
                        getContext().watch(actorRef);
                    }
                }).match(DeleteTopic.class, (topic) -> {
                    if (getContext().child(topic.topic).isEmpty()) {
                        getContext().child(topic.topic).get().tell(akka.actor.Kill.getInstance(), getSelf());
                    }
                })
                .build();
    }

    public static class CreateTopic {
        private String topic;

        public static CreateTopic getInstence(String topic) {
            return new CreateTopic(topic);
        }

        public CreateTopic(String topic) {
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
}
