package co.myechelon.a2myechelon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SendMsgActivity extends AppCompatActivity {
private Toolbar toolbar;
    private RecyclerView recyclerView;


       private FloatingActionButton sendtxt;
    private EditText textm;
    private String from = FirebaseAuth.getInstance().getUid().toString();
    private String key;
    private FirebaseRecyclerAdapter<Texts, SendMsgActivity.SendMsgActivityViewHolder> mPeopleRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);
        toolbar = (Toolbar) findViewById(R.id.toolbar) ;
        sendtxt = (FloatingActionButton) findViewById(R.id.send) ;
        textm = (EditText) findViewById(R.id.message) ;
        if(getIntent().hasExtra("image_title")&&getIntent().hasExtra("image_uid")) {


            final String title1 = getIntent().getStringExtra("image_title");
            final String uid1 = getIntent().getStringExtra("image_uid");

            setTitle(title1);

            sendtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String textmsg = textm.getText().toString();

                    Texts texts = new Texts(textmsg, title1,from,uid1, 551);
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference();



                        databaseReference.child("messages").child(title1).push().setValue(texts);

                    textm.setText(null);



                }
            });


                    recyclerView = (RecyclerView) findViewById(R.id.recycler);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SendMsgActivity.this));





                    DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("messages").child(title1);

                    Query personsQuery = personsRef.orderByValue();


                    personsRef.keepSynced(true);

                        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Texts>().setQuery(personsQuery, Texts.class).build();

                        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Texts, SendMsgActivity.SendMsgActivityViewHolder>(personsOptions) {
                            @Override
                            protected void onBindViewHolder(SendMsgActivity.SendMsgActivityViewHolder holder, final int position, final Texts model) {
                                      if(model.getFrom().equals(from)) {
                                          holder.setMineMessage(model.getMessage());
                                      }else{
                                          holder.setOtherMessage(model.getMessage());
                                      }

                            }

                            @Override
                            public SendMsgActivity.SendMsgActivityViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

                                View view = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.user_text, parent, false);




                                return  new SendMsgActivity.SendMsgActivityViewHolder(view);
                            }


                        };

                    }


                        recyclerView.setAdapter(mPeopleRVAdapter);
                        mPeopleRVAdapter.startListening();




            setSupportActionBar(toolbar);







    }


    public static class SendMsgActivityViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public SendMsgActivityViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setMineMessage(  String message) {
            TextView post_text = (TextView) mView.findViewById(R.id.txt);
            TextView post_text2 = (TextView) mView.findViewById(R.id.txt2);
            post_text2.setVisibility(View.INVISIBLE);
            post_text.setText(message);
        }
        public void setOtherMessage(  String message) {
            TextView post_text = (TextView) mView.findViewById(R.id.txt2);
            TextView post_text2 = (TextView) mView.findViewById(R.id.txt);
            post_text2.setVisibility(View.INVISIBLE);
            post_text.setText(message);
        }


    }



}
