package cc.tpark.router;

import cc.tpark.commons.InnerMsg;

import java.util.List;

public interface Router {
    /**
     * 发布消息
     *
     * @param msg
     */
    void publish(InnerMsg msg);

    /**
     * 订阅消息
     *
     * @param topic
     * @param ip
     */
    void subscribe(String topic, String ip);


    /**
     * 取消订阅
     *
     * @param topic
     * @param ip
     */
    void desubscribe(String topic, String ip);

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
