package co.myechelon.a2myechelon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

/**
 * Created by Teacher on 6/6/2018.
 */

public class Signup extends AppCompatActivity {


    private EditText user_email;
    private EditText user_password;

    private ProgressDialog progressDialog;
    private Button user_signup;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String uemail,upass,uusername,ucountry,ucity,uaddress,ucontacts,uname;
    private static int PICK_IMAGE = 123;
    Uri imagepath;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
setTitle("Registration");

        //  skip= (Button) findViewById(R.id.btn_skip);
        user_signup = (Button) findViewById(R.id.signupbutton);

        user_email = (EditText) findViewById(R.id.signupemail);
        user_password = (EditText) findViewById(R.id.signuppass);


        firebaseAuth =FirebaseAuth.getInstance();
        firebaseStorage =FirebaseStorage.getInstance();

         storageReference = firebaseStorage.getReference();

        progressDialog = new ProgressDialog(this);
        user_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){

                    String usemail=user_email.getText().toString().trim();
                    String uspass=user_password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(usemail,uspass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                sendverification();
                                userdata();
                                Toast.makeText(Signup.this, "successfully registered", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                finish();
                                startActivity(new Intent(Signup.this, Signin.class));
                        }else{
                                Toast.makeText(Signup.this, "registration failed", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }



                            }


                    });
                }
            }
        });
    }

    public boolean validate() {
        boolean result=false;
        upass= user_password.getText().toString();
         uemail= user_email.getText().toString();

        progressDialog.setMessage("loading");
        progressDialog.show();

        if ( uemail.isEmpty() || upass.isEmpty() ){

            Toast.makeText(this,"please fill the blanks", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

        }else{

            result=true;
        }
        return result;
    }
    private void sendverification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Signup.this, "successfully sent to your email!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Signup.this, "failed to send the verification!", Toast.LENGTH_SHORT).show();

                    }


                    }

            });
        }



    }
    private void userdata(){
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        Profile profile= new Profile(uemail,upass,"","","","","","");
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(profile);


    }
}

