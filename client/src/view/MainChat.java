/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.ClientHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import model.Chat;
import model.ChatFile;
import model.FilePart;
import model.Friend;
import model.LoginSuccess;
import model.Notify;
import model.RawData;
import model.Room;
import model.UserIN;
import resources.Resources;

/**
 *
 * @author Hello
 */
public class MainChat extends javax.swing.JFrame implements Observer {

    /**
     * Creates new form MainChat
     */
    public List<Friend> friends;
    public List<Room> rooms;
    public UserIN myUserIN;
    public Room currentRoom;
    public ClientHandler clientHandler;

    private DefaultTableModel tmFriend;
    private DefaultTableModel tmRoom;
    private DefaultTableModel tmUserInRoom;
    private DefaultTableModel tmNotify;

    private ByteArrayOutputStream bos;
    private String downloadUrl;

    public MainChat(ClientHandler clientHandler, LoginSuccess loginSuccess) {
        initComponents();
        this.clientHandler = clientHandler;
        this.clientHandler.addObserver(this);

        //Khoi tao cac list de hien thi o table trong GUI
        myUserIN = loginSuccess.getUser();
        friends = loginSuccess.getListFriends();
        rooms = loginSuccess.getListRooms();

        //Truong hop chua co list Room trong loginSuccess
        if (rooms == null) {
            rooms = new ArrayList<>();
        }

        tmFriend = (DefaultTableModel) tbFriend.getModel();
        tmRoom = (DefaultTableModel) tbRoom.getModel();
        tmUserInRoom = (DefaultTableModel) tbUserInRoom.getModel();
        tmNotify = (DefaultTableModel) tbNotify.getModel();

        //disable chuc nang ve Room khi chua select Room(xoa phong, them nguoi, roi phong,..)
        disableRoomFeatures();

        //Hien thi du lieu len table trong GUI
        updateFriendTable();
        updateRoomTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbRoom = new javax.swing.JTable();
        sendMsgBtn = new javax.swing.JButton();
        sendFileBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbFriend = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        msgTxt = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbUserInRoom = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbNotify = new javax.swing.JTable();
        quanLyBanBeBtn = new javax.swing.JButton();
        createRoomBtn = new javax.swing.JButton();
        getOlderMessageBtn = new javax.swing.JButton();
        addUserBtn = new javax.swing.JButton();
        deleteUserBtn = new javax.swing.JButton();
        leaveRoomBtn = new javax.swing.JButton();
        roomNametxt = new javax.swing.JTextField();
        getNewerMessageBtn = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        listChatBox = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tbRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên phòng"
            }
        ));
        tbRoom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRoomMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbRoom);
        if (tbRoom.getColumnModel().getColumnCount() > 0) {
            tbRoom.getColumnModel().getColumn(0).setHeaderValue("Tên phòng");
        }

        sendMsgBtn.setText("Gửi");
        sendMsgBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMsgBtnActionPerformed(evt);
            }
        });

        sendFileBtn.setText("Gửi file");
        sendFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileBtnActionPerformed(evt);
            }
        });

        tbFriend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên Bạn", "Online"
            }
        ));
        tbFriend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbFriendMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbFriend);

        msgTxt.setColumns(20);
        msgTxt.setRows(3);
        jScrollPane3.setViewportView(msgTxt);

        tbUserInRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên người trong phòng"
            }
        ));
        jScrollPane5.setViewportView(tbUserInRoom);

        tbNotify.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nội dung thông báo"
            }
        ));
        jScrollPane6.setViewportView(tbNotify);

        quanLyBanBeBtn.setText("Quản lý bạn bè");
        quanLyBanBeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quanLyBanBeBtnActionPerformed(evt);
            }
        });

        createRoomBtn.setText("Tạo phòng");
        createRoomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createRoomBtnActionPerformed(evt);
            }
        });

        getOlderMessageBtn.setText("Tin nhắn cũ hơn");
        getOlderMessageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getOlderMessageBtnActionPerformed(evt);
            }
        });

        addUserBtn.setText("Thêm người");
        addUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserBtnActionPerformed(evt);
            }
        });

        deleteUserBtn.setText("Xoá người");
        deleteUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteUserBtnActionPerformed(evt);
            }
        });

        leaveRoomBtn.setText("Rời phòng");
        leaveRoomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leaveRoomBtnActionPerformed(evt);
            }
        });

        roomNametxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomNametxtActionPerformed(evt);
            }
        });

        getNewerMessageBtn.setText("Tin nhắn mới hơn");
        getNewerMessageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getNewerMessageBtnActionPerformed(evt);
            }
        });

        listChatBox.setEditable(false);
        listChatBox.setColumns(10);
        listChatBox.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        listChatBox.setRows(5);
        listChatBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listChatBoxMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(listChatBox);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(roomNametxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(createRoomBtn))
                    .addComponent(quanLyBanBeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(getNewerMessageBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(getOlderMessageBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jScrollPane3)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(sendFileBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sendMsgBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(16, 16, 16))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(deleteUserBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(leaveRoomBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addUserBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(quanLyBanBeBtn)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(createRoomBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(roomNametxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(getNewerMessageBtn)
                                    .addComponent(getOlderMessageBtn)))
                            .addComponent(jScrollPane5))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(addUserBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sendMsgBtn))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(sendFileBtn)
                                    .addComponent(deleteUserBtn)
                                    .addComponent(leaveRoomBtn))))))
                .addGap(31, 31, 31))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sendMsgBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMsgBtnActionPerformed
        String msg = msgTxt.getText();
        msgTxt.setText("");
        if (msg.length() == 0) {
            return;
        }
        Chat chat = new Chat();
        chat.setMsg(msg);
        chat.setChatType(0);
        if (!currentRoom.getFriendId().equals(-1)) {
            chat.setToRoomId(-1);
            if (currentRoom.getAdminId().equals(myUserIN.getId())) {
                chat.setUserId(currentRoom.getFriendId());
            } else {
                chat.setUserId(currentRoom.getAdminId());
            }
        } else {
            chat.setToRoomId(currentRoom.getId());
        }
        clientHandler.chatToRoom(chat);
    }//GEN-LAST:event_sendMsgBtnActionPerformed

    private void quanLyBanBeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quanLyBanBeBtnActionPerformed
        new FriendManager(this).setVisible(true);
    }//GEN-LAST:event_quanLyBanBeBtnActionPerformed

    private void leaveRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leaveRoomBtnActionPerformed
        // DEL_USER
        UserIN userIN = myUserIN;
        List<UserIN> listAddUser = new ArrayList<>();
        listAddUser.add(userIN);
        Room room = new Room();
        room.setId(currentRoom.getId());
        room.setName(currentRoom.getName());
        room.setListUsers(listAddUser);
        clientHandler.deleteFriendInRoom(room);

        //Xoa phong trong list Room khi roi phong
        for (Room r : rooms) {
            if (r.getId().equals(currentRoom.getId())) {
                rooms.remove(r);
                break;
            }
        }
        //Cap nhat list Room GUI
        currentRoom = null;
        disableRoomFeatures();
        updateRoomTable();
        //Xoa chat
    }//GEN-LAST:event_leaveRoomBtnActionPerformed

    private void createRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createRoomBtnActionPerformed
        String roomName = roomNametxt.getText();
        roomNametxt.setText("");
        if (roomName.length() == 0) {
            return;
        }
        Room room = new Room();
        room.setName(roomName);
        clientHandler.createRoom(room);
    }//GEN-LAST:event_createRoomBtnActionPerformed

    private void getOlderMessageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getOlderMessageBtnActionPerformed
        Chat lastMessage = currentRoom.getListChats().get(0);
        clientHandler.getOlderChat(lastMessage);
    }//GEN-LAST:event_getOlderMessageBtnActionPerformed

    private void addUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserBtnActionPerformed
        new AddUserToRoom(this, friends).setVisible(true);
    }//GEN-LAST:event_addUserBtnActionPerformed

    private void deleteUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteUserBtnActionPerformed
        int row = tbUserInRoom.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn người cần xoá");
            return;
        } else if (currentRoom.getListUsers().get(row).getId() == myUserIN.getId()) {
            JOptionPane.showMessageDialog(this, "Không được tự xoá bản thân");
            return;
        }
        List<UserIN> userINs = currentRoom.getListUsers();

        UserIN userIN = userINs.get(row);
        List<UserIN> listAddUser = new ArrayList<>();
        listAddUser.add(userIN);

        Room room = new Room();
        room.setId(currentRoom.getId());
        room.setName(currentRoom.getName());
        room.setListUsers(listAddUser);

        clientHandler.deleteFriendInRoom(room);

    }//GEN-LAST:event_deleteUserBtnActionPerformed

    private void sendFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileBtnActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int x = fileChooser.showDialog(this, "Chon file");
        if (x == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentRoom.getId();
            clientHandler.chatWithFile(file, currentRoom.getId());
            JOptionPane.showMessageDialog(this, "Upload done: " + file.getName());
            Chat chat = new Chat();
            chat.setMsg(file.getName());
            chat.setChatType(1);
            if (!currentRoom.getFriendId().equals(-1)) {
                chat.setToRoomId(-1);
                if (currentRoom.getAdminId().equals(myUserIN.getId())) {
                    chat.setUserId(currentRoom.getFriendId());
                } else {
                    chat.setUserId(currentRoom.getAdminId());
                }
            } else {
                chat.setToRoomId(currentRoom.getId());
            }
            System.out.println(chat.getToRoomId());
            clientHandler.chatToRoom(chat);
        }
    }//GEN-LAST:event_sendFileBtnActionPerformed

    private void roomNametxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomNametxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roomNametxtActionPerformed

    private void tbRoomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRoomMouseClicked
        int row = tbRoom.getSelectedRow();
        if (row < 0) {
            return;
        }
        //Lay thong tin phong
        Room room = rooms.get(row);
        clientHandler.getRoomInfo(room);

    }//GEN-LAST:event_tbRoomMouseClicked

    private void getNewerMessageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getNewerMessageBtnActionPerformed
        Chat lastMessage = currentRoom.getListChats().get(currentRoom.getListChats().size() - 1);
        clientHandler.getNewerChat(lastMessage);
    }//GEN-LAST:event_getNewerMessageBtnActionPerformed

    private void tbFriendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbFriendMouseClicked
        int row = tbFriend.getSelectedRow();
        Friend f = friends.get(row);
        Room r = new Room();
        r.setFriendId(f.getUserId());
        clientHandler.getRoomInfo(r);


    }//GEN-LAST:event_tbFriendMouseClicked

    private void listChatBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listChatBoxMouseClicked
        try {
            int line = listChatBox.getLineOfOffset(listChatBox.getCaretPosition());
            int start = listChatBox.getLineStartOffset(line);
            int end = listChatBox.getLineEndOffset(line);
            String text = listChatBox.getDocument().getText(start, end - start).trim();
            for (Chat c : currentRoom.getListChats()) {
                if (c.getMsg().equals(text.split(": ", 2)[1]) && c.getChatType() == 1) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int result = fileChooser.showOpenDialog(this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        downloadUrl = file.getAbsolutePath();
                        System.out.println(downloadUrl);
                        clientHandler.downloadFile(c.getId());
                    }
                    break;
                }
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_listChatBoxMouseClicked

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addUserBtn;
    private javax.swing.JButton createRoomBtn;
    private javax.swing.JButton deleteUserBtn;
    private javax.swing.JButton getNewerMessageBtn;
    private javax.swing.JButton getOlderMessageBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JButton leaveRoomBtn;
    private javax.swing.JTextArea listChatBox;
    private javax.swing.JTextArea msgTxt;
    private javax.swing.JButton quanLyBanBeBtn;
    private javax.swing.JTextField roomNametxt;
    private javax.swing.JButton sendFileBtn;
    private javax.swing.JButton sendMsgBtn;
    private javax.swing.JTable tbFriend;
    private javax.swing.JTable tbNotify;
    private javax.swing.JTable tbRoom;
    private javax.swing.JTable tbUserInRoom;
    // End of variables declaration//GEN-END:variables

    public void changeStatusOnline(Integer id) {
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).getUserId() == id) {
                friends.get(i).setStatus(1);
                tmFriend.removeRow(i);
                tmFriend.insertRow(i, friends.get(i).toObject());
                break;
            }
        }
    }

    public void changeStatusOffline(Integer id) {
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).getUserId() == id) {
                friends.get(i).setStatus(0);
                tmFriend.removeRow(i);
                tmFriend.insertRow(i, friends.get(i).toObject());
                break;
            }
        }
    }

    public void updateFriendTable() {
        tmFriend.setRowCount(0);
        for (Friend f : friends) {
            tmFriend.addRow(f.toObject());
        }
    }

    private String getFriendName(Integer id) {
        if (friends == null) {
            return null;
        }
        for (Friend f : friends) {
            if (f.getUserId().equals(id)) {
                return f.getUserName();
            }
        }
        return null;
    }

    public void updateRoomTable() {
        tmRoom.setRowCount(0);
        for (Room r : rooms) {
            if (r.getFriendId() != null) {
                if (r.getAdminId().equals(myUserIN.getId())) {
                    tmRoom.addRow(new Object[]{getFriendName(r.getFriendId())});
                } else {
                    tmRoom.addRow(new Object[]{getFriendName(r.getAdminId())});
                }
            } else {
                tmRoom.addRow(r.toObject());
            }
        }
    }

    public void updateUserINTable() {
        tmUserInRoom.setRowCount(0);
        if (currentRoom == null) {
            return;
        }
        List<UserIN> userINs = currentRoom.getListUsers();
        for (UserIN u : userINs) {
            if (myUserIN.getId().equals(u.getId())) {
                tmUserInRoom.addRow(new Object[]{"Tôi"});
                continue;
            }
            tmUserInRoom.addRow(u.toObject());
        }
    }

    public void disableRoomFeatures() {
        sendFileBtn.setEnabled(false);
        sendMsgBtn.setEnabled(false);
        getOlderMessageBtn.setEnabled(false);
        getNewerMessageBtn.setEnabled(false);
        addUserBtn.setEnabled(false);
        deleteUserBtn.setEnabled(false);
        leaveRoomBtn.setEnabled(false);
        listChatBox.selectAll();
        listChatBox.replaceSelection("");
        tmUserInRoom.setNumRows(0);

    }

    public void enableRoomFeatures(boolean isGroup, Integer adminId) {
        sendFileBtn.setEnabled(true);
        sendMsgBtn.setEnabled(true);
        getOlderMessageBtn.setEnabled(true);
        getNewerMessageBtn.setEnabled(true);
        if (isGroup) {
            leaveRoomBtn.setEnabled(true);
            if (myUserIN.getId().equals(adminId)) {
                deleteUserBtn.setEnabled(true);
                addUserBtn.setEnabled(true);
            }
        } else {
            addUserBtn.setEnabled(false);
            deleteUserBtn.setEnabled(false);
            leaveRoomBtn.setEnabled(false);
        }
    }

    private void deleteUser(Integer id) {
        if (currentRoom == null) {
            return;
        }
        List<UserIN> userINs = currentRoom.getListUsers();
        for (UserIN u : userINs) {
            if (u.getId().equals(id)) {
                userINs.remove(u);
                break;
            }
        }
        updateUserINTable();
    }

    private void leaveRoom(Integer id) {
        for (Room r : rooms) {
            if (r.getId().equals(id)) {
                rooms.remove(r);
                break;
            }
        }
        updateRoomTable();
    }

    private void showChat() {
        if (currentRoom == null) {
            return;
        }
        listChatBox.selectAll();
        listChatBox.replaceSelection("");
        List<Chat> chats = currentRoom.getListChats();
        for (Chat c : chats) {
            if (myUserIN.getName().equals(c.getUserName())) {
                listChatBox.append("Tôi: " + c.getMsg() + "\n");
            } else {
                listChatBox.append(c.getUserName() + ": " + c.getMsg() + "\n");
            }
        }
    }

    private String getRoomName(Integer id) {
        for (Room r : rooms) {
            if (r.getId().equals(id)) {
                return r.getName();
            }
        }
        return "";
    }

    @Override
    public void update(Observable o, Object arg) {
        RawData receiveData = (RawData) arg;
        hanlderResponse(receiveData);
    }

    private void hanlderResponse(RawData receiveData) {
        String code = receiveData.getCode();
        switch (code) {
            case Resources.CREATE_ROOM_SUCCESS:
                Room room = clientHandler.gson.fromJson(receiveData.getData(), Room.class);
                rooms.add(room);
                updateRoomTable();
                break;
            case Resources.ONLINE_RESPONSE:
                int userOnlineId = clientHandler.gson.fromJson(receiveData.getData(), Integer.class);
                changeStatusOnline(userOnlineId);
                ;
                break;
            case Resources.OFFLINE_RESPONSE:
                int userOfflineId = clientHandler.gson.fromJson(receiveData.getData(), Integer.class);
                changeStatusOffline(userOfflineId);
                break;
            case Resources.ADD_FRIEND_RESPONSE:
                Friend friend = clientHandler.gson.fromJson(receiveData.getData(), Friend.class);
                if (friend.getStatus().equals(1)) {
                    friends.add(friend);
                    updateFriendTable();
                }
                break;
            case Resources.RECEIVE_ADD_FRIEND:
                friend = clientHandler.gson.fromJson(receiveData.getData(), Friend.class);
                if (friend.getStatus().equals(1)) {
                    tmNotify.addRow(new Object[]{friend.getUserName() + " đã đồng ý kết bạn với bạn"});
                    friends.add(friend);
                    updateFriendTable();
                } else {
                    tmNotify.addRow(new Object[]{friend.getUserName() + " đã gửi lời mời kết bạn đến bạn"});
                }
                break;
            case Resources.DEL_FRIEND_RESPONSE:
                Friend user = clientHandler.gson.fromJson(receiveData.getData(), Friend.class);
                for (Friend f : friends) {
                    if (f.getUserId().equals(user.getUserId())) {
                        friends.remove(f);
                        break;
                    }
                }
                updateFriendTable();
                break;
            case Resources.RECEIVE_DEL_FRIEND:
                friend = clientHandler.gson.fromJson(receiveData.getData(), Friend.class);
                tmNotify.addRow(new Object[]{friend.getUserName() + " đã huỷ kết bạn với bạn"});
                for (Friend f : friends) {
                    if (f.getUserId().equals(friend.getUserId())) {
                        friends.remove(f);
                        break;
                    }
                }
                updateFriendTable();
                break;
            case Resources.ADD_USER_RESPONSE:
                room = clientHandler.gson.fromJson(receiveData.getData(), Room.class);
                UserIN userIN = room.getListUsers().get(0);
                currentRoom.getListUsers().add(userIN);
                updateUserINTable();
                break;
            case Resources.RECEIVE_ADD_USER:
                room = clientHandler.gson.fromJson(receiveData.getData(), Room.class);
                tmNotify.addRow(new Object[]{"Bạn được thêm vào phòng " + room.getName()});
                rooms.add(room);
                updateRoomTable();
                break;
            case Resources.DELETE_USER_SUCCESS:
                room = clientHandler.gson.fromJson(receiveData.getData(), Room.class);
                userIN = room.getListUsers().get(0);
                deleteUser(userIN.getId());
                break;
            case Resources.RECEIVE_DELETE_USER:
                room = clientHandler.gson.fromJson(receiveData.getData(), Room.class);
                userIN = room.getListUsers().get(0);

                if (myUserIN.getId().equals(userIN.getId())) {
                    tmNotify.addRow(new Object[]{"Bạn đã rời phòng " + room.getName()});
                    leaveRoom(room.getId());
                    disableRoomFeatures();
                } else {
                    tmNotify.addRow(new Object[]{userIN.getName() + " đã rời phòng " + room.getName()});
                    room = clientHandler.gson.fromJson(receiveData.getData(), Room.class);
                    userIN = room.getListUsers().get(0);
                    //Xoa user da bi roi phong
                    deleteUser(userIN.getId());
                }
                break;
            case Resources.GET_ROOM_INFO_RESPONSE:
                currentRoom = clientHandler.gson.fromJson(receiveData.getData(), Room.class);

                //Hien thi thong tin
                enableRoomFeatures(currentRoom.isIsGroup(), currentRoom.getAdminId());
                updateUserINTable();

                Chat chat = new Chat();
                chat.setToRoomId(currentRoom.getId());
                chat.setId(-1);
                clientHandler.getNewerChat(chat);
                break;
            case Resources.CHAT_MSG_ROOM_RESPONSE:
                chat = clientHandler.gson.fromJson(receiveData.getData(), Chat.class);
                if (currentRoom == null || !currentRoom.getId().equals(chat.getToRoomId())) {
                    //phong private
                    if (getRoomName(chat.getToRoomId()).equals("")) {
                        tmNotify.addRow(new Object[]{chat.getUserName() + " đã gửi tin nhắn riêng cho bạn"});
                    } else {
                        tmNotify.addRow(new Object[]{"Có tin nhắn mới trong phòng " + getRoomName(chat.getToRoomId())});
                    }
                } else {
                    chat = new Chat();
                    chat.setId(-1);
                    chat.setToRoomId(currentRoom.getId());
                    clientHandler.getNewerChat(chat);
                }
                break;
            case Resources.GET_CHAT_RESPONSE:
                Chat[] listChats = clientHandler.gson.fromJson(receiveData.getData(), Chat[].class);
                List<Chat> chats = new LinkedList<>(Arrays.asList(listChats));
                listChatBox.selectAll();
                listChatBox.replaceSelection("");
                if (chats.size() == 0) {
                    return;
                }
                if (chats.size() > 1 && chats.get(0).getId() > chats.get(1).getId()) {
                    Collections.reverse(chats);
                }
                if (chats.isEmpty()) {
                    currentRoom.setListChats(new ArrayList<>());
                } else {
                    currentRoom.setListChats(chats);
                }
                showChat();
                break;
            case Resources.DOWNLOAD_FILE:
                try {
                ChatFile chatFile = clientHandler.gson.fromJson(receiveData.getData(), ChatFile.class);
                FilePart filePart = chatFile.getFilePart();
                if (filePart.getPartIndex() == 1) {
                    System.out.println("start");
                    bos = new ByteArrayOutputStream();
                }
                bos.write(filePart.getData(), 0, filePart.getPartSize());
                int index = filePart.getPartIndex();
                int count = filePart.getPartCount();
                if (index == count) {
                    System.out.println("ok");
                    byte[] fileFull = bos.toByteArray();
                    //Upload file to disk
                    JFileChooser fileChooser = new JFileChooser();
                    File file = new File(downloadUrl + "\\" + filePart.getName());
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(fileFull);
                    fos.close();
                    JOptionPane.showMessageDialog(this, "download done" + filePart.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }
    }
}