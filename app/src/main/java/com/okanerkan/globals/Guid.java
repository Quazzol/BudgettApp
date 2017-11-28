package com.okanerkan.globals;

import android.support.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by OkanErkan on 28.11.2017.
 */

public class Guid
{
    // aec063ae-4503-4337-b01c-9207fb10b9df

    @NonNull
    public static String New()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String Empty()
    {
        return "00000000-0000-0000-0000-000000000000";
    }

    public static boolean IsEmpty(String _guid)
    {
        return _guid.equalsIgnoreCase(Guid.Empty());
    }

    public static String ConvertToSHA(String _textToHash) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        byte[] salt = Guid.getSalt();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);

        byte[] bytes = md.digest(_textToHash.getBytes());
        StringBuilder sb = new StringBuilder();

        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}