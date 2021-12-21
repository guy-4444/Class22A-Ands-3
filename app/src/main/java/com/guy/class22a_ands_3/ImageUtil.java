package com.guy.class22a_ands_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil
{
    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }


    public static String convert(Context context, String file) {
        byte[] arr = read(context, file);
        return Base64.encodeToString(arr, Base64.DEFAULT);
    }

    public static byte[] read(Context context, String file) {
        byte[] ret = null;

        if (context != null) {
            try {
                InputStream inputStream = context.openFileInput(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                int nextByte = inputStream.read();
                while (nextByte != -1) {
                    outputStream.write(nextByte);
                    nextByte = inputStream.read();
                }

                ret = outputStream.toByteArray();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }
}