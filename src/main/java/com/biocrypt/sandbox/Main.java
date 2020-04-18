package com.biocrypt.sandbox;

import com.biocrypt.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.util.Random;


public class Main {

    final static BufferedImage img = loadImage("src/main/resources/input/input_pqr.png");
    final static int width = img.getWidth();
    final static int height = img.getHeight();
    static int sharecnt = 1;

    /*Step II: Take the number of shares (n) and minimum number of shares (k) to be taken to reconstruct the image.
    k must be less than or equal to n.
    Step III: Calculate recons=(n-k)+1. */
    final static int n = 2;
    final static int k = 2;
    final static int recons = (n - k) + 1; //4
    static int loop = 0;
    static int[][][][] Binary_values_pixels = new int[n][width][height][8];
    static ImageKN original = new ImageKN();


    public static void main(String[] args) throws Exception {
        Test test = new Test();
        boolean match = test.getMatch("src/main/resources/input/input_pqr.png", "src/knshares12.png");


        ImageKN binaryvalues = new ImageKN();
        ImageKN[] nShares = new ImageKN[n];
        for (int i = 0; i < n; i++) {
            nShares[i] = new ImageKN();
        }
        // int[] a = {1,0,1,1,0,0,0,1};
        //int[] b = {1,1,1,0,1,0,0,1};
        int[] bin = { 1, 0, 0, 0, 0, 0, 0, 0 };
        printArray(bin);
        //int x = convertArraytoNumber(bin);
        //  System.out.println("Check expected: 128........"+x);
        // OR(2,4);
        binaryvalues = RGBApixelscolors(img);
        //CHECKED binaryvalues.printArray(binaryvalues.redDecValues);

        int[][][] redValuesOfNShares = new int[n][width][height];
        int[][][] greenValuesOfNShares = new int[n][width][height];
        int[][][] blueValuesOfNShares = new int[n][width][height];
        int[][][] alphaValuesOfNShares = new int[n][width][height];

        //  binaryvalues.printArray(binaryvalues.redDecValues);
        redValuesOfNShares = SettingOneInReconsShares(binaryvalues.redDecValues);
        greenValuesOfNShares = SettingOneInReconsShares(binaryvalues.greenDecValues);
        blueValuesOfNShares = SettingOneInReconsShares(binaryvalues.blueDecValues);
        alphaValuesOfNShares = SettingOneInReconsShares(binaryvalues.alphaDecValues);
        System.out.println("Shares generated");
        for (int i = 0; i < n; i++) {
            nShares[i].redDecValues = redValuesOfNShares[i];
            nShares[i].greenDecValues = greenValuesOfNShares[i];
            nShares[i].blueDecValues = blueValuesOfNShares[i];
            nShares[i].alphaDecValues = alphaValuesOfNShares[i];

        }
        //     checkDifference(original.redDecValues, nShares[2].redDecValues);
        //    nShares[1].printArray(nShares[1].redDecValues);
        System.out.println("Shares encapsulated..Heading for Image Creation");
        //Shares are ready now.
        for (int i = 0; i < n; i++) {
            arrayToImage(nShares[i]);
        }
        decryptionKNSharing(nShares);

    }

