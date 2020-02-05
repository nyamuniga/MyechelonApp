package co.myechelon.a2myechelon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProductsActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;



    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Products, ProductsViewHolder> mPeopleRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        setTitle("All Products");
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        firebaseAuth = FirebaseAuth.getInstance();



        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("products");
        Query personsQuery = personsRef.orderByKey();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Products>().setQuery(personsQuery, Products.class).build();

    mPeopleRVAdapter = new FirebaseRecyclerAdapter<Products, ProductsActivity.ProductsViewHolder>(personsOptions) {
        @Override
        protected void onBindViewHolder(ProductsViewHolder holder, final int position, final Products model) {

            holder.setPrice(model.getPrice());
            holder.setTitle(model.getTitle());
            holder.setImage(getApplicationContext(),model.getImage());


        }

        @Override
        public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_card, parent, false);

            return new ProductsViewHolder(view);
        }
    };

        recyclerView.setAdapter(mPeopleRVAdapter);
}

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();


    }

public static class ProductsViewHolder extends RecyclerView.ViewHolder{
    View mView;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    public ProductsViewHolder(View itemView){
        super(itemView);
        mView = itemView;
    }
    public void setTitle(String title){
        TextView post_title = (TextView)mView.findViewById(R.id.title);
        post_title.setText(title);
    }
    public void setPrice(String price){
        TextView post_desc = (TextView)mView.findViewById(R.id.price);
        post_desc.setText(price);
    }
    public void setImage(final Context ctx, final String image){
        final ImageView post_image = (ImageView) mView.findViewById(R.id.image);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("images/"+image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Picasso.with(ctx).load(uri).fit().centerCrop().into(post_image);
            }
        });

    }

}
}