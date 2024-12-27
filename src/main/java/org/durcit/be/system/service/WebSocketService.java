package org.durcit.be.system.service;

public interface WebSocketService {
    public void sendMessageToUser(String userId, String destination, Object message);
    public void sendMessageToTopic(String topic, Object message);
}
