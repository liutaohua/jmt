package cc.tpark;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cc.tpark.actor.ActorConnAPi;
import cc.tpark.actor.manager.ConnectionManager;
import cc.tpark.actor.manager.TopicManager;
import cc.tpark.api.ConnectionAPI;

public enum ApplicationContext {
    instence;

    private final ActorSystem jmt;
    private final ActorRef connectionManager;
    private final ActorRef topicManager;

    private final ConnectionAPI connectionAPI;

    ApplicationContext() {
        jmt = ActorSystem.create("jmt");
        connectionManager = jmt.actorOf(Props.create(ConnectionManager.class), "connections");
        topicManager = jmt.actorOf(Props.create(TopicManager.class), "topics");
        connectionAPI = new ActorConnAPi(connectionManager);
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

    public ConnectionAPI getConnectionAPI() {
        return connectionAPI;
    }
}
