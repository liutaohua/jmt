package cc.tpark.router;

import cc.tpark.commons.InnerMsg;
import cc.tpark.connections.Connections;

import java.util.ArrayList;
import java.util.List;

public class SimpleRouter implements Router {
    private ProtocolCtl ctl;

    public SimpleRouter(Connections connections) {
        this.ctl = new ProtocolCtl(connections);
    }

    @Override
    public void publish(InnerMsg msg) {
        ctl.publish(msg);
    }

    @Override
    public void subscribe(String topic, String ip) {
        ctl.addMember(topic, ip);
    }

    @Override
    public void desubscribe(String topic, String ip) {
        ctl.delMember(topic, ip);
    }

    @Override
    public List<String> getMembers(String topic) {
        return ctl.getMembers(topic);
    }

    @Override
    public List<String> getTopics() {
        ArrayList<String> topics = new ArrayList<>();
        topics.addAll(ctl.getTopics());
        return topics;
    }

    @Override
    public void delTopic() {

    }
}
