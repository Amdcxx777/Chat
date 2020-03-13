package com.amdc.android.chatapp;

import android.annotation.SuppressLint;
import android.widget.Toast;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

@SuppressLint("Registered")
public class Decryption extends MainActivity {
    public Decryption(String msm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        @SuppressLint("GetInstance")
        Cipher decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        String[] msg = msm.substring(1, msm.length() - 1).split(", ");
        byte[] msgByte = new byte[msg.length];

        for (int i = 0; i < msg.length; i++) {
            try { msgByte[i] = Byte.parseByte(msg[i]);
            } catch (Exception ignored) {}
        }
        try { decryptedBytes = decryptCipher.doFinal(msgByte);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            Toast.makeText(getApplicationContext(), "Key not valid", Toast.LENGTH_SHORT).show();
        }
    }
}
