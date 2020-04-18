package com.biocrypt;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class Biocrypt extends javax.swing.JFrame {

    JLabel imageLabel;
    int finalStartX = -1, finalStartY = -1, finalEndX = -1, finalEndY = -1;
    ImageIcon icon;
    BufferedImage img;

    public static String pin, username;
    public static final int K = 2;

    public Biocrypt() {
        initComponents();

    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        setTitle("BioCrypt");

        loadImage = new javax.swing.JButton();
        imagePanel = new javax.swing.JPanel();
        selectRegion = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        jLabel1 = new JLabel();
        participant = new javax.swing.JComboBox();
        jLabel2 = new JLabel();
        numberOFShares = new javax.swing.JComboBox();
        generateShares = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        laodImageMenu = new javax.swing.JMenuItem();
        clearMenu = new javax.swing.JMenuItem();
        exitMenu = new javax.swing.JMenuItem();
        combineMenu = new javax.swing.JMenu();
        shareCombinerMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        loadImage.setText("Load Image");
        loadImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadImageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
                imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 607, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
                imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 401, Short.MAX_VALUE)
        );

        selectRegion.setText("Select Region");
        selectRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectRegionActionPerformed(evt);
            }
        });

        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        jLabel1.setText("K");

        participant.setModel(new javax.swing.DefaultComboBoxModel(new String[]{ "2", "3", "4" }));

        jLabel2.setText("N");

        numberOFShares.setModel(new javax.swing.DefaultComboBoxModel(new String[]{ "2", "3", "4" }));

        generateShares.setText("Generate");
        generateShares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    generateSharesActionPerformed(evt);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        fileMenu.setText("Encrypt");

        laodImageMenu.setText("Load Image");
        laodImageMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laodImageMenuActionPerformed(evt);
            }
        });
        fileMenu.add(laodImageMenu);

        clearMenu.setText("Clear");
        clearMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearMenuActionPerformed(evt);
            }
        });
        fileMenu.add(clearMenu);

        exitMenu.setText("Exit");
        exitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenu);

        jMenuBar1.add(fileMenu);

        combineMenu.setText("Decrypt");

        shareCombinerMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        shareCombinerMenu.setText("Share Combiner");
        shareCombinerMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shareCombinerMenuActionPerformed(evt);
            }
        });
        combineMenu.add(shareCombinerMenu);

        jMenuBar1.add(combineMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(selectRegion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(loadImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        /*.addGroup(layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel2))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(numberOFShares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(participant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))*/
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(exit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(generateShares, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)))
                                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 3, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(loadImage)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectRegion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clear)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                /*.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(participant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(numberOFShares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))*/
                                .addGap(18, 18, 18)
                                .addComponent(generateShares)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(exit)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    int startX, startY, endX, endY;
    boolean isDragging = false;

    private void loadImageActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();

                img = ImageIO.read(file);
                icon = new ImageIcon(img);

                imageLabel = new JLabel(icon) {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);
                        g.setColor(Color.GREEN);
                        int w = (endX - startX);
                        int h = (endY - startY);

                        if (w < 0) {
                            w = w * -1;
                        }
                        g.drawRect(startX, startY, w, h);
                    }
                };
                System.out.println(0 + " -- " + 0);
                System.out.println(img.getWidth() + " -- " + img.getHeight());
                imageLabel.setBounds(0, 0, img.getWidth(), img.getHeight());
                imageLabel.setIcon(icon);
                imageLabel.setAlignmentX(JLabel.CENTER);
                imageLabel.setAlignmentY(JLabel.CENTER);
                imageLabel.setLayout(new GridLayout(1, 1));
                imageLabel.setBorder(new LineBorder(Color.yellow, 3));
                imageLabel.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        startX = e.getX();
                        startY = e.getY();
                        isDragging = true;
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // System.out.println("Released");
                        // System.out.println(endX + "---- " + endY);
                        isDragging = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // System.out.println("entered");
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // System.out.println(e.getLocationOnScreen());
                    }
                });

                imageLabel.addMouseMotionListener(new MouseMotionListener() {

                    @Override
                    public void mouseDragged(MouseEvent e) {

                        if (isDragging) {

                            endX = e.getX();
                            endY = e.getY();
                            imageLabel.revalidate();
                            imageLabel.repaint();
                        }
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        imageLabel.revalidate();
                        imageLabel.repaint();
                    }
                });

                imagePanel.add(imageLabel);
                imagePanel.setLayout(new GridLayout(1, 1));
                imagePanel.revalidate();
                imagePanel.repaint();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    boolean isSelected = false;

    private void selectRegionActionPerformed(java.awt.event.ActionEvent evt) {
        if (endX == startX || endY == startY) {
            JOptionPane.showMessageDialog(rootPane, "Please select a region.", "Error", 0);
        } else {
            finalStartX = startX;
            finalStartY = startY;
            finalEndX = endX;
            finalEndY = endY;
            isSelected = true;
            System.out.println(startX + "   " + startY);
            System.out.println(endX + "   " + endY);
            JOptionPane.showMessageDialog(this, "Region selected successfully.");

            int height = finalEndY - finalStartY;
            int width = finalEndX - finalStartX;
            BufferedImage cropImg = img.getSubimage(finalStartX, finalStartY, width, height);

            String filename = Url.INPUT_PATH + "input_" + username + ".png";
            try {
                ImageIO.write(img, "PNG", new File(filename));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String coordinates = finalStartX + "," + finalStartY + ","
                        + finalEndX + "," + finalEndY;
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(img, "PNG", outputStream);
                String encodedImage = Base64.getEncoder().encodeToString(outputStream.toByteArray());

                String url = Url.UPLOAD_FINGERPRINT_URL;
                String param = "fingerprint=" + URLEncoder.encode(encodedImage, StandardCharsets.UTF_8) + "&" + "username=" + username + "&"
                        + "coordinates=" + coordinates;
                String response = "";
                HttpSendData send1 = new HttpSendData(url, param);
                try {
                    response = send1.sendPOST();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        startX = 0;
        startY = 0;
        endX = 0;
        endY = 0;
        finalStartX = -1;
        finalStartY = -1;
        finalEndX = -1;
        finalEndY = -1;
        isSelected = false;
        imagePanel.remove(imageLabel);
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    private void generateSharesActionPerformed(java.awt.event.ActionEvent evt) throws Exception {//GEN-FIRST:event_generateSharesActionPerformed

        if (isSelected) {
            ImageProcessing imageProcessing = new ImageProcessing(img, finalStartX, finalStartY, finalEndX, finalEndY, username);
            //int k = Integer.parseInt((String)participant.getSelectedItem());
            //int n =Integer.parseInt((String)numberOFShares.getSelectedItem());
            NGenerator nGenerator = new NGenerator();
            int n = nGenerator.getValueOfN(pin);
            System.out.println("N: " + n + " " + pin);
            int k = K;

            boolean sharesGenerated = false;
            if (k <= n) {
                imageProcessing.generateKoutOfNShares(k, n);
                sharesGenerated = true;

            } else {
                JOptionPane.showMessageDialog(this, "K must me smaller than equal to n.", "Error", 0);
            }

            if (sharesGenerated) {
                imageLabel.revalidate();
                imageLabel.repaint();
                startX = 0;
                startY = 0;
                endX = 0;
                endY = 0;
                finalStartX = -1;
                finalStartY = -1;
                finalEndX = -1;
                finalEndY = -1;
                isSelected = false;
                JOptionPane.showMessageDialog(this, "Shares generated successfully.", "Success", 1);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please select region first by pressing the select region button.", "Error", 0);
        }
    }

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Menu", 0);
        if (choice == 0) {
            System.exit(1);
        }
    }

    private void laodImageMenuActionPerformed(java.awt.event.ActionEvent evt) {
        loadImageActionPerformed(evt);
    }

    private void clearMenuActionPerformed(java.awt.event.ActionEvent evt) {
        clearActionPerformed(evt);
    }

    private void exitMenuActionPerformed(java.awt.event.ActionEvent evt) {
        exitActionPerformed(evt);
    }

    private void shareCombinerMenuActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
        ShareCombiner shareCombiner = new ShareCombiner();
        shareCombiner.setVisible(true);

        //LoginPage loginPage = new LoginPage();
        //loginPage.setVisible(true);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            Registration registration = new Registration();
            registration.setVisible(true);
        } else {
            username = args[0];
            pin = args[1];
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Biocrypt().setVisible(true);
                }
            });
        }
    }


    private javax.swing.JButton clear;
    private javax.swing.JMenuItem clearMenu;
    private javax.swing.JMenu combineMenu;
    private javax.swing.JButton exit;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton generateShares;
    private javax.swing.JPanel imagePanel;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem laodImageMenu;
    private javax.swing.JButton loadImage;
    private javax.swing.JComboBox numberOFShares;
    private javax.swing.JComboBox participant;
    private javax.swing.JButton selectRegion;
    private javax.swing.JMenuItem shareCombinerMenu;

}
