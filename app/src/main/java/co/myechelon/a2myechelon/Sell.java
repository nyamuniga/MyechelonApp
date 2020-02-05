package co.myechelon.a2myechelon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Teacher on 6/6/2018.
 */

public class Sell extends AppCompatActivity {


    private EditText price;
    private EditText title;
    private ProgressDialog progressDialog;
private Button sell;
    private ImageView user_pro;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String ptitle,pprice;
    private static int PICK_IMAGE = 123;
    private LocationListener locationListener;
    private LocationManager locationManager;
    Uri imagepath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==PICK_IMAGE || requestCode==RESULT_OK || data.getData()!=null){
            imagepath= data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                user_pro.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(Sell.this, "fail", Toast.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
setTitle("Sell an item");
        //  skip= (Button) findViewById(R.id.btn_skip);
        sell = (Button) findViewById(R.id.post);
        user_pro = (ImageView) findViewById(R.id.proimage);
        title = (EditText) findViewById(R.id.protitle);
        price = (EditText) findViewById(R.id.proprice);
        firebaseAuth =FirebaseAuth.getInstance();
        firebaseStorage =FirebaseStorage.getInstance();

         storageReference = firebaseStorage.getReference();
      user_pro.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent();
              intent.setType("image/*");
              intent.setAction(Intent.ACTION_GET_CONTENT);
              startActivityForResult(Intent.createChooser(intent,"select an image"),PICK_IMAGE);

          }
      });


        progressDialog = new ProgressDialog(this);
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){

                    userdata();


                }
            }
        });
    }

    public boolean validate() {
        boolean result=false;
        pprice=price.getText().toString();
         ptitle= title.getText().toString();

        progressDialog.setMessage("loading");
        progressDialog.show();

        if (imagepath==null || pprice.isEmpty() ||ptitle.isEmpty()){

            Toast.makeText(this,"please fill the blanks", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

        }else{

            result=true;
        }
        return result;
    }
       private void userdata() {
           FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
           final DatabaseReference databaseReference = firebaseDatabase.getReference();
           final DatabaseReference mydatabaseReference = firebaseDatabase.getReference();
           StorageReference imagereference = storageReference.child(firebaseAuth.getUid()).child("images").child(ptitle);
           UploadTask uploadTask = imagereference.putFile(imagepath);
           uploadTask.addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(Sell.this, "failed to upload", Toast.LENGTH_SHORT).show();

               }
           }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Toast.makeText(Sell.this, "uploaded successfully", Toast.LENGTH_SHORT).show();

               }
           });


           locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
           locationListener = new LocationListener() {
               @Override
               public void onLocationChanged(android.location.Location location) {

                   Geocoder geocoder = new Geocoder(Sell.this, Locale.getDefault());
                   try {
                       List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                       String address = addresses.get(0).getAddressLine(0);
                       String area = addresses.get(0).getLocality();

                       Products product= new Products(pprice,ptitle,ptitle,firebaseAuth.getUid());
                       databaseReference.child("products").child(area).child(firebaseAuth.getCurrentUser().getUid()+ptitle).setValue(product);
                       mydatabaseReference.child("users").child(firebaseAuth.getUid().toString()).child("products").child(firebaseAuth.getCurrentUser().getUid()+ptitle).setValue(product);



                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }

               @Override
               public void onStatusChanged(String provider, int status, Bundle extras) {

               }

               @Override
               public void onProviderEnabled(String provider) {

               }

               @Override
               public void onProviderDisabled(String provider) {
                   Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                   startActivity(intent);
               }
           };
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   requestPermissions(new String[]{

                           android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET

                   }, 10);


               } else {

                   locationManager.requestLocationUpdates("gps", 5000, 0f, locationListener);
               }
           }
           progressDialog.dismiss();
           finish();
       }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates("gps", 5000,0f, locationListener);

                }
        }
    }






    }