    public static void checkDifference(int[][] original, int check[][]) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int differ = original[i][j] - check[i][j];
                System.out.println("Differnce:" + differ);
            }
        }
    }

    public static void arrayToImage(ImageKN img) {
        Picture pic = new Picture(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                int r = Integer.parseInt(("" + img.redDecValues[i][j]));
                int g = Integer.parseInt(("" + img.greenDecValues[i][j]));
                int b = Integer.parseInt(("" + img.blueDecValues[i][j]));
                int a = Integer.parseInt(("" + img.alphaDecValues[i][j]));

                Color color = new Color(r, g, b);

                pic.set(i, j, color);

            }
        }
        pic.save("knshares" + sharecnt + ".png");
        sharecnt++;

    }


    /**********************Setting One in Recons Shares *************************/
    public static int[][][] SettingOneInReconsShares(int[][] Original_binary_pixels) {

        int[][][] manipulator = new int[width][height][8];
        //  printArray(Original_binary_pixels);
        int[] rand = new int[recons]; // Array of random recons numbers for recons shares
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    for (int x = 0; x < 8; x++) {
                        Binary_values_pixels[i][j][k][x] = 0; //Initialization


                    }

                }
            }

        }

        for (int j = 0; j < width; j++) {
            for (int k = 0; k < height; k++) {

                manipulator[j][k] = retrievingSingleDigits("" + Original_binary_pixels[j][k]);

                //  System.out.println("Length : "+(retrievingSingleDigits(""+Original_binary_pixels[j][k]).length));
            }

        }
        //     System.out.println("\nNumber: "+Original_binary_pixels[0][0]);
