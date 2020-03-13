package com.amdc.android.chatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_CODE = 1;
    private RelativeLayout activity_mine;
    private EmojiconEditText emojiconEditText;
    static byte[] encryptedBytes;
    static byte[] decryptedBytes;
    static SecretKeySpec key = new SecretKeySpec("Shauryxx777xx777".getBytes(), "AES");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_mine, "You are authorized", Snackbar.LENGTH_SHORT).show();
                displayAllMessage();
            } else {
                Snackbar.make(activity_mine, "You are not logged in", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_mine = findViewById(R.id.activity_main);
        ImageView emoButton = findViewById(R.id.emojiButton);
        ImageView submitButton = findViewById(R.id.submitButton);
        emojiconEditText = findViewById(R.id.textField);
        EmojIconActions emojIconActions = new EmojIconActions(getApplicationContext(), activity_mine, emojiconEditText, emoButton);
        emojIconActions.ShowEmojIcon();
        submitButton.setOnClickListener(e-> {
            try { new Encryption(emojiconEditText.getText().toString());
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException ex) {
                Snackbar.make(activity_mine, "Key not valid", Snackbar.LENGTH_SHORT).show();
            }
            FirebaseDatabase.getInstance().getReference().push().setValue(new Message(Objects.requireNonNull(FirebaseAuth.getInstance().
                    getCurrentUser()).getDisplayName(), Arrays.toString(encryptedBytes)));
                emojiconEditText.setText("");
        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        else { Snackbar.make(activity_mine, "You are authorized", Snackbar.LENGTH_SHORT).show();
        displayAllMessage();
        }
    }

    private void displayAllMessage() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        FirebaseListAdapter<Message> adapter = new FirebaseListAdapter<Message>(this, Message.class,
                R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView mess_user, mess_time, mess_text;
                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                mess_text = v.findViewById(R.id.message_text);
                mess_user.setText(model.getUserName());
                try {
                    new Decryption(model.getTextMessage());
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                    Toast.makeText(getApplicationContext(), "Key not valid", Toast.LENGTH_SHORT).show();
                }
                mess_text.setText(new String(decryptedBytes, StandardCharsets.UTF_8));
                mess_time.setText(DateFormat.format("dd/mm/yyyy  HH:mm:ss", model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }
}
