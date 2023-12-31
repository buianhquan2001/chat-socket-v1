/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.ClientHandler;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Friend;
import model.RawData;
import resources.Resources;

/**
 *
 * @author Quan
 */
public class FriendInvitation extends javax.swing.JFrame implements Observer {

    /**
     * Creates new form FriendInvitation
     */
    private ClientHandler clientHandler;
    private DefaultTableModel tmInvitation;
    private List<Friend> invitations;
    private MainChat mainChat;
    
    public FriendInvitation(ClientHandler clientHandler) {
        initComponents();
        setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
        this.clientHandler = clientHandler;
        this.clientHandler.addObserver(this);
        this.tmInvitation = (DefaultTableModel) tbInvitation.getModel();
        
        clientHandler.getInvitation();
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
        tbInvitation = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        backBt = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tbInvitation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên"
            }
        ));
        jScrollPane1.setViewportView(tbInvitation);

        jButton1.setText("Đồng ý");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Huỷ kết bạn");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        backBt.setText("Quay lại");
        backBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(34, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(backBt, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backBt, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtActionPerformed
        this.setVisible(false);
        clientHandler.deleteObserver(this);
    }//GEN-LAST:event_backBtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int row = tbInvitation.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn người kết bạn");
            return ;
        }
        Friend friend = invitations.get(row);
        clientHandler.addFriend(friend);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int row = tbInvitation.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn người cần huỷ");
            return ;
        }
        Friend friend = invitations.get(row);
        clientHandler.deleteFriend(friend);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbInvitation;
    // End of variables declaration//GEN-END:variables

    public void updateInvitationTable() {
        tmInvitation.setRowCount(0);
        for (Friend f : invitations) {
            tmInvitation.addRow(f.toFriendInvitationObject());
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        RawData receiveData = (RawData) arg;
        hanlderResponse(receiveData);
    }

    private void hanlderResponse(RawData receiveData) {
        String code = receiveData.getCode();
        switch(code) {
            case Resources.FIND_INVITE_RESPONSE:
                Friend[] listInvitations = clientHandler.gson.fromJson(receiveData.getData(), Friend[].class);
                invitations = new LinkedList<>(Arrays.asList(listInvitations));
                updateInvitationTable();
                break;
            case Resources.ADD_FRIEND_RESPONSE:
            case Resources.DEL_FRIEND_RESPONSE:
                clientHandler.getInvitation();
                break;
        } 
    }
}
