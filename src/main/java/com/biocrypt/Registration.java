package com.biocrypt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class Registration extends JFrame {

    File[] files;
    int startX, startY;

    public Registration() {
        initComponents();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clear = new JButton();
        exit = new JButton();

        name_label = new JLabel("Name");
        user_label = new JLabel("Username");
        pass_label = new JLabel("Pin");
        username_field = new JTextField();
        name_field = new JTextField();
        password_field = new JPasswordField();
        registerBtn = new JButton("Register");


        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        name_field.setColumns(30);
        username_field.setColumns(30);
        password_field.setColumns(30);

        clear.setText("Clear");
        clear.addActionListener(this::clearActionPerformed);

        exit.setText("Exit");
        exit.addActionListener(this::exitActionPerformed);

        registerBtn.addActionListener(this::registrationActionPerformed);

        setTitle("BioCrypt");

        GroupLayout layout = new GroupLayout(getContentPane());

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(name_label)
                                .addComponent(user_label)
                                .addComponent(pass_label))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(name_field)
                                .addComponent(username_field)
                                .addComponent(password_field)
                                .addComponent(registerBtn))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(clear)
                                .addComponent(exit))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(name_label)
                                .addComponent(name_field))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(user_label)
                                .addComponent(username_field))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(pass_label)
                                .addComponent(password_field)
                                .addComponent(clear))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(registerBtn)
                                .addComponent(exit))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitActionPerformed(ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        this.dispose();
        Biocrypt vCSecure = new Biocrypt();
        vCSecure.setVisible(true);
    }//GEN-LAST:event_exitActionPerformed

    private void clearActionPerformed(ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        username_field.setText("");
    }//GEN-LAST:event_clearActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Biocrypt vCSecure = new Biocrypt();
        vCSecure.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void registrationActionPerformed(ActionEvent evt) {
        String name = name_field.getText();
        String username = username_field.getText();
        String pin = new String(password_field.getPassword());
        System.out.println(username + " " + pin);

        String url = Url.REGISTER_URL;
        String param = "name=" + name + "&" + "username=" + username + "&" + "pin=" + pin;
        String response = "";
        HttpSendData send1 = new HttpSendData(url, param);
        try {
            response = send1.sendPOST();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(response);
        if (response.equals("1")) {
            String[] data = new String[2];
            data[0] = username;
            data[1] = pin;
            this.setVisible(false);
            Biocrypt.main(data);
        } else {
            JOptionPane.showMessageDialog(this, "Something went wrong!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextArea choosenFile;
    private JButton clear;
    private JButton exit;
    private JFileChooser fileChooser;
    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JButton merger;


    JLabel name_label, user_label, pass_label;
    JTextField username_field, name_field;
    JPasswordField password_field;
    JButton registerBtn;
    // End of variables declaration//GEN-END:variables


    public static void main(String[] args) {
        Registration registration = new Registration();
        registration.setVisible(true);
    }
}


