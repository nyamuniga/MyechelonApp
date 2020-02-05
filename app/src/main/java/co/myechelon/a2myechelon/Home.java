package co.myechelon.a2myechelon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static co.myechelon.a2myechelon.R.id.image;
import static co.myechelon.a2myechelon.R.id.pp;
import static co.myechelon.a2myechelon.R.id.profile;
import static co.myechelon.a2myechelon.R.id.title;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private  FirebaseDatabase  firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private LinearLayout linearLayout;
    private DatabaseReference mDatabase;
    private FloatingActionButton button;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Products, Home.HomeViewHolder> mPeopleRVAdapter;
    private LocationListener locationListener;
    private LocationManager locationManager;


    private int getSpanCount(){
        DisplayMetrics displayMetrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width=displayMetrics.widthPixels;
        float minimum = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,displayMetrics);
        return (int)(width/minimum);
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

toolbar = (Toolbar) findViewById(R.id.toolbar) ;
        setTitle("myechelon");
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
        button = (FloatingActionButton) findViewById(R.id.sell);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(getApplicationContext(),popup.class);
                startActivity(i);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {

                Geocoder geocoder=new Geocoder(Home.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    String address=addresses.get(0).getAddressLine(0);
                    String area=addresses.get(0).getLocality();



                    DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("products").child(area);


                    Query personsQuery = personsRef.orderByKey();

                    recyclerView = (RecyclerView) findViewById(R.id.recycler);
                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(new GridLayoutManager(Home.this,getSpanCount()));

                    personsRef.keepSynced(true);

                    FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Products>().setQuery(personsQuery, Products.class).build();

                    mPeopleRVAdapter = new FirebaseRecyclerAdapter<Products, Home.HomeViewHolder>(personsOptions) {
                        @Override
                        protected void onBindViewHolder(final Home.HomeViewHolder holder, final int position, final Products model) {

                            holder.setPrice(model.getPrice());
                            holder.setTitle(model.getTitle());

                           final ImageView post_image = (ImageView) holder.mView.findViewById(R.id.image);
                            storageReference.child(model.getUid()).child("images/"+model.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    Picasso.with(Home.this).load(uri).fit().centerCrop().into(post_image);
                                }
                            });
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent =new Intent(Home.this,Details.class);
                                    intent.putExtra("image_url",model.getImage());
                                    intent.putExtra("image_title",model.getTitle());
                                    intent.putExtra("image_price",model.getPrice());
                                    intent.putExtra("image_uid",model.getUid());
                                    startActivity(intent);
                                }
                            });


                        }

                        @Override
                        public Home.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.activity_card, parent, false);

                            return new Home.HomeViewHolder(view);
                        }
                    };







                    recyclerView.setAdapter(mPeopleRVAdapter);
                    mPeopleRVAdapter.startListening();



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                Toast.makeText(Home.this, "Make sure POSITION is enabled in settings", Toast.LENGTH_LONG).show();
            }
        };

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{

                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET

                }, 10);


            } else {

                locationManager.requestLocationUpdates("gps", 1800000,0f, locationListener);
            }





    }






    public static class HomeViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public HomeViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setTitle(  String title3) {
            TextView post_title = (TextView) mView.findViewById(R.id.title);
            post_title.setText(title3);
        }
        public void setPrice( String price3) {
            TextView post_price = (TextView) mView.findViewById(R.id.price);
            post_price.setText(price3);
        }


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates("gps", 5000,0f, locationListener);

                }
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.userprofile:
                startActivity(new Intent(Home.this,ProfileActivity.class));
            break;
            case R.id.location:
                startActivity(new Intent(Home.this,Location.class));
                break;
            case R.id.send:
                startActivity(new Intent(Home.this,SendMsgActivity.class));
                break;
            case R.id.sell:
                startActivity(new Intent(Home.this,Sell.class));
                break;

            case R.id.logout:
             firebaseAuth.getCurrentUser();
            firebaseAuth.signOut();

                startActivity(new Intent(Home.this, Signin.class));
                finish();
                break;
            case R.id.message:
                startActivity(new Intent(Home.this, Messages.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}