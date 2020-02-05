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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Teacher on 6/7/2018.
 */

public class Forgotpass extends AppCompatActivity {


    private EditText email;

    private ProgressDialog progressDialog;
    private Button resetpass;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        //  skip= (Button) findViewById(R.id.btn_skip);
        resetpass = (Button) findViewById(R.id.reset);
        email = (EditText) findViewById(R.id.emailtosend);


        progressDialog = new ProgressDialog(this);
        firebaseAuth =FirebaseAuth.getInstance();
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uemail = email.getText().toString().trim();

                progressDialog.setMessage("loading");
                progressDialog.show();

                if (uemail.isEmpty()) {

                    Toast.makeText(Forgotpass.this, "please fill the blanks", Toast.LENGTH_SHORT).show();

                } else {

                    firebaseAuth.sendPasswordResetEmail(uemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(Forgotpass.this, "successfully sent", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Forgotpass.this, Signin.class));
                            } else {
                                Toast.makeText(Forgotpass.this, "failed", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        }
                    });
                }

            }


        });
    }

}