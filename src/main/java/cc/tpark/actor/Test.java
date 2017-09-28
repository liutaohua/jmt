package cc.tpark.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Test {
    public static void main(String[] args) throws InterruptedException {

        ActorSystem jmt = ActorContext.instence.getJmt();

        ActorRef actorRef = jmt.actorOf(Props.create(Client.class), "test");
        actorRef.tell("xuming", ActorRef.noSender());
        Thread.sleep(6000);
        actorRef.tell("xuming", ActorRef.noSender());
//        ActorRef common = jmt.actorOf(Props.create(TopicUtil.class), "common");
//
//        common.tell(TopicUtil.CreateTopic.getInstence("ff"), ActorRef.noSender());
//        common.tell(TopicUtil.CreateTopic.getInstence("ff"), ActorRef.noSender());
//        common.tell(TopicUtil.CreateTopic.getInstence("ff"), ActorRef.noSender());
    }
}
