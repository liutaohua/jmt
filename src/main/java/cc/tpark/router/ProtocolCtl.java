package cc.tpark.router;

import cc.tpark.commons.InnerMsg;
import cc.tpark.connections.Connections;

import java.util.*;

public class ProtocolCtl {
    private Map<String, List<String>> topics = new HashMap<>();
    private Connections connections;

    public ProtocolCtl(Connections connections) {
        this.connections = connections;
    }

    public void publish(InnerMsg msg) {
        String topic = msg.getTopic();
        List<String> members = topics.get(topic);
        if (members == null || members.size() <= 0) {
            return;
        }
        for (String ip : members) {
            connections.sendMsg(ip, msg);
        }
    }

    public void addMember(String topic, String ip) {
        ArrayList<String> ips = new ArrayList<>();
        ips.add(ip);
        addMembers(topic, ips);
    }

    public void addMembers(String topic, List<String> ips) {
        List<String> members = topics.get(topic);
        if (members == null || members.size() <= 0) {
            topics.put(topic, ips);
            return;
        }
        members.addAll(ips);
    }

    public void delMember(String topic, String ip) {
        ArrayList<String> ips = new ArrayList<>();
        ips.add(ip);
        delMembers(topic, ips);
    }

    public void delMembers(String topic, List<String> ips) {
        List<String> members = topics.get(topic);
        if (members == null || members.size() <= 0) {
            topics.remove(topic);
            return;
        }
        members.removeAll(ips);
        if (members.size() <= 0) {
            topics.remove(topic);
        }
    }

    public List<String> getMembers(String topic) {
        return topics.get(topic);
    }

    public Set<String> getTopics() {
        return topics.keySet();
    }
}