//printArray(manipulator[0][0]);
       /* System.out.println(" padding checking :");
 for(int i = 7;i>=0;i--)

 {
     System.out.print(manipulator[0][33][i]);
  }
     System.out.println(" Original :"+Original_binary_pixels[0][33]);      */ // printArray(manipulator); // Checked
        /*******************************Initialization Ends ****************************************/
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < 8; k++) {
                    //    System.out.println(manipulator[i][j][k]);
                    if (manipulator[i][j][k] == 1) {
                        // System.out.println("Entered");
                        rand = random_place(n, recons);

                        // printArray(rand);
                        //  System.out.println(": Rand\n") ;
                        for (int l = 0; l < recons; l++) {

                            Binary_values_pixels[rand[l]][i][j][k] = 1;
                        }
                    }


                }

            }
        }
        int[][][] final_img_share = new int[n][width][height];


        //   System.out.println("Hello Hi");
        //    printArray(Binary_values_pixels); //CHECKED
               /*   for(int i=0;i<8;i++)
                  System.out.print(Binary_values_pixels[0][56][0][i]);*/
        sharecnt = 1;
        for (int i = 0; i < n; i++) {
            System.out.println("Share " + i);
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                   /*  System.out.println("value sent:");
                     System.out.println("Entered "+sharecnt);
               sharecnt++;*/
                    //    printArray(Binary_values_pixels[i][j][k]);
                    int[] num = Binary_values_pixels[i][j][k];
                     /*for(int x=0;x<8;x++)
                     {
                         System.out.println(i+" "+ j + "  "+ k+ "  "+ "Length :"+Binary_values_pixels[i][j][k].length);
                         //num[x]=  Binary_values_pixels[i][j][k][x];
                     }*/

                    // System.out.println("\nNumber : ");
                    // printArray(num);
                    final_img_share[i][j][k] = convertArraytoNumber(num);
                    //  System.out.println(final_img_share[i][j][k]);
                }
            }
        }
        // printArray(Binary_values_pixels); //CHECKED !! GIVING RIGHT
        // System.out.println("One stage");
        //    printArray(final_img_share);
        return final_img_share;
    }

    public static void printArray(int[][][] img) {

        for (int x = 0; x < width; x++) {

            for (int i = 0; i < height; i++) {

                for (int j = 7; j >= 0; j--) {
                    System.out.print(img[x][i][j]);

                }
                System.out.print(" ");
            }
            System.out.println("\n");
        }
    }

    public static int convertArraytoNumber(int[] bin) {
        // System.out.println("Entered"+sharecnt);
        //  sharecnt++;
        //    System.out.println("Entered 1 ");
        // printArray(bin);
        // System.out.print("Entered 2 ");
        int dec = 0;
        //  System.out.println("Length :"+bin.length);
        for (int i = 0; i < 8; i++) {
            // System.out.print(bin[i]);
            if (bin[i] == 1) {

                dec = dec + powerTwo(i);

            }
        }
        //  System.out.println("\nDecimal : "+dec );
        return dec;
    }

    public static int powerTwo(int n) {
        int product = 1;
        for (int i = 1; i <= n; i++) {
            product = product * 2;
        }
        return product;
    }

    public static int[] random_place(int n, int recons) {
        loop++;
        Random rd = new Random();
        int[] rand = new int[recons];
        // System.out.println("\nLoop number : "+loop );
        for (int x = 0; x < recons; x++) {
            rand[x] = 999;
        }
        int rand_int = 0;
        boolean flag = true;
        for (int i = 0; i < (recons); i++) {
            flag = true;
            while (flag) {
                //System.out.println(flag);
                flag = false;
                rand_int = rd.nextInt(n);
                for (int j = 0; j < recons; j++) {
                    //   System.out.println("Rand_int :"+rand_int);
                    // System.out.println("Rand["+j+"] :"+rand[j]);
                    if (rand_int == rand[j]) {
                        //  System.out.println(i+"Enters");
                        flag = true;
                        break;
                    }
                }


            }
            rand[i] = rand_int;
            //   System.out.println(" " + rand_int);
        }
        //printArray(rand);
        return rand;
    }

    /************************* Padding with zeroes. Returns 1D array************/
    public static int[] retrievingSingleDigits(String num) {
        int len = num.length();
        int[] store = new int[8];
//System.out.println("Entered");
        for (int i = 0; i < 8; i++) {
            store[i] = 0;

        }

        int k = 0;
//System.out.println(" String :"+num);
        for (int i = len - 1; i >= 0; i--) {
            //  System.out.println("charAt "+i +":"+num.charAt(i));
            int x = Integer.parseInt(num.charAt(i) + "");
            store[k] = x;

            k++;
        }
/*for(int i=7;i>=0;i--)
    System.out.print(store[i]);*/
        // printArray(store);
        return store;
    }


    /*************** Loads a image given the pathname ***************************/
    static BufferedImage loadImage(String filename) {
        BufferedImage in;
        try {
            File filein = new File(filename);
            in = ImageIO.read(filein);
        } catch (Exception ex) {
            in = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        return in;
    }

    /**************************************************************************************/
    /**********************************Image to R, G, B, A arrays. - four arrays ***********/
    public static ImageKN RGBApixelscolors(BufferedImage img) {
        Raster writeableRaster = img.getData();
        ColorModel colorModel = img.getColorModel();
        System.out.println(colorModel);
        ImageKN image = new ImageKN();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Object data = writeableRaster.getDataElements(i, j, null);
                String r = "" + colorModel.getRed(data);
                String g = "" + colorModel.getGreen(data);
                String b = "" + colorModel.getBlue(data);
                String a = "" + colorModel.getAlpha(data);
                // String rgb = ""+colorModel.getRGB(data);
                //System.out.println("RGB : "+rgb +"r :"+r+"  g:  "+g+"b:  "+b+"a:  "+a);
                //ImageKN original = new ImageKN();
                original.redDecValues[i][j] = Integer.parseInt(r);
                original.blueDecValues[i][j] = Integer.parseInt(b);
                original.greenDecValues[i][j] = Integer.parseInt(g);
                original.alphaDecValues[i][j] = Integer.parseInt(a);
                //Has binary numbers in them
                image.redDecValues[i][j] = Integer.parseInt("" + paddingNew(r));
                image.greenDecValues[i][j] = Integer.parseInt("" + paddingNew(g));
                image.blueDecValues[i][j] = Integer.parseInt("" + paddingNew(b));
                image.alphaDecValues[i][j] = Integer.parseInt("" + paddingNew(a));

            }
        }
      /*  System.out.println("Blue : ");
        image.printArray(image.blueDecValues);
        /**System.out.println("Green : ");
        image.printArray(image.greenDecValues);
        System.out.println("Red : ");
        image.printArray(image.redDecValues);
        System.out.println("Alpha : ");
        image.printArray(image.alphaDecValues);**-*/


        //arrayToImage(image);
        return image;
        //perfectly returning binary values of RGB components. CHECKED NO NEED TO CHECK AGAIN

    }

    /********************** Padding ************/
    public static String paddingNew(String num) {


        //   System.out.println("Loop : "+loop+"  Original number:"+num);
        String hex = "" + num;
        int bin = Integer.parseInt(hex);
        String numi = Integer.toBinaryString(bin);
        int len = numi.length();

        String numPad = new String();
        if (len <= 8) {
            switch (len) {
                case 1:
                    numPad = "0000000" + numi;

                    break;
                case 2:
                    numPad = "000000" + numi;
                    break;
                case 3:
                    numPad = "00000" + numi;
                    break;
                case 4:
                    numPad = "0000" + numi;
                    break;
                case 5:
                    numPad = "000" + numi;
                    break;
                case 6:
                    numPad = "00" + numi;
                    break;
                case 7:
                    numPad = "0" + numi;
                    break;
                case 8:
                    numPad = numi;
                    break;

            }
        }
        // int finalInt=Integer.parseInt(numPad);

        return numPad;
    }

    public static void printArray(int[][][][] img) {
        System.out.println("Share " + sharecnt);
        sharecnt++;
        for (int x = 0; x < n; x++) {
            for (int i = 0; i < width; i++) {

                for (int j = 0; j < height; j++) {
                    for (int k = 7; k >= 0; k--) {
                        System.out.print(img[x][i][j][k]);
                    }
                    System.out.print(" ");
                }
                System.out.println("\n");
            }
        }
    }

    public static void printArray(int[] rand) {
        for (int i = 0; i < rand.length; i++) {
            System.out.print(" " + rand[i]);
        }
        System.out.println("\n\n");

    }

    public static void printArray(int[][] binary) {

        for (int i = 0; i < binary.length; i++) {
            for (int j = 0; j < binary[i].length; j++) {
                //binInt[i][j]=(int)binary[i][j];
                System.out.print(" " + binary[i][j]);
            }
            System.out.println("\n");
        }
    }

    /********************************************************/
    public static class ImageKN {

        int[][] redDecValues = new int[width][height];
        int[][] greenDecValues = new int[width][height];
        int[][] blueDecValues = new int[width][height];
        int[][] alphaDecValues = new int[width][height];

        public static void printArray(int[][] binary) {

            for (int i = 0; i < binary.length; i++) {
                for (int j = 0; j < binary[i].length; j++) {
                    //binInt[i][j]=(int)binary[i][j];
                    System.out.print(" " + binary[i][j]);
                }
                System.out.println("\n");
            }
        }
    }

    /* public static class ImageKN {
        StringBuilder[][] redDecValues = new StringBuilder[width][height];
        StringBuilder[][] greenDecValues = new StringBuilder[width][height];
        StringBuilder[][] blueDecValues = new StringBuilder[width][height];
        StringBuilder[][] alphaDecValues = new StringBuilder[width][height];
        public static void printArray(int[][] binary) {
            for (int i = 0; i < binary.length; i++) {
                for (int j = 0; j < binary[i].length; j++) {
                    //binInt[i][j]=(int)binary[i][j];
                    System.out.print(" " + binary[i][j]);
                }
                System.out.println("\n");
            }
        }
    }*/
    public static void decryptionKNSharing(ImageKN[] nShares) {
        //    int[] rand = random_place(n, k-2);
//printArray(rand);
        ImageKN finalImage = new ImageKN();
        for (int i = 0; i < 12; i += 4) {

            for (int j = 0; j < width; j++) {
                for (int x = 0; x < height; x++) {
                    finalImage.alphaDecValues[j][x] = OR(finalImage.alphaDecValues[j][x], nShares[i].alphaDecValues[j][x]);
                    finalImage.redDecValues[j][x] = OR(finalImage.redDecValues[j][x], nShares[i].redDecValues[j][x]);
                    finalImage.blueDecValues[j][x] = OR(finalImage.blueDecValues[j][x], nShares[i].blueDecValues[j][x]);
                    finalImage.greenDecValues[j][x] = OR(finalImage.greenDecValues[j][x], nShares[i].greenDecValues[j][x]);

                }
            }
        }
        arrayToImage(finalImage);


        for (int i = 0; i < k; i++) {
            for (int j = 0; j < width; j++) {
                for (int x = 0; x < height; x++) {
                    finalImage.alphaDecValues[j][x] = OR(finalImage.alphaDecValues[j][x], nShares[i].alphaDecValues[j][x]);
                    finalImage.redDecValues[j][x] = OR(finalImage.redDecValues[j][x], nShares[i].redDecValues[j][x]);
                    finalImage.blueDecValues[j][x] = OR(finalImage.blueDecValues[j][x], nShares[i].blueDecValues[j][x]);
                    finalImage.greenDecValues[j][x] = OR(finalImage.greenDecValues[j][x], nShares[i].greenDecValues[j][x]);

                }
            }
        }
        arrayToImage(finalImage);
  /*     for(int i = 0;i < 85 ;i ++)
       {
           for(int j=0 ;j<width;j++)
           {
               for(int x = 0; x<height; x++)
               {
                       finalImage.alphaDecValues[j][x] = OR (finalImage.alphaDecValues[j][x],nShares[i].alphaDecValues[j][x]);
                    finalImage.redDecValues[j][x] = OR (finalImage.redDecValues[j][x],nShares[i].redDecValues[j][x]);
                 finalImage.blueDecValues[j][x] = OR (finalImage.blueDecValues[j][x],nShares[i].blueDecValues[j][x]);
              finalImage.greenDecValues[j][x] = OR (finalImage.greenDecValues[j][x],nShares[i].greenDecValues[j][x]);
               }
           }
       }
       arrayToImage(finalImage);
 for(int i = 0;i < n ;i ++)
       {
           for(int j=0 ;j<width;j++)
           {
               for(int x = 0; x<height; x++)
               {
                       finalImage.alphaDecValues[j][x] = OR (finalImage.alphaDecValues[j][x],nShares[i].alphaDecValues[j][x]);
                    finalImage.redDecValues[j][x] = OR (finalImage.redDecValues[j][x],nShares[i].redDecValues[j][x]);
                 finalImage.blueDecValues[j][x] = OR (finalImage.blueDecValues[j][x],nShares[i].blueDecValues[j][x]);
              finalImage.greenDecValues[j][x] = OR (finalImage.greenDecValues[j][x],nShares[i].greenDecValues[j][x]);
               }
           }
       }
       arrayToImage(finalImage);
*/
    }

    public static int OR(int a, int b) //Parameters are decimal values. Does bitwise OR and sends back the integrated binary bits in decimal representation
    {

        String z = paddingNew("" + a);
        String y = paddingNew("" + b);
        int[] x = new int[8];
        int[] w = new int[8];
//System.out.println("a:"+a);
        //      System.out.println("b:"+b);

        //  System.out.println("z:"+z);
        //  System.out.println("y:"+y);

        for (int i = 7; i >= 0; i--) {
            x[i] = Integer.parseInt("" + z.charAt(i));
            w[i] = Integer.parseInt("" + y.charAt(i));
        }


        int index = 0;
        int[] res = new int[8];
        for (int i = (x.length - 1); i >= 0; i--) {

            if (x[i] == 1 || w[i] == 1)
                res[index] = 1;
            else res[index] = 0;
            index++; // a[0] a[1] arrangement problems. hence padding needed. trailing zeroes for convertArraytoNumber.
        }

        int result = convertArraytoNumber(res);
        return result;
    }
     /* public static int[] convertDectoBinArray(int a)
      {
          String hex = "" + a;
                int bin = Integer.parseInt(hex);
                String by = Integer.toBinaryString(bin);
                paddingNew(by);

return x;
      }*/
}
