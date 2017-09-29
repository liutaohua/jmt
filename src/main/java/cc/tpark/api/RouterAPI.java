package cc.tpark.api;

import java.util.List;

public interface RouterAPI {
    /**
     * 发布消息
     *
     * @param msg
     */
    void publish(Object msg);

    /**
     * 订阅消息
     *
     * @param topic
     * @param id
     */
    void subscribe(String topic, String id);


    /**
     * 取消订阅
     *
     * @param topic
     * @param id
     */
    void desubscribe(String topic, String id);

    /**
     * 获取topic下的所有订阅成员
     *
     * @param topic
     * @return
     */
    List<String> getMembers(String topic);


    /**
     * 查看所有topic
     *
     * @return
     */
    List<String> getTopics();

    /**
     * 删除topic
     */
    void delTopic();
}
