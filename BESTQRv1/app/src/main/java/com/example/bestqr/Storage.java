package com.example.bestqr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class Storage {
    private FirebaseStorage storage;
    private StorageReference storage_ref;

    public Storage() {
        storage = FirebaseStorage.getInstance();
        storage_ref = storage.getReference();
    }

    public void upload(QRCODE qrcode, String androidid) {
        byte[] data = get_bytes(qrcode, androidid);
        upload_bytes(data, androidid, qrcode.getHash());
    }

    public void upload_bytes(byte[] bytes, String androidid, String hash) {
        StorageReference folder_ref = storage_ref.child(androidid);
        StorageReference file_ref = folder_ref.child(hash + ".jpg");
        UploadTask task = file_ref.putBytes(bytes);
        while (!task.isComplete()) {}
    }

    public byte[] get_bytes(QRCODE qrcode, String androidid) {
        Bitmap bitmap = qrcode.getCode();
        String hash = qrcode.getHash();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        System.out.println(data.length);

        return data;
    }

    public Bitmap download(String androidid, String hash) {
        String filename = hash + ".jpg";
        StorageReference file_ref = storage_ref.child(androidid).child(filename);

        Task<byte[]> task = file_ref.getBytes(Long.MAX_VALUE);

        while (!task.isSuccessful()) {}

        byte[] data = task.getResult();

        System.out.println(data.length);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }

}
