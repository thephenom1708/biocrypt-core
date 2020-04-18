package com.biocrypt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<Share> getAllShares(String serverIp, String username) {
        String url = "http://" + serverIp + ":" + Url.PORT + Url.RETURN_SHARES_URL;
        String params = "username=" + username;
        JsonArray jsonShares;
        List<Share> shares = new ArrayList<>();

        HttpSendData send = new HttpSendData(url, params);
        try {
            JsonObject response = send.sendPOSTJson();
            jsonShares = response.getAsJsonArray("shares");

            for (JsonElement element : jsonShares) {
                String number = element.getAsJsonObject().get("share_number").getAsString();
                String data = element.getAsJsonObject().get("share_data").getAsString();

                Share share = new Share(Integer.parseInt(number), data);
                shares.add(share);
            }
        } catch (Exception e) {
            System.out.println("No Share found on " + serverIp);
            return new ArrayList<>();
        }
        return shares;
    }

    public static BufferedImage getCroppedImageFromRemoteCoordinates(BufferedImage img, String username, int n) {
        String url = Url.GET_COORDINATES_URL;
        String param = "username=" + username;
        String response = "";
        HttpSendData send1 = new HttpSendData(url, param);
        try {
            response = send1.sendPOST();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] coordinates = response.split(",");
        int sx, sy, ex, ey;
        sx = Integer.parseInt(coordinates[0]);
        sy = Integer.parseInt(coordinates[1]);
        ex = Integer.parseInt(coordinates[2]);
        ey = Integer.parseInt(coordinates[3]);

        VC newVc = new VC(sx, sy, ex, ey, n, img, username);
        return newVc.getOutputImg();
    }

    public static BufferedImage getMergedImageFromShares(List<BufferedImage> shares) {
        int max = 0;
        boolean flag = false;
        for (int i = 1; i < shares.size(); i++) {
            if (shares.get(i).getHeight() > shares.get(max).getHeight()) {
                max = i;
            } else if (shares.get(i).getHeight() == shares.get(max).getHeight()) {
                flag = true;
            }
        }


        int startX = shares.get(max).getRGB(0, 0);
        int startY = shares.get(max).getRGB(shares.get(max).getWidth() - 1, 0);

        if (flag) {
            startX = 0;
            startY = 0;
        }
        int k = 0;
        if (max == 0) {
            k = 1;
        }

        for (int i = 0; i < shares.get(k).getHeight(); i++) {
            for (int j = 0; j < shares.get(k).getWidth(); j++) {
                int temp = 0;
                for (int p = 0; p < shares.size(); p++) {
                    if (p == max) {
                        temp = temp | shares.get(p).getRGB(startX + j, startY + i);
                    } else {
                        temp = temp | shares.get(p).getRGB(j, i);
                    }
                }
                shares.get(max).setRGB(startX + j, startY + i, temp);
            }
        }
        return shares.get(max);
    }
}
