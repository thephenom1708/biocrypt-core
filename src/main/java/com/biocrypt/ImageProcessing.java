package com.biocrypt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

public class ImageProcessing {

    String username;

    BufferedImage input;
    int startX, startY, endX, endY;
    int width, height;
    ArrayList<BufferedImage> shares;
    Color whiteColor;
    Color blackColor;
    final int BLACK;
    ArrayList<Integer> permut;
    BufferedImage copy;


    int[][] basisWhite2x4 = { { 1, 0, 0, 1 },
            { 1, 0, 0, 1 },
            { 1, 0, 0, 1 },
            { 1, 0, 0, 1 } };

    int[][] basisBlack2x4 = { { 1, 0, 0, 1 },
            { 1, 1, 0, 0 },
            { 0, 1, 1, 0 },
            { 1, 0, 1, 0 } };

    int[][] basisWhite2x3 = { { 1, 0, 0, 1 },
            { 1, 0, 0, 1 },
            { 1, 0, 0, 1 } };

    int[][] basisBlack2x3 = { { 1, 0, 0, 1 },
            { 0, 1, 1, 0 },
            { 1, 0, 1, 0 } };

//    int[][] basisWhite2x3 = { { 1, 0, 0, 0 },
//            { 0, 0, 0, 0 },
//            { 0, 1, 0, 0 } };
//
//    int[][] basisBlack2x3 = { { 1, 1, 1, 1 },
//            { 1, 0, 0, 0 },
//            { 1, 1, 1, 1 } };


    int[][] basisWhite3x3 = { { 0, 0, 1, 1 },
            { 0, 1, 0, 1 },
            { 0, 1, 1, 0 } };

    int[][] basisBlack3x3 = { { 1, 1, 0, 0 },
            { 1, 0, 1, 0 },
            { 1, 0, 0, 1 } };


    int[][] basisWhite3x4 = { { 0, 0, 0, 1, 1, 1 },
            { 0, 0, 1, 0, 1, 1 },
            { 0, 0, 1, 1, 0, 1 },
            { 0, 0, 1, 1, 1, 0 } };

    int[][] basisBlack3x4 = { { 1, 1, 1, 0, 0, 0 },
            { 1, 1, 0, 1, 0, 0 },
            { 1, 1, 0, 0, 1, 0 },
            { 1, 1, 0, 0, 0, 1 } };


    int[][] basisWhite4x4 = { { 0, 0, 0, 1, 1, 1 },
            { 0, 0, 1, 0, 1, 1 },
            { 0, 0, 1, 1, 0, 1 },
            { 0, 0, 1, 1, 1, 0 } };

    int[][] basisBlack4x4 = { { 1, 1, 0, 0, 0, 1 },
            { 0, 1, 0, 1, 0, 1 },
            { 0, 1, 0, 0, 1, 1 },
            { 0, 1, 0, 1, 0, 1 } };


    int[][] basisWhite2x2 = { { 1, 0, 0, 1 },
            { 1, 0, 0, 1 } };

    int[][] basisBlack2x2 = { { 1, 0, 0, 1 },
            { 0, 1, 1, 0 } };


    // ImageProcessing class
    public ImageProcessing(BufferedImage input, int startX, int startY, int endX, int endY, String username) {
        // input image
        this.username = username;

        this.input = input;
        // start and end coordinate of secret area 
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        // shares array list
        shares = new ArrayList<>();
        // width of share
        width = Math.abs(startX - endX);
        // height of share
        height = Math.abs(startY - endY);

        // make width even
        if (width % 2 != 0) {
            width++;
            this.endX++;
        }
        // make height even
        if (height % 2 != 0) {
            height++;
            this.endY++;
        }
        // color
        whiteColor = new Color(0, 0, 0, 0);
        blackColor = new Color(0, 0, 0, 255);
        // for the permutation of matrix columns
        permut = new ArrayList<>();
        permut.add(0);
        permut.add(1);
        permut.add(2);
        permut.add(3);
        BLACK = blackColor.getRGB();
        System.out.println(startX + "   " + startY + " , " + endX + "   " + endY + "   " + height + "   " + width);
        makeItBinary();
    }

