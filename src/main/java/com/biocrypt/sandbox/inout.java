package com.biocrypt.sandbox;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import javax.imageio.*;
import java.io.*;
import java.util.*;
import java.awt.Color;

public class inout {

    static int sharecnt = 0;
    static Scanner read = new Scanner(System.in);

    public static void main(String args[]) throws IOException {
        long startTime = System.currentTimeMillis();
        BufferedImage img = loadImage("src/main/resources/input/input_pqr.png");
        System.out.println(img.getColorModel()+"color model :");
        int Nshares;
        int w = img.getWidth();
        int h = img.getHeight();
        img = alter.monochrome(img);

        saveImage(img, "monochrome_image", 1);
        int[][] pixels = encrypt(img);
        System.out.println("Hello ! Welcome to Progressive Visual Cryptography\n Enter the number of shares :");
        Nshares = read.nextInt();
        int[][][] Shares = new int[Nshares][w][h];
        Shares = makeShares(pixels, Nshares);
        ProgressiveVC(Shares);
        long endTime = System.currentTimeMillis();
        System.out.println("Total elapsed time in execution is:" + (endTime - startTime));
    }

    static BufferedImage loadImage(String filename) {
        BufferedImage in;
        try {
            File filein = new File(filename);
            in = ImageIO.read(filein);
        } catch (Exception ex) {
            in = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        }
        return in;
    }

    static boolean saveImage(BufferedImage pic, String filename, int format) {
        boolean valid = false;

        String type = "png";
        if (format == 1) {
            type = "jpg";
        }
        filename.trim();
        filename = filename + sharecnt + "." + type;
        try {
            File fileout = new File(filename);
            ImageIO.write(pic, type, fileout);
            valid = true;
        } catch (Exception ex) {
            valid = false;
        }
        sharecnt++;
        return valid;

    }

    public static int[][] encrypt(BufferedImage pic) {
        Raster image_raster = pic.getData();
        int w = pic.getWidth();
        int h = pic.getHeight();

        int[][] original; // Where we'll put the image pixels

        //get pixel by pixel
        int[] pixel = new int[3];
        int[] buffer = new int[3];

        // declaring the size of arrays
        original = new int[image_raster.getWidth()][image_raster.getHeight()];


        //get the image in the array
        // MyThread thread = new MyThread();
        //thread.start();
        for (int i = 0; i < image_raster.getWidth(); i++) {
            for (int j = 0; j < image_raster.getHeight(); j++) {
                pixel = image_raster.getPixel(i, j, buffer);
                original[i][j] = pixel[0];

                if (original[i][j] == 0) {
                    original[i][j] = 0;
                } else {
                    original[i][j] = 1;
                }

            }

        }

        return original;
    }

    public static int[][][] makeShares(int[][] pixels, int Nshares) throws IOException {
        int[][] cZero = new int[Nshares][Nshares];
        int[][] cOne = new int[Nshares][Nshares];
        int width = pixels[0].length;
        int height = pixels.length;
        System.out.println("Width : " + width);
        System.out.println("Height : " + height);
        int[][][] Shares = new int[Nshares][width][height];
        /* -----------------------------------------Sharing Matrices---------------------------------------*/
        for (int i = 0; i < Nshares; i++) {
            cZero[0][i] = 1;

        }

        for (int i = 1; i < Nshares; i++) {
            for (int j = 0; j < Nshares; j++) {
                cZero[i][j] = 0;

            }

        }

        for (int i = 0; i < Nshares; i++) {
            for (int j = 0; j < Nshares; j++) {
                if (i == j) {
                    cOne[i][i] = 1;
                } else {
                    cOne[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < Nshares; i++) {
            for (int j = 0; j < Nshares; j++) {
                System.out.print("\t" + cZero[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < Nshares; i++) {
            for (int j = 0; j < Nshares; j++) {
                System.out.print("\t" + cOne[i][j]);
            }
            System.out.println();
        }

        /* -----------------------------------------PVC algorithm---------------------------------------*/
        int m = 1;
        Random rd = new Random();
        int rand;
        int ctr = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                m = 0;
                rand = rd.nextInt(Nshares);
                while (m < Nshares) {
                    if (pixels[i][j] == 0) {
                        Shares[m][i][j] = cZero[rand][m];
                    } else {
                        Shares[m][i][j] = cOne[rand][m];
                    }
                    m++;
                }

            }
        }
        /* -----------------------------------------Shares---------------------------------------*/
        for (int i = 0; i < Nshares; i++) {
            getImageFromArray(Shares[i], width, height);
        }
        return Shares;

    }

    public static void getImageFromArray(int[][] pixels, int width, int height) {


        Picture pic = new Picture(width, height);
        int k = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (pixels[i][j] == 0) {
                    k++;
                    pic.set(i, j, Color.BLACK);
                } else {
                    pic.set(i, j, Color.WHITE);
                }

            }

        }
        System.out.println(k);
        pic.save("share" + sharecnt + ".jpg");
        //pic.show();
        sharecnt++;
    }

    public static int[][] convert(int[][][] Shares, int width, int height, int m) {
        int[][] share = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                share[i][j] = Shares[m][i][j];

            }
        }
        return share;
    }

    public static void printArray(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print(" " + a[i][j]);
            }
            System.out.println("\n");
        }
    }

    public static void decrypt(int width, int height, int Nshares, int[][][] shares) {
        int m = 0;
        int[][] pic = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                m = 0;
                // rand = rd.nextInt(Nshares);
                while (m < Nshares) {

                    if (shares[m][i][j] == 1) {
                        pic[i][j] = 1;
                        break;
                    }

                    m++;
                }

            }
        }
        getImageFromArray(pic, width, height);
    }

    public static void ProgressiveVC(int[][][] Shares) {
        int k;
        int choice;
        int NShares = Shares.length;
        int w = Shares[0].length;
        int h = Shares[0][0].length;
        int[][][] kshares = new int[NShares][w][h];
        boolean flag = true;
        for (int i = 0; i < NShares; i++) {
            for (k = 0; k <= i; k++) {
                kshares[k] = Shares[k];
            }
            decrypt(w, h, k, kshares);
        }
    }
    static class MyThread extends Thread
    {
        public void run ()
        {
     /* for (int count = 1, row = 1; row < 20; row++, count++)
      {
           for (int i = 0; i < count; i++)
                System.out.print ('*');
           System.out.print ('\n');
      }*/

        }
    }
}