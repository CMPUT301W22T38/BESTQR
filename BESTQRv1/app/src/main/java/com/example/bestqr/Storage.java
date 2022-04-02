package com.example.bestqr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.bestqr.models.QRCODE;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Storage  {

    public static class ReferenceHolder {
        public static FirebaseStorage STORAGE = FirebaseStorage.getInstance();
        public static StorageReference STORAGE_REFERENCE = STORAGE.getReference();
    }

    public static void upload(QRCODE qrcode, String androidid) {
        byte[] data = get_bytes(qrcode, androidid);
        upload_bytes(data, androidid, qrcode.getHash());
    }

    public static void upload_bytes(byte[] bytes, String androidid, String hash) {
        StorageReference folder_ref = ReferenceHolder.STORAGE_REFERENCE.child(androidid);
        StorageReference file_ref = folder_ref.child(hash + ".jpg");
        UploadTask task = file_ref.putBytes(bytes);

        while (!task.isComplete() && !task.isSuccessful()) {}
    }

    public static byte[] get_bytes(QRCODE qrcode, String androidid) {
        Bitmap bitmap = qrcode.getBitmap();
        String hash = qrcode.getHash();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        return data;
    }

    public static Bitmap download(String androidid, String hash) {
        String filename = hash + ".jpg";
        StorageReference file_ref = ReferenceHolder.STORAGE_REFERENCE.child(androidid).child(filename);

        Task<byte[]> task = file_ref.getBytes(Long.MAX_VALUE);
        while (!task.isComplete() && !task.isSuccessful()) {}

        byte[] data = task.getResult();

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        return bitmap;
    }

}
