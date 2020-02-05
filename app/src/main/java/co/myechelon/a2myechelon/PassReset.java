package co.myechelon.a2myechelon;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PassReset extends AppCompatActivity {


    private EditText newpass;

    private Button update;
    private ProgressDialog progressDialog;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset);


        newpass = (EditText) findViewById(R.id.editpass);

        update = (Button) findViewById(R.id.updatepass);
        progressDialog = new ProgressDialog(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progressDialog.setMessage("loading");
                progressDialog.show();
                String newPassword = newpass.getText().toString();
if (newPassword.isEmpty()){
    progressDialog.dismiss();
    Toast.makeText(PassReset.this,"please fill the blank", Toast.LENGTH_SHORT).show();
}else{
                firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(PassReset.this, "successfully changed password!", Toast.LENGTH_SHORT).show();
                            finish();


                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(PassReset.this, "failed to change password!", Toast.LENGTH_SHORT).show();

                        }


                    }

                });
}
            }
        });





    }
}