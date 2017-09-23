package cc.tpark.connections;

import cc.tpark.commons.InnerMsg;

public interface Connections {
    /**
     * 向ip发送消息
     *
     * @param ip
     * @param msg
     */
    public void sendMsg(String ip, InnerMsg msg);
}
