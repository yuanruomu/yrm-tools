package cn.yrm.tools.common.service;

public interface IMessageClient {

    /**
     * 同步发送消息
     * @param templateCode
     * @param jsonParams
     * @param receiver
     * @return
     */
    boolean sendMessageSync(String templateCode, String jsonParams, String receiver);

    /**
     * 异步发送消息 等待Job定时扫描
     * @param templateCode
     * @param jsonParams
     * @param receiver
     * @return
     */
    boolean sendMessageLazy(String templateCode, String jsonParams, String receiver);
}
