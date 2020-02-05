package co.myechelon.a2myechelon;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profile;
    private TextView email;
    private TextView password;
    private TextView username;
    private TextView country;
    private TextView city;
    private TextView address;
    private TextView contacts;
    private TextView name;
    private Button edit;
    private Button editpass;
    private FirebaseAuth firebaseAuth;
    private  FirebaseDatabase  firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile = (ImageView) findViewById(R.id.pp);

        name = (TextView) findViewById(R.id.pname);
        username = (TextView) findViewById(R.id.pusername);
        city = (TextView) findViewById(R.id.pcity);
        country = (TextView) findViewById(R.id.pcountry);
        contacts = (TextView) findViewById(R.id.pcontacts);
        address = (TextView) findViewById(R.id.paddress);
        edit = (Button) findViewById(R.id.pedit);
        editpass = (Button) findViewById(R.id.changepass);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, UpdateProfile.class));
            }
        });
        editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, PassReset.class));
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        storageReference = firebaseStorage.getReference();
        if (firebaseAuth.getUid() == null) {
            firebaseAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, Signin.class));
            finish();
        } else {
            storageReference.child(firebaseAuth.getUid()).child("images/profile pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                    Picasso.with(ProfileActivity.this).load(uri).fit().centerCrop().into(profile);
                }
            });




        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());



            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Profile profile1 = dataSnapshot.getValue(Profile.class);


                        name.setText(profile1.getProfile_name());


                        username.setText(profile1.getProfile_username());

                        country.setText(profile1.getProfile_country());


                        city.setText(profile1.getProfile_city());

                        contacts.setText(profile1.getProfile_contacts());

                        address.setText(profile1.getProfile_address());


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    }
