package com.amdc.android.chatapp;

import android.annotation.SuppressLint;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@SuppressLint("Registered")
public class Encryption extends MainActivity {
    @SuppressLint("GetInstance")
    public Encryption(String msm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher encryptCipher = Cipher.getInstance("AES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        encryptedBytes = encryptCipher.doFinal(msm.getBytes());
    }
}
