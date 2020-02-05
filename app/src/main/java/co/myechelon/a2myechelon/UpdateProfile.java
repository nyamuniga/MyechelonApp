package co.myechelon.a2myechelon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UpdateProfile extends AppCompatActivity {

    private ImageView profile;
    private TextView email;
    private TextView password;
    private TextView username;
    private TextView country;
    private TextView city;
    private TextView address;
    private TextView contacts;
    private TextView name;
    private Button update;
    private ProgressDialog progressDialog;
    private String uemail, upass, uusername, ucountry, ucity, uaddress, ucontacts, uname;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
   private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        profile = (ImageView) findViewById(R.id.profile);


        name = (TextView) findViewById(R.id.name);
        username = (TextView) findViewById(R.id.username);
        city = (TextView) findViewById(R.id.city);
        country = (TextView) findViewById(R.id.country);
        contacts = (TextView) findViewById(R.id.contacts);
        address = (TextView) findViewById(R.id.address);
        update = (Button) findViewById(R.id.update);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child(firebaseAuth.getUid()).child("images").child("profile pic");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(UpdateProfile.this).load(uri).into(profile);

            }
        });

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());

        if (firebaseAuth.getUid() == null) {
            firebaseAuth.signOut();
            startActivity(new Intent(UpdateProfile.this, Signin.class));
            finish();
        } else {

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
                    Toast.makeText(UpdateProfile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });
        }
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.setMessage("loading");
                    progressDialog.show();

                    uname = name.getText().toString().trim();
                    uusername = username.getText().toString().trim();
                    ucity = city.getText().toString().trim();
                    ucountry = country.getText().toString().trim();
                    ucontacts = contacts.getText().toString().trim();
                    uaddress = address.getText().toString().trim();
                    final DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());



                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Profile profile1 = dataSnapshot.getValue(Profile.class);


                            Profile profile = new Profile(profile1.getProfile_email(),profile1.getProfile_password(),uusername,ucountry,ucity,uaddress,ucontacts,uname);

                            databaseReference.setValue(profile);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(UpdateProfile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });


                    progressDialog.dismiss();
                        Toast.makeText(UpdateProfile.this, "updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateProfile.this, Home.class));






                }
            });
        }
    }
