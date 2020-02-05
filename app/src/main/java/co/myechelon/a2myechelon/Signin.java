package co.myechelon.a2myechelon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Teacher on 6/6/2018.
 */

public class Signin extends AppCompatActivity {

    private Button signup;
   private TextView forgotpass;
    private EditText email;
    private EditText password;
    private Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signup= (Button) findViewById(R.id.signup);
        login= (Button) findViewById(R.id.login);
        email= (EditText) findViewById(R.id.phone_number);
        password= (EditText) findViewById(R.id.password);
        forgotpass= (TextView) findViewById(R.id.forgot);
        firebaseAuth =FirebaseAuth.getInstance();

        FirebaseUser user= firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        if(user !=null){
            finish();
            startActivity(new Intent(Signin.this,Home.class));

        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this,Signup.class);
                startActivity(intent);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this,Forgotpass.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }


    public void validate() {
        progressDialog.setMessage("loading");
        progressDialog.show();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()){

            progressDialog.dismiss();
            Toast.makeText(Signin.this, "fill the blanks", Toast.LENGTH_SHORT).show();
        }else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        checkverification();
                    } else {
                        Toast.makeText(Signin.this, "login failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }
            });
        }
    }
private void checkverification(){
        FirebaseUser firebaseUser= firebaseAuth.getInstance().getCurrentUser();
    boolean emailflag=firebaseUser.isEmailVerified();

    if (emailflag){
        finish();
        startActivity(new Intent(Signin.this, UpdateProfile.class));

    }else{
        Toast.makeText(Signin.this, "verify email", Toast.LENGTH_SHORT).show();
        firebaseAuth.signOut();

    }


    }



}
