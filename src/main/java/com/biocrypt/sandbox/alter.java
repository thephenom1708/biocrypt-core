package com.biocrypt.sandbox;

import java.awt.image.*;
import java.awt.Color;
import java.util.*;

/*========================================================================
Copyright (c) 2007 Robert Hansen, www.r-hansen.com
This file is part of One-Time Image.
One-Time Image is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.
One-Time Image is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with One-Time Image; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
========================================================================*/

public class alter {

    static int getBrightness(int rgb) {
        int r,g,b;
        Color pixColour = new Color(rgb);
        r=pixColour.getRed();
        g=pixColour.getGreen();
        b=pixColour.getBlue();
        rgb = (int)((r+g+b)/3);
        return rgb;
    }

    static BufferedImage monochrome(BufferedImage picture) {
        int i,j,min=128,max=128,value=0,col;

        for (i=0;i<picture.getHeight();i++){
            for (j=0;j<picture.getWidth();j++) {
                value=getBrightness(picture.getRGB(j,i));
                if (value<min) min=value;
                if (value>max) max=value;
            }
        }

        value=(int)Math.ceil((max+min)/2);

        for (i=0;i<picture.getHeight();i++){
            for (j=0;j<picture.getWidth();j++) {
                col=getBrightness(picture.getRGB(j,i));
                if (col<value) picture.setRGB(j,i,-16777216);//black
                else picture.setRGB(j,i,-1);//white
            }
        }

        return picture;
    }

    static BufferedImage meldImages(BufferedImage template, BufferedImage random) {
        int i,j;
        BufferedImage output = new BufferedImage(template.getWidth(),template.getHeight(),BufferedImage.TYPE_INT_RGB);

        for (i=0;i<template.getHeight();i++){
            for (j = 0; j < template.getWidth(); j++) {
                if (template.getRGB(j,i)<-8388608) {//black
                    if (random.getRGB(j,i)<-8388608) output.setRGB(j,i,-1);
                    else output.setRGB(j,i,-16777216);
                }
                else {
                    if (random.getRGB(j,i)<-8388608) output.setRGB(j,i,-16777216);
                    else output.setRGB(j,i,-1);
                }
            }
        }
        return output;
    }

    static BufferedImage makeGrey(BufferedImage template) {//note; 0 is black in the top left corner, 1 has white in that corner
        BufferedImage output = new BufferedImage(template.getWidth()*2,template.getHeight()*2,BufferedImage.TYPE_INT_RGB);
        int i,j;

        for (i=0;i<template.getHeight();i++){
            for (j=0;j<template.getWidth();j++) {
                if (template.getRGB(j,i)<-8388608) {//black
                    output.setRGB((j*2),(i*2),-16777216);//black
                    output.setRGB((j*2),(i*2)+1,-1);//white
                    output.setRGB((j*2)+1,(i*2),-1);//white
                    output.setRGB((j*2)+1,(i*2)+1,-16777216);//black
                }
                else {
                    output.setRGB((j*2),(i*2),-1);//white
                    output.setRGB((j*2),(i*2)+1,-16777216);//black
                    output.setRGB((j*2)+1,(i*2),-16777216);//black
                    output.setRGB((j*2)+1,(i*2)+1,-1);//white
                }

            }
        }
        return output;
    }

    static BufferedImage createRandom(BufferedImage template) {
        int i,j;
        //IF INTENDED FOR ANY 'SERIOUS' USE, REPLACE THE LINE BELOW WITH A BETTER SOURCE OF RANDOM NUMBERS
        Random rn = new Random();

        BufferedImage output = new BufferedImage(template.getWidth(),template.getHeight(),BufferedImage.TYPE_INT_RGB);
        for (i=0;i<template.getHeight();i++){
            for (j=0;j<template.getWidth();j++) {
                if (rn.nextBoolean()==false) output.setRGB((j),(i),-16777216);//black
                else output.setRGB((j),(i),-1);//white
            }
        }

        return output;
    }


}
