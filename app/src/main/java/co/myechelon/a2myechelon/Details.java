package co.myechelon.a2myechelon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;





public class Details extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private  FirebaseDatabase  firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(getIntent().hasExtra("image_url")&&getIntent().hasExtra("image_title")&&getIntent().hasExtra("image_price") &&getIntent().hasExtra("image_uid")) {

            String image1 = getIntent().getStringExtra("image_url");
            final String title1 = getIntent().getStringExtra("image_title");
            String price1 = getIntent().getStringExtra("image_price");
            final String uid1 = getIntent().getStringExtra("image_uid");
            TextView name1 = (TextView) findViewById(R.id.details);
            name1.setText(title1);
            FloatingActionButton chat =(FloatingActionButton)findViewById(R.id.chat);
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Details.this,SendMsgActivity.class);

                    intent.putExtra("image_title",title1);
                    intent.putExtra("image_uid",uid1);
                    startActivity(intent);

                }
            });
            TextView price4 = (TextView) findViewById(R.id.price);
            price4.setText(price1);

            final ImageView image4 = (ImageView) findViewById(R.id.product_image);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference();
            storageReference.child(uid1).child("images/"+image1).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                    Picasso.with(Details.this).load(uri).fit().centerCrop().into(image4);
                }
            });

        }
    }
}





