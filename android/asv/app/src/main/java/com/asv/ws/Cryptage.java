package com.asv.ws;

import java.util.Calendar;

/**
 * Created by Bertraior on 24/06/2015.
 */
public class Cryptage {
    public String crypte(int androidId)
    {
        Calendar lCDateTime = Calendar.getInstance();
        long timestamp = lCDateTime.getTimeInMillis() / 1000000;
        String md5 = new StringBuilder().append(androidId).append("graindesel").append(timestamp).toString();
        String secondmd5 = new StringBuilder().append("danzou").append(timestamp).toString();
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            byte[] secondarray = md.digest(secondmd5.getBytes());
            StringBuffer sb2 = new StringBuffer();
            for (int i = 0; i < secondarray.length; ++i) {
                sb2.append(Integer.toHexString((secondarray[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb2.substring(0, 5) + sb.toString() + sb2.substring(6, 9);
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
