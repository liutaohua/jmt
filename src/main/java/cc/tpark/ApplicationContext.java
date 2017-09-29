package cc.tpark;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cc.tpark.actor.api.ActorConnAPI;
import cc.tpark.actor.api.ActorTopicAPI;
import cc.tpark.actor.manager.ConnectionManager;
import cc.tpark.actor.manager.TopicManager;
import cc.tpark.api.ConnectionAPI;
import cc.tpark.api.TopicAPI;

public enum ApplicationContext {
    instence;

    private final ActorSystem jmt;
    private final ActorRef connectionManager;
    private final ActorRef topicManager;

    private final ConnectionAPI connectionAPI;
    private TopicAPI topicAPI;

    ApplicationContext() {
        jmt = ActorSystem.create("jmt");
        connectionManager = jmt.actorOf(Props.create(ConnectionManager.class), "connections");
        topicManager = jmt.actorOf(Props.create(TopicManager.class), "topics");
        connectionAPI = new ActorConnAPI(connectionManager);
        topicAPI = new ActorTopicAPI(topicManager);
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

    public TopicAPI getTopicAPI() {
        return topicAPI;
    }
}
