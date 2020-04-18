package com.biocrypt;

import jdk.jshell.execution.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ShareCombiner extends JFrame {

    int n;
    String username, pin;

    ArrayList<File> files = new ArrayList<>();
    int startX, startY;

    public ShareCombiner() {
        initComponents();
        //fileChooser.setMultiSelectionEnabled(true);
        //fileChooser.setAcceptAllFileFilterUsed(true);
    }


    private void initComponents() {

        setTitle("BioCrypt");

        jLabel1 = new JLabel();
        fileChooser = new JFileChooser();
        jScrollPane1 = new JScrollPane();
        choosenFile = new JTextArea();
        merger = new JButton();
        clear = new JButton();
        exit = new JButton();

        user_label = new JLabel("Username");
        pass_label = new JLabel("Pin");
        username_field = new JTextField();
        password_field = new JPasswordField();
        loginBtn = new JButton("Login");
        imgBtn = new JButton("Load Fingerprint");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel1.setText("Login Here");
        username_field.setColumns(30);
        password_field.setColumns(30);
        //fileChooser.setApproveButtonMnemonic(1);
        //fileChooser.setApproveButtonText("Select");
        //fileChooser.setApproveButtonToolTipText("");
        fileChooser.setForeground(new java.awt.Color(255, 255, 102));
        fileChooser.setToolTipText("");
        fileChooser.addActionListener(this::fileChooserActionPerformed);

        choosenFile.setColumns(20);
        choosenFile.setRows(5);
        jScrollPane1.setViewportView(choosenFile);

        imgBtn.addActionListener(evt -> {
            try {
                imgBtnActionPerformed(evt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        merger.setText("Decrypt");
        merger.addActionListener(this::mergerActionPerformed);

        clear.setText("Clear");
        clear.addActionListener(this::clearActionPerformed);

        exit.setText("Exit");
        exit.addActionListener(this::exitActionPerformed);

        loginBtn.addActionListener(evt -> {
            try {
                loginActionPerformed(evt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(user_label)
                                .addComponent(pass_label))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(username_field)
                                .addComponent(password_field)
                                .addComponent(loginBtn))
                        //.addComponent(fileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(merger)
                                .addComponent(imgBtn)
                                .addComponent(exit)
                                .addComponent(jScrollPane1))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(user_label)
                                .addComponent(username_field)
                                .addComponent(merger))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(pass_label)
                                .addComponent(password_field)
                                .addComponent(imgBtn))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loginBtn)
                                .addComponent(exit))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                //.addComponent(fileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void imgBtnActionPerformed(ActionEvent evt) throws Exception {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, PNG & BMP Images",
                "jpg", "png", "jpeg", "BMP");

        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                BufferedImage img = ImageIO.read(file);

                BufferedImage currOutputImg = Utility.getCroppedImageFromRemoteCoordinates(img, username, n);

                String filePath = Url.CURRENT_OUTPUT_PATH + "output_" + username_field.getText() + ".png";
                ImageIO.write(currOutputImg, "PNG", new File(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void exitActionPerformed(ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        this.dispose();
        Biocrypt vCSecure = new Biocrypt();
        vCSecure.setVisible(true);
    }//GEN-LAST:event_exitActionPerformed

    private void clearActionPerformed(ActionEvent evt) {
        username_field.setText("");
    }

    private void mergerActionPerformed(ActionEvent evt) {
        try {
            if (files != null) {
                if (files.size() == 1) {
                    JOptionPane.showMessageDialog(this, "Number of share selected must be greater than 1.", "Error", 0);
                } else {
                    try {
                        ArrayList<BufferedImage> shares = new ArrayList<>();
                        for (File file : files) {
                            shares.add(ImageIO.read(file));
                        }

                        BufferedImage mergedImage = Utility.getMergedImageFromShares(shares);

                        String filePath = Url.OUTPUT_PATH + "output_" + username_field.getText() + ".png";
                        String currOutputFilePath = Url.CURRENT_OUTPUT_PATH + "output_" + username_field.getText() + ".png";

                        ImageIO.write(mergedImage, "PNG", new File(filePath));

                        Test test = new Test();
                        boolean match = test.getMatch(currOutputFilePath, filePath);
                        if (match) {
                            JOptionPane.showMessageDialog(this, "Shares decrypted successfully. Authentication SUCCESSFUL !!! ", "Success", 1);
                        } else {
                            JOptionPane.showMessageDialog(this, "Shares decrypted successfully. Authentication FAILED !!! ", "Success", 1);
                        }

                        choosenFile.setText(choosenFile.getText() + "\nOutput file output.png generated successfully.");
                        OutputImage outImage = new OutputImage(mergedImage);
                        outImage.setVisible(true);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No share file selected.", "Error", 0);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_mergerActionPerformed

    private void fileChooserActionPerformed(ActionEvent evt) {//GEN-FIRST:event_fileChooserActionPerformed

        if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
            //files=fileChooser.getSelectedFiles();
            StringBuilder output = new StringBuilder("Selected files:\n");
            for (File f : files) {
                output.append(f.getName()).append("\n");
            }
            choosenFile.setText(output.toString());
        } else {
            choosenFile.setText("No share selected.");
        }
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        Biocrypt vCSecure = new Biocrypt();
        vCSecure.setVisible(true);
    }

    private void loginActionPerformed(ActionEvent evt) throws Exception {
        username = username_field.getText();
        pin = new String(password_field.getPassword());

        String url = Url.VERIFY_PIN_URL;
        String param = "username=" + username + "&" + "pin=" + pin;
        String response = "";
        HttpSendData send1 = new HttpSendData(url, param);
        try {
            response = send1.sendPOST();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response.equals("1")) {
            NGenerator nGenerator = new NGenerator();
            n = nGenerator.getValueOfN(pin);
            System.out.println("N: " + n + " " + pin);
            int k = 2;

            List<String> serverList = Url.nodes;

            for (String serverIp: serverList) {
                List<Share> shares = Utility.getAllShares(serverIp, username);

                for(Share share: shares) {
                    byte[] shareBytes = Base64.getDecoder().decode(share.getData());
                    ByteArrayInputStream bis = new ByteArrayInputStream(shareBytes);

                    String fileName = Url.GENERATED_SHARE_PATH + "share_" + username + "_" + share.getNumber() + ".png";

                    try {
                        BufferedImage shareImage = ImageIO.read(bis);
                        File newShare = new File(fileName);
                        files.add(newShare);

                        ImageIO.write(shareImage, "png", newShare);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("image created");
                }
            }
            System.out.println("Files: " + files.size());;
            StringBuilder output = new StringBuilder("Selected files:\n");
            for (File f : files) {
                output.append(f.getName()).append("\n");
            }
            choosenFile.setText(output.toString());

        } else {
            JOptionPane.showMessageDialog(this, "Incorrect login or password",
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


    JLabel user_label;
    JLabel pass_label;
    JTextField username_field;
    JPasswordField password_field;
    JButton loginBtn;
    JButton imgBtn;
    // End of variables declaration//GEN-END:variables
}