    // change the image to binary
    public void makeItBinary() {

        try {
            BufferedImage img = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            for (int i = 0; i < input.getHeight(); i++) {
                for (int j = 0; j < input.getWidth(); j++) {
                    Color c = new Color(input.getRGB(j, i));
                    Color temp = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);
                    img.setRGB(j, i, temp.getRGB());
                }
            }

            img.setRGB(0, 0, startX);
            img.setRGB(input.getWidth() - 1, 0, startY);

            for (int i = startY; i < endY; i++) {
                for (int j = startX; j < endX; j++) {
                    Color temp = new Color(input.getRGB(j, i));
                    if (temp.getRed() > 230) {
                        Color c = new Color(0, 0, 0, 0);
                        // show color is the data we are placing in the origional image on which we have to do processing
                        Color show = new Color(255, 255, 255, 255);
                        input.setRGB(j, i, show.getRGB());
                        img.setRGB(j, i, c.getRGB());
                    } else {
                        Color c = new Color(0, 0, 0, 255);
                        Color show = new Color(0, 0, 0, 0);
                        input.setRGB(j, i, show.getRGB());
                        img.setRGB(j, i, c.getRGB());
                    }
                }
            }
            shares.add(img);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "Exception in makeItBinary()");
            e.printStackTrace();
        }
    }

    // method to count number of black pixels in block 
    private int countNumberOfBlackPixelInBlock(int row, int col) {
        int count = 0;
        // same pixel
        Color temp = new Color(input.getRGB(col, row));
        count += temp.getRed() / 255;
        // right
        temp = new Color(input.getRGB(col + 1, row));
        count += temp.getRed() / 255;
        //bottom
        temp = new Color(input.getRGB(col, row + 1));
        count += temp.getRed() / 255;
        //bottom right
        temp = new Color(input.getRGB(col + 1, row + 1));
        count += temp.getRed() / 255;
        count = 4 - count;
        return count;
    }

    // generate black block in share
    private void generateBlackBlock(int[][] blackMat, int row, int col) {
        Collections.shuffle(permut);
        ArrayList<Integer> rowSuffler = new ArrayList<>();
        for (int i = 0; i < blackMat.length; i++) {
            rowSuffler.add(i);
        }
        Collections.shuffle(rowSuffler);
        for (int i = 1; i < blackMat.length; i++) {
            shares.get(i).setRGB(col, row, blackMat[rowSuffler.get(i)][permut.get(0)] * BLACK);
            shares.get(i).setRGB(col + 1, row, blackMat[rowSuffler.get(i)][permut.get(1)] * BLACK);
            shares.get(i).setRGB(col, row + 1, blackMat[rowSuffler.get(i)][permut.get(2)] * BLACK);
            shares.get(i).setRGB(col + 1, row + 1, blackMat[rowSuffler.get(i)][permut.get(3)] * BLACK);
        }
        shares.get(0).setRGB(startX + col, startY + row, blackMat[rowSuffler.get(0)][permut.get(0)] * BLACK);
        shares.get(0).setRGB(startX + col + 1, startY + row, blackMat[rowSuffler.get(0)][permut.get(1)] * BLACK);
        shares.get(0).setRGB(startX + col, startY + row + 1, blackMat[rowSuffler.get(0)][permut.get(2)] * BLACK);
        shares.get(0).setRGB(startX + col + 1, startY + row + 1, blackMat[rowSuffler.get(0)][permut.get(3)] * BLACK);

    }

    // generate white block in shares
    private void generateWhiteBlock(int[][] whiteMat, int row, int col) {
        Collections.shuffle(permut);

        for (int i = 1; i < whiteMat.length; i++) {
            shares.get(i).setRGB(col, row, whiteMat[i][permut.get(0)] * BLACK);
            shares.get(i).setRGB(col + 1, row, whiteMat[i][permut.get(1)] * BLACK);
            shares.get(i).setRGB(col, row + 1, whiteMat[i][permut.get(2)] * BLACK);
            shares.get(i).setRGB(col + 1, row + 1, whiteMat[i][permut.get(3)] * BLACK);
        }
        shares.get(0).setRGB(startX + col, startY + row, whiteMat[0][permut.get(0)] * BLACK);
        shares.get(0).setRGB(startX + col + 1, startY + row, whiteMat[0][permut.get(1)] * BLACK);
        shares.get(0).setRGB(startX + col, startY + row + 1, whiteMat[0][permut.get(2)] * BLACK);
        shares.get(0).setRGB(startX + col + 1, startY + row + 1, whiteMat[0][permut.get(3)] * BLACK);
    }


    // check whether the pixel is at boundary or not
    private boolean isAtBoundary(int i, int j) {
        return i == startY || j == startX || i == endY - 2 || j == endX - 2;
    }

    // check whether the pixel is diagonally black or not
    private boolean isDiagonallyBlack(int i, int j) {
        Color c1 = new Color(input.getRGB(j, i));
        Color c2 = new Color(input.getRGB(j + 1, i));
        Color c3 = new Color(input.getRGB(j, i + 1));
        Color c4 = new Color(input.getRGB(j + 1, i + 1));
        if (c4.getRed() == 255 && c1.getRed() == 255) {
            return true;
        }
        return c2.getRed() == 255 && c3.getRed() == 255;
    }

    // count neigbour black pixels of block
    private int neighborBlackPixelOfBlock(int h, int w) {
        if (h - 1 < 0 || h + 2 >= input.getHeight()) {
            return 0;
        }
        if (w + 2 >= input.getWidth() || w - 1 < 0) {
            return 0;
        }

        int count = 0;
        //top
        Color temp = new Color(input.getRGB(w - 1, h));
        count += temp.getRed() / 255;
        temp = new Color(input.getRGB(w - 1, h + 1));
        count += temp.getRed() / 255;

        //bottom
        temp = new Color(input.getRGB(w + 2, h));
        count += temp.getRed() / 255;
        temp = new Color(input.getRGB(w + 2, h + 1));
        count += temp.getRed() / 255;

        //left
        temp = new Color(input.getRGB(w, h - 1));
        count += temp.getRed() / 255;
        temp = new Color(input.getRGB(w + 1, h - 1));
        count += temp.getRed() / 255;

        //right
        temp = new Color(input.getRGB(w, h + 2));
        count += temp.getRed() / 255;
        temp = new Color(input.getRGB(w + 1, h + 2));
        count += temp.getRed() / 255;

        count = 8 - count;
        return count;
    }

    // count neighbour black pixel of pixel
    private int neighborBlackPixelsOfPixel(int h, int w) {

        if (h - 1 < 0 || h + 1 > input.getHeight()) {
            return 0;
        }
        if (w + 1 >= input.getWidth() || w - 1 < 0) {
            return 0;
        }

        int count = 0;
        //top
        Color temp = new Color(input.getRGB(w - 1, h - 1));
        count += temp.getRed() / 255;
        temp = new Color(input.getRGB(w - 1, h));
        count += temp.getRed() / 255;
        temp = new Color(input.getRGB(w - 1, h + 1));
        count += temp.getRed() / 255;

        //bottom
        temp = new Color(input.getRGB(w + 1, h - 1));
        count += temp.getRed() / 255;
        temp = new Color(input.getRGB(w + 1, h));
        count += temp.getRed() / 255;
        temp = new Color(input.getRGB(w + 1, h + 1));
        count += temp.getRed() / 255;

        //left
        temp = new Color(input.getRGB(w, h - 1));
        count += temp.getRed() / 255;

        //right
        temp = new Color(input.getRGB(w, h + 1));
        count += temp.getRed() / 255;

        count = 8 - count;
        return count;
    }

    // count neighbor black pixel of black pixel in block
    private int neighborBlackOfBlackPixelsInBlock(int h, int w) {
        int count = 0;
        Color c1 = new Color(input.getRGB(w, h));
        Color c2 = new Color(input.getRGB(w + 1, h));
        Color c3 = new Color(input.getRGB(w, h + 1));
        Color c4 = new Color(input.getRGB(w + 1, h + 1));

        if (c1.getRed() == 255) {
            count += neighborBlackPixelsOfPixel(h, w);
        } else {
            count += neighborBlackPixelsOfPixel(h, w + 1);
        }

        if (c3.getRed() == 255) {
            count += neighborBlackPixelsOfPixel(h + 1, w);
        } else {
            count += neighborBlackPixelsOfPixel(h + 1, w + 1);
        }
        return count;
    }

    // generate share method take two basis matrices as input one for black box and one for white box
    // input: Basis matrices for white and black pixel
    // Output: Shares for the secret images
    public void generateShares(int[][] whiteMat, int[][] blackMat, int n) {
        for (int i = 1; i < whiteMat.length; i++) {
            shares.add(new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR));
        }

        for (int i = 0; i < height; i += 2) {
            for (int j = 0; j < width; j += 2) {

                switch (countNumberOfBlackPixelInBlock(startY + i, startX + j)) {
                    case 0:
                    case 1:
                        generateWhiteBlock(whiteMat, i, j);
                        break;
                    case 2:
                        if (isAtBoundary(i, j)) {
                            Random r = new Random();
                            int k = Math.abs(r.nextInt() % 2);
                            if (k == 0) {
                                generateBlackBlock(blackMat, i, j);
                            } else {
                                generateWhiteBlock(whiteMat, i, j);
                            }
                        } else {
                            if (isDiagonallyBlack(i, j)) {
                                if (neighborBlackOfBlackPixelsInBlock(i, j) > 8) {
                                    generateBlackBlock(blackMat, i, j);
                                } else {
                                    generateWhiteBlock(whiteMat, i, j);
                                }
                            } else {
                                if (neighborBlackPixelOfBlock(i, j) > 4) {
                                    generateBlackBlock(blackMat, i, j);
                                } else {
                                    generateWhiteBlock(whiteMat, i, j);
                                }
                            }
                        }
                        break;
                    case 3:
                    case 4:
                        generateBlackBlock(blackMat, i, j);
                }
            }
        }
        try {
            List<String> serverList = Url.nodes;
            int nodeCount = serverList.size();
            String url;
            String param;
            String response = "";
            int serverIndex = 0;
            int shareIndex;
            for (int i = 0; i < n; i++) {

                if(i >= nodeCount && i % shares.size() == 0)
                    shareIndex = 1;
                else
                    shareIndex = i % shares.size();

//                System.out.println("Share: " + shareIndex);
                String fileName = Url.SHARE_PATH + "share" + (i + 1) + ".png";
                ImageIO.write(shares.get(shareIndex), "PNG", new File(fileName));

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(shares.get(shareIndex), "PNG", outputStream);
                //byte[] bytes = outputStream.toByteArray();
                String encodedImage = Base64.getEncoder().encodeToString(outputStream.toByteArray());

                url = "http://" + serverList.get(i % nodeCount) + ":" + Url.PORT + Url.CREATE_SHARE_URL;
                param = "share_number=" + (i + 1) + "&" + "share_data=" + URLEncoder.encode(encodedImage, StandardCharsets.UTF_8) + "&" + "username=" + username;
                System.out.println("Sending to " + serverList.get(i % nodeCount));

                HttpSendData send1 = new HttpSendData(url, param);
                try {
                    response = send1.sendPOST();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(response);

                serverIndex++;
                if(serverIndex == nodeCount)
                    serverIndex = 0;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    // create a complete black block
    private void createBlackBlock(int i, int j) {
        Color show = new Color(0, 0, 0, 0);
        input.setRGB(startX + j, startY + i, show.getRGB());
        input.setRGB(startX + j + 1, startY + i, show.getRGB());
        input.setRGB(startX + j, startY + i + 1, show.getRGB());
        input.setRGB(startX + j + 1, startY + i + 1, show.getRGB());

    }

    // create a complete white block
    private void createWhiteBlock(int i, int j) {
        Color show = new Color(255, 255, 255, 255);
        input.setRGB(startX + j, startY + i, show.getRGB());
        input.setRGB(startX + j + 1, startY + i, show.getRGB());
        input.setRGB(startX + j, startY + i + 1, show.getRGB());
        input.setRGB(startX + j + 1, startY + i + 1, show.getRGB());
    }


    // count number of black pixels in 2*3 block 
    private int countNumberOfBlackPixelInExtendedBlock(int row, int col) {
        int count = 0;

        if (row + 2 >= endY)
            return 0;
        if (col + 3 >= endX)
            return 0;

        // same pixel
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                Color temp = new Color(input.getRGB(col + j, row + i));
                count += temp.getRed() / 255;
            }
        }
        count = 6 - count;
        return count;
    }


    // doPreprocessing 
    void doPreprocessing() {
        copy = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < input.getHeight(); i++) {
            for (int j = 0; j < input.getWidth(); j++) {
                Color c = new Color(input.getRGB(j, i));
                Color temp = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);
                copy.setRGB(j, i, temp.getRGB());
            }
        }
        for (int i = 0; i < height; i += 2) {
            for (int j = 0; j < width; j += 2) {

                switch (countNumberOfBlackPixelInBlock(startY + i, startX + j)) {
                    case 0:
                    case 1:
                        createWhiteBlock(i, j);
                        break;
                    case 2:
                        if (isAtBoundary(i, j)) {
                            Random r = new Random();
                            int k = Math.abs(r.nextInt() % 2);
                            if (k == 0) {
                                createBlackBlock(i, j);
                            } else {
                                createWhiteBlock(i, j);
                            }
                        } else {
                            if (isDiagonallyBlack(i, j)) {
                                if (neighborBlackOfBlackPixelsInBlock(i, j) > 8) {
                                    createBlackBlock(i, j);
                                } else {
                                    createWhiteBlock(i, j);
                                }
                            } else {
                                if (neighborBlackPixelOfBlock(i, j) > 4) {
                                    createBlackBlock(i, j);
                                } else {
                                    createWhiteBlock(i, j);
                                }
                            }
                        }
                        break;
                    case 3:
                    case 4:
                        createBlackBlock(i, j);
                }
            }
        }

        copy.setRGB(0, 0, startX);
        copy.setRGB(input.getWidth() - 1, 0, startY);
        shares.remove(0);
        shares.add(copy);
    }

    // generate extended black block
    private void generateExtendedBlackBlock(int[][] blackMat, int row, int col) {
        if (col + 3 >= width || row + 2 >= height)
            return;

        ArrayList<Integer> rowSuffler = new ArrayList<>();
        for (int i = 0; i < blackMat.length; i++) {
            rowSuffler.add(i);
        }
        permut = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            permut.add(i);
        }
        Collections.shuffle(permut);
        Collections.shuffle(rowSuffler);

        for (int i = 0; i < blackMat.length; i++) {
            int count = 0;
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    if (i == 0) {
                        shares.get(0).setRGB(startX + col + k, startY + row + j, blackMat[rowSuffler.get(i)][permut.get(count++)] * BLACK);
                    } else {
                        shares.get(i).setRGB(col + k, row + j, blackMat[rowSuffler.get(i)][permut.get(count++)] * BLACK);
                    }
                }
            }
        }
    }

    // generate extended white block
    private void generateExtendedWhiteBlock(int[][] whiteMat, int row, int col) {

        if (col + 3 >= width || row + 2 >= height)
            return;

        ArrayList<Integer> rowSuffler = new ArrayList<>();
        for (int i = 0; i < whiteMat.length; i++) {
            rowSuffler.add(i);
        }
        permut = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            permut.add(i);
        }
        Collections.shuffle(permut);
        Collections.shuffle(rowSuffler);


        for (int i = 0; i < whiteMat.length; i++) {
            int count = 0;
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    if (i == 0) {
                        shares.get(0).setRGB(startX + col + k, startY + row + j, whiteMat[rowSuffler.get(i)][permut.get(count++)] * BLACK);
                    } else {
                        shares.get(i).setRGB(col + k, row + j, whiteMat[rowSuffler.get(i)][permut.get(count++)] * BLACK);
                    }
                }
            }
        }
    }

    // generate shares after preprocessing of 2*2 blocks
    public void generateShareAfterPreProcessing(int[][] whiteMat, int[][] blackMat) throws IOException {
        doPreprocessing();
        for (int i = 1; i < whiteMat.length; i++) {
            shares.add(new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR));
        }

        for (int i = 0; i < height; i += 2) {
            for (int j = 0; j < width; j += 3) {

                switch (countNumberOfBlackPixelInExtendedBlock(startY + i, startX + j)) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        generateExtendedWhiteBlock(whiteMat, i, j);
                        break;
                    case 4:
                    case 5:
                    case 6:
                        generateExtendedBlackBlock(blackMat, i, j);
                }
            }
        }
        try {
            //write all shares now
            List<String> serverList = Url.nodes;
            int nodeCount = serverList.size();
            String url;
            String param;
            String response = "";
            int serverIndex = 0;

            for (int i = 0; i < shares.size(); i++) {
                String fileName = Url.SHARE_PATH + "share" + (i + 1) + ".png";
                ImageIO.write(shares.get(i), "PNG", new File(fileName));

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(shares.get(i), "PNG", outputStream);
                //byte[] bytes = outputStream.toByteArray();
                String encodedImage = Base64.getEncoder().encodeToString(outputStream.toByteArray());


                url = "http://" + serverList.get(i) + ":" + Url.PORT + Url.CREATE_SHARE_URL;
                param = "share_number=" + (i + 1) + "&" + "share_data=" + URLEncoder.encode(encodedImage, StandardCharsets.UTF_8) + "&" + "username=" + username;
                System.out.println("Sending to " + serverList.get(i));

                HttpSendData send1 = new HttpSendData(url, param);
                try {
                    response = send1.sendPOST();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(response);

                serverIndex++;
                if (serverIndex == nodeCount)
                    serverIndex = 0;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    // generate k out on n shares
    public boolean generateKoutOfNShares(int k, int n) {

        if (k == 2 && n == 2) {
            generateShares(basisWhite2x2, basisBlack2x2, n);
        } else if (k == 2 && n == 3) {
            generateShares(basisWhite2x3, basisBlack2x3, n);
        } else if (k == 2 && n == 4) {
            generateShares(basisWhite2x4, basisBlack2x4, n);
        } else if (k == 3 && n == 3) {
            generateShares(basisWhite3x3, basisBlack3x3, n);
        } else if (k == 3 && n == 4) {
//            generateShareAfterPreProcessing(basisWhite3x4, basisBlack3x4);
            generateShares(basisWhite2x3, basisBlack2x3, n);
        } else if (k == 4 && n == 4) {
//            generateShareAfterPreProcessing(basisWhite4x4, basisBlack4x4);
            generateShares(basisWhite2x3, basisBlack2x3, n);
        } else if(n > 4) {
            generateShares(basisWhite2x3, basisBlack2x3, n);
        }
        return true;
    }


    public void generateShares_direct(int[][] whiteMat, int[][] blackMat) {
        for (int i = 1; i < whiteMat.length; i++) {
            shares.add(new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR));
        }

        for (int i = 0; i < height; i += 2) {
            for (int j = 0; j < width; j += 2) {

                switch (countNumberOfBlackPixelInBlock(startY + i, startX + j)) {
                    case 0:
                    case 1:
                        generateWhiteBlock(whiteMat, i, j);
                        break;
                    case 2:
                        if (isAtBoundary(i, j)) {
                            Random r = new Random();
                            int k = Math.abs(r.nextInt() % 2);
                            if (k == 0) {
                                generateBlackBlock(blackMat, i, j);
                            } else {
                                generateWhiteBlock(whiteMat, i, j);
                            }
                        } else {
                            if (isDiagonallyBlack(i, j)) {
                                if (neighborBlackOfBlackPixelsInBlock(i, j) > 8) {
                                    generateBlackBlock(blackMat, i, j);
                                } else {
                                    generateWhiteBlock(whiteMat, i, j);
                                }
                            } else {
                                if (neighborBlackPixelOfBlock(i, j) > 4) {
                                    generateBlackBlock(blackMat, i, j);
                                } else {
                                    generateWhiteBlock(whiteMat, i, j);
                                }
                            }
                        }
                        break;
                    case 3:
                    case 4:
                        generateBlackBlock(blackMat, i, j);
                }
            }
        }
        System.out.println("Size: " + shares.size());
    }


    public ArrayList<BufferedImage> generateKoutOfNShares_direct(int k, int n) {
        if (k == 2 && n == 2) {
            generateShares_direct(basisWhite2x2, basisBlack2x2);
        } else if (k == 2 && n == 3) {
            generateShares_direct(basisWhite2x3, basisBlack2x3);
        } else if (k == 2 && n == 4) {
            generateShares_direct(basisWhite2x4, basisBlack2x4);
        } else if (k == 3 && n == 3) {
            generateShares_direct(basisWhite3x3, basisBlack3x3);
        } else if (k == 3 && n == 4) {
            generateShares_direct(basisWhite3x4, basisBlack3x4);
        } else if (k == 4 && n == 4) {
            generateShares_direct(basisWhite4x4, basisBlack4x4);
        } else if(n > 4) {
            generateShares_direct(basisWhite2x3, basisBlack2x3);
        }
        return shares;
    }

}



