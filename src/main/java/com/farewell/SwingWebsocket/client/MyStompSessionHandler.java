package com.farewell.SwingWebsocket.client;

import com.farewell.SwingWebsocket.chat.Message;
import org.apache.tomcat.websocket.WsSession;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private String username;
    private MessageListener messageListener;
    public MyStompSessionHandler(MessageListener messageListener,String username) {
        this.username = username;
        this.messageListener = messageListener;
    }
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Connected");
        session.send("/app/connect",username);
        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if (payload instanceof Message) {
                        Message message = (Message) payload;
                        messageListener.onMessage(message);
                        System.out.println("Received message: " + message.getSender() + ": " + message.getMessage());

                    } else {
                        System.out.println("Received unknown payload: " + payload.getClass().getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Subscribed");

        session.subscribe("/topic/users", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return new ArrayList<String>().getClass();
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                   if (payload instanceof ArrayList) {
                       ArrayList<String> activeUsers = (ArrayList<String>) payload;
                       messageListener.onActiveUsers(activeUsers);
                       System.out.println("Received active users: " + activeUsers);
                   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Subscribed to users");

        session.send("/app/request-users","");
    }

@Override
public void handleTransportError(StompSession session, Throwable exception) {
    super.handleTransportError(session, exception);
}
}
