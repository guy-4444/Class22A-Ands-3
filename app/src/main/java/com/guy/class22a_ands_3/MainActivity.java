package com.guy.class22a_ands_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView1;
    private ImageView imageView2;
    private MaterialButton encrypt;
    private MaterialButton decrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        encrypt = findViewById(R.id.encrypt);
        decrypt = findViewById(R.id.decrypt);
        encrypt.setOnClickListener(view -> firstMethod());
        decrypt.setOnClickListener(view -> secondMethod());
    }

    private void firstMethod() {
        Log.d("pttt", "- - - - - - - -STARTED- - - - - - - -");
        long start = System.currentTimeMillis();
        //imageView1.buildDrawingCache();
        //Bitmap bitmap = imageView1.getDrawingCache();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.img_bear);
        imageView1.setImageBitmap(bitmap);

        String base64String = ImageUtil.convert(bitmap);


        String enc = encrypt(base64String);

        Log.d("pttt", " firstMethod duration= " + (System.currentTimeMillis() - start));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("image");
        myRef.setValue(enc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("pttt", " firstMethod with upload duration= " + (System.currentTimeMillis() - start));
            }
        });
    }

    long secondMethodStart = 0;
    private void secondMethod() {
        secondMethodStart = System.currentTimeMillis();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("image");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data = snapshot.getValue(String.class);
                continueSecondMethod(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void continueSecondMethod(String data) {
        String dec = decrypt(data);


        Bitmap bitmapAfter64 = ImageUtil.convert(dec);
        imageView2.setImageBitmap(bitmapAfter64);

        Log.d("pttt", "- - - - - - - -DONE- - - - - - - -");

        Log.d("pttt", " firstMethod duration= " + (System.currentTimeMillis() - secondMethodStart));
    }

    private String encrypt(String str) {
        try {
            return CryptoUtil.encrypt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NA";
    }

    private String decrypt(String str) {
        try {
            return CryptoUtil.decrypt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NA";
    }



}