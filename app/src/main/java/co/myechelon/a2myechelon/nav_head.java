package co.myechelon.a2myechelon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Teacher on 6/12/2018.
 */

public class nav_head extends AppCompatActivity {
    private ImageView profile1;
    private TextView email;
    private TextView name;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);


        profile1 = (ImageView) findViewById(R.id.pp);
        email = (TextView) findViewById(R.id.email123);
        name = (TextView) findViewById(R.id.name123);

        firebaseAuth = FirebaseAuth.getInstance();
       String email1= firebaseAuth.getCurrentUser().getEmail();
        email.setText(email1);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
   DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(firebaseAuth.getUid());



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile1 = dataSnapshot.getValue(Profile.class);


                name.setText(profile1.getProfile_name());










            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(nav_head.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}