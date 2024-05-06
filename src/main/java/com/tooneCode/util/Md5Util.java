package com.tooneCode.util;

import com.intellij.openapi.diagnostic.Logger;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    private static final Logger log = Logger.getInstance(Md5Util.class);

    public Md5Util() {
    }

    public static String encode(byte[] bytes) {
        String md5 = "";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            md5 = (new BigInteger(1, messageDigest.digest())).toString(16);
        } catch (NoSuchAlgorithmException var3) {
        }

        return md5;
    }

    public static String encode(InputStream inputStream) {
        String md5 = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            DigestInputStream digestInputStream = new DigestInputStream(inputStream, digest);
            digest = digestInputStream.getMessageDigest();
            digestInputStream.close();
            md5 = (new BigInteger(1, digest.digest())).toString(16);
        } catch (Exception var4) {
        }

        return md5;
    }
}
