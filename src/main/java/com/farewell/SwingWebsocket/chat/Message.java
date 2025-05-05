package com.farewell.SwingWebsocket.chat;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String message;
    private String sender;
}
