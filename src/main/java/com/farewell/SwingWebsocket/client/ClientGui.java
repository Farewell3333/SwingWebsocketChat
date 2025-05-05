package com.farewell.SwingWebsocket.client;


import com.farewell.SwingWebsocket.chat.Message;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ClientGui extends JFrame implements MessageListener {
    private JPanel connectedUsersPanel, messagePanel;
    private MyStompClient myStompClient;
    private String username;
    private JScrollPane scrollPane,userScrollPane;
    public ClientGui(String username) throws ExecutionException, InterruptedException {
        super("User: " + username);
        this.username = username;
        myStompClient = new MyStompClient(this, username);
        setSize(1218,685);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ClientGui.this,"Do you want to exit?","Exit",JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    myStompClient.disconnectUser(username);
                    ClientGui.this.dispose();
                }
            }
        });
        addComponentListener(new ComponentAdapter() {
             public void componentResized(ComponentEvent e) {
                 updateMessageSize();
             }
        });
        getContentPane().setBackground(Utilities.PRIMARY_COLOR);
        addGuiComponents();
    }
    private void addGuiComponents(){
        addConnectUsersComponents();
        addChatComponents();
    }
    private void addConnectUsersComponents(){
        connectedUsersPanel = new JPanel();
        connectedUsersPanel.setBorder(Utilities.addPadding(10,10,10,10));
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECONDARY_COLOR);
        connectedUsersPanel.setPreferredSize(new Dimension(200, 640));

        JLabel connectedUsersLabel = new JLabel("Connected Users:");

        connectedUsersLabel.setFont(new Font("Inter", Font.BOLD,18 ));
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);
        connectedUsersPanel.add(connectedUsersLabel);
        userScrollPane = new JScrollPane(connectedUsersPanel);
        userScrollPane.setBackground(Utilities.TRANSPARENT_COLOR);
        userScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        userScrollPane.getVerticalScrollBar().setUnitIncrement(15);
        add(userScrollPane, BorderLayout.WEST);

    }
    private void addChatComponents(){
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT_COLOR);
        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setBackground(Utilities.TRANSPARENT_COLOR);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        scrollPane.getViewport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                revalidate();
                repaint();
            }
        });
        chatPanel.add(scrollPane, BorderLayout.CENTER);



        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        JTextField inputField = new JTextField();
        inputField.setBorder(Utilities.addPadding(0,10,0,10));
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER){
                    String input = inputField.getText();
                    if(input.isEmpty()) return;
                    inputField.setText("");
                    myStompClient.sendMessage(new Message(input,username));

                }
            }
        });
        inputField.setBackground(Utilities.SECONDARY_COLOR);
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setFont(new Font("Inter", Font.PLAIN,16 ));
        inputField.setPreferredSize(new Dimension(inputPanel.getWidth(),50));
        inputPanel.add(inputField, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);
        inputPanel.setBorder(Utilities.addPadding(10,10,10,10));

        add(chatPanel, BorderLayout.CENTER);
    }
    private JPanel createChatPanel(Message message){
        JPanel chatMessage = new JPanel();
        chatMessage.setBackground(Utilities.TRANSPARENT_COLOR);
        chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
        chatMessage.setBorder(Utilities.addPadding(20,20,10,20));

        JLabel usernameLabel = new JLabel(message.getSender());
        usernameLabel.setForeground(Utilities.TEXT_COLOR);
        usernameLabel.setFont(new Font("Inter", Font.BOLD,18 ));
        chatMessage.add(usernameLabel);
        JLabel messageLabel = new JLabel();
        messageLabel.setText("<html>" + "<body style='width:" + (0.60 * getWidth()) + "'px>"+ message.getMessage() + "</body>"+ "</html>");
        messageLabel.setForeground(Utilities.TEXT_COLOR);
        messageLabel.setFont(new Font("Inter", Font.PLAIN,18 ));
        chatMessage.add(messageLabel);
        return chatMessage;
    }
    private void updateMessageSize() {
        for (int i = 0; i < messagePanel.getComponents().length; i++) {
            Component component = messagePanel.getComponent(i);
            if (component instanceof JPanel) {
                JPanel chatMessage = (JPanel) component;
                if (chatMessage.getComponent(1) instanceof JLabel) {
                    JLabel messageLabel = (JLabel) chatMessage.getComponent(1);
                    messageLabel.setText("<html>" +
                            "<body style='width:" + (0.60 * getWidth()) + "'px>" +
                            messageLabel.getText() +
                            "</body>" +
                            "</html>");
                }
            }
        }
    }

    @Override
    public void onMessage(Message message) {
        messagePanel.add(createChatPanel(message));
        revalidate();
        repaint();

        scrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
    }

    @Override
    public void onActiveUsers(ArrayList<String> users) {
        if(connectedUsersPanel.getComponentCount() >=2){
            connectedUsersPanel.remove(1);
        }
        JPanel usersPanel = new JPanel();
        usersPanel.setBackground(Utilities.TRANSPARENT_COLOR);
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));

        for (String user : users) {
            JLabel usernameLabel = new JLabel();
            usernameLabel.setText(user);
            usernameLabel.setForeground(Utilities.TEXT_COLOR);
            usernameLabel.setFont(new Font("Inter", Font.BOLD,16 ));
            usersPanel.add(usernameLabel);
        }
        connectedUsersPanel.add(usersPanel);
        revalidate();
        repaint();
    }
}
