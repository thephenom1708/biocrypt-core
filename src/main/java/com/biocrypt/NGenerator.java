package com.biocrypt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class NGenerator {
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public int getValueOfN(String pin) throws Exception
    {
        String s1 = pin;
        String s2 = toHexString(getSHA(s1));
        int cntComp = 0;
        int cntPrime = 0;
        for (int i = 0; i < s2.length(); i++) {
            if (Character.isDigit(s2.charAt(i))) {
                int n = Character.getNumericValue(s2.charAt(i));
                // Corner case
                if (n <= 1)
                    continue;

                // Check from 2 to n-1
                for (int j = 2; j <= n / 2; j++)
                    if (n % j == 0) {
                        cntComp++;
                        break;
                    }
                cntPrime++;
            }
        }
        int res = 2 + (Math.abs(cntPrime - cntComp) % 7);
        System.out.println("N: " + res);
        return res;
    }
}