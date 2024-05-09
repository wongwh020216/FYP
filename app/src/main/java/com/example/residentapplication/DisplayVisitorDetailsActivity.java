package com.example.residentapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class DisplayVisitorDetailsActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference ref;
    TextView dName, dPhoneNumber, dPurpose, dVehicle, dVisitDate, dEndDate,dType;
    Button shareButton;

    public static String[] storage_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        } else {
            p = storage_permissions;
        }
        return p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_visitor_details);

        ActivityCompat.requestPermissions(DisplayVisitorDetailsActivity.this,
                permissions(),
                1);


        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(v -> {
            shareButton.setVisibility(View.GONE);
            File file = SaveImage();
            if (file!=null)
                share(file);
            shareButton.setVisibility(View.VISIBLE);
        });

        dName = findViewById(R.id.dName);
        dPhoneNumber = findViewById(R.id.dPhoneNumber);
        dPurpose = findViewById(R.id.dPurpose);
        dVehicle = findViewById(R.id.dVehicle);
        dVisitDate = findViewById(R.id.dVisitDate);
        dEndDate = findViewById(R.id.dEndDate);
        dType = findViewById(R.id.dType);

        String diName = getIntent().getStringExtra("keyName");
        String diPhoneNumber = getIntent().getStringExtra("keyPhoneNumber");
        String diPurpose = getIntent().getStringExtra("keyPurpose");
        String diVehicle = getIntent().getStringExtra("keyVehicle");
        String diVisitDate = getIntent().getStringExtra("keyVisitDate");
        String diEndDate = getIntent().getStringExtra("keyEndDate");
        String diType = getIntent().getStringExtra("keyType");

        dName.setText(diName);
        dPhoneNumber.setText(diPhoneNumber);
        dPurpose.setText(diPurpose);
        dVehicle.setText(diVehicle);
        dVisitDate.setText(diVisitDate);
        dEndDate.setText(diEndDate);
        dType.setText(diType);

    }

    private void share(File file) {
        Uri uri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(this,getPackageName()+".provider",file);

        }else{
            uri = Uri.fromFile(file);
        }

        Intent intent =new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Screenshot");
        intent.putExtra(Intent.EXTRA_STREAM,uri);

        try{
            startActivity(Intent.createChooser(intent,"Share Using"));
        }catch(ActivityNotFoundException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File SaveImage() {


        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
            File fileDir = new File(path);
            if (!fileDir.exists())
                fileDir.mkdir();

            String mPath = path + "/Screenshot_ " + new Date().getTime() + ".png";
            Bitmap bitMap = screenshot();
            File file = new File(mPath);
            FileOutputStream fOut = new FileOutputStream(file);
            bitMap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            return file;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap screenshot() {
        View v = findViewById(R.id.rootView);
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        v.draw(canvas); // Draw the view onto the canvas
        return bitmap;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            shareButton.performClick();
        }else{
            Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
