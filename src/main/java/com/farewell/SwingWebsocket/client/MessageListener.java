package com.farewell.SwingWebsocket.client;

import com.farewell.SwingWebsocket.chat.Message;

import java.util.ArrayList;

public interface MessageListener {
    void onMessage(Message message);
    void onActiveUsers(ArrayList<String> users);
}
