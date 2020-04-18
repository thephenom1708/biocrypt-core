package com.biocrypt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class VC {

    BufferedImage img;
    int finalStartX = -1, finalStartY = -1, finalEndX = -1, finalEndY = -1;
    int n , kVal = 2;
    ArrayList <File> files;
    ArrayList<BufferedImage> shares;
    String username;

    public VC(int startX, int startY, int endX, int endY, int n, BufferedImage img, String username)
    {
        this.finalEndX = endX;
        this.finalEndY = endY;
        this.finalStartX = startX;
        this.finalStartY = startY;
        this.img = img;
        this.n = n;
        shares = new ArrayList<>();
        this.username = username;
    }

    public BufferedImage getOutputImg()
    {
        ImageProcessing imageProcessing = new ImageProcessing(img, finalStartX, finalStartY, finalEndX, finalEndY, username);
        shares = imageProcessing.generateKoutOfNShares_direct(kVal, n);

        System.out.println(shares.size());
        int max=0;
        boolean flag=false;
        for(int i=1;i<shares.size();i++)
        {
            if(shares.get(i).getHeight()>shares.get(max).getHeight())
            {
                max=i;
            }
            else if(shares.get(i).getHeight()==shares.get(max).getHeight())
            {
                flag=true;
            }
        }


        int startX=shares.get(max).getRGB(0,0);
        int startY=shares.get(max).getRGB(shares.get(max).getWidth()-1,0);

        if(flag)
        {
            startX=0;
            startY=0;
        }
        int k=0;
        if(max==0)
        {
            k=1;
        }

        for(int i=0;i<shares.get(k).getHeight();i++)
        {
            for(int j=0;j<shares.get(k).getWidth();j++)
            {
                int temp=0;
                for(int p=0;p<shares.size();p++)
                {
                    if(p==max)
                    {
                        temp=temp|shares.get(p).getRGB(startX+j,startY+i);
                    }
                    else
                    {
                        temp=temp|shares.get(p).getRGB(j,i);
                    }
                }
                shares.get(max).setRGB(startX+j,startY+i,temp);
            }
        }
        return shares.get(max);
    }

}
