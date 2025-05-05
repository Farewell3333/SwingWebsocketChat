package com.farewell.SwingWebsocket.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {
     private final SimpMessagingTemplate messagingTemplate;
     private final WebSocketSessionManager sessionManager;

     @Autowired
     public WebsocketController(SimpMessagingTemplate messagingTemplate, WebSocketSessionManager sessionManager) {
         this.messagingTemplate = messagingTemplate;
         this.sessionManager = sessionManager;
     }
     @MessageMapping("/message")
    public void handleMessage(Message message) {
         System.out.println("Received message from user: "+ message.getSender() + ": " + message.getMessage());
         messagingTemplate.convertAndSend("/topic/messages", message);
         System.out.println("Sent message to /topic/messages: " + message.getSender() + ": " + message.getMessage());
     }

     @MessageMapping("/connect")
    public void connectUser(String username) {
         sessionManager.addUser(username);
         sessionManager.broadcastActiveUsernames();
         System.out.println("Connected to user: " + username);
     }

     @MessageMapping("/disconnect")
    public void disconnectUser(String username) {
         sessionManager.removeUser(username);
         sessionManager.broadcastActiveUsernames();
         System.out.println("Disconnected from user: " + username);
     }

     @MessageMapping("/request-users")
    public void requestUsers() {
         sessionManager.broadcastActiveUsernames();
         System.out.println("Requesting users");
     }


}
