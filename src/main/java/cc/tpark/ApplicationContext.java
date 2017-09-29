package cc.tpark;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cc.tpark.actor.manager.ConnectionManager;
import cc.tpark.actor.manager.TopicManager;

public enum ApplicationContext {
    instence;

    private final ActorSystem jmt;
    private final ActorRef connectionManager;
    private final ActorRef topicManager;

    ApplicationContext() {
        jmt = ActorSystem.create("jmt");
        connectionManager = jmt.actorOf(Props.create(ConnectionManager.class), "connections");
        topicManager = jmt.actorOf(Props.create(TopicManager.class), "topics");
    }

    public ActorSystem getJmt() {
        return jmt;
    }

    public ActorRef getConnectionManager() {
        return connectionManager;
    }

    public ActorRef getTopicManager() {
        return topicManager;
    }

}
