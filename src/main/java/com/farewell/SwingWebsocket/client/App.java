package com.farewell.SwingWebsocket.client;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String username = JOptionPane.showInputDialog(null,"Enter username","Chat",JOptionPane.QUESTION_MESSAGE);
                if(username == null || username.equals("") || username.length() >16){
                    JOptionPane.showMessageDialog(null,"Invalid username","Chat",JOptionPane.ERROR_MESSAGE);
                    return;
                }


                ClientGui clientGui = null;
                try {
                    clientGui = new ClientGui(username);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clientGui.setVisible(true);
            }
        });
    }
}
