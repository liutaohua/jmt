package cc.tpark.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Test {
    public static void main(String[] args) {

        ActorSystem jmt = ActorContext.instence.getJmt();

        ActorRef common = jmt.actorOf(Props.create(TopicUtil.class), "common");

        common.tell(TopicUtil.CreateTopic.getInstence("ff"), ActorRef.noSender());
        common.tell(TopicUtil.CreateTopic.getInstence("ff"), ActorRef.noSender());
        common.tell(TopicUtil.CreateTopic.getInstence("ff"), ActorRef.noSender());
    }
}
