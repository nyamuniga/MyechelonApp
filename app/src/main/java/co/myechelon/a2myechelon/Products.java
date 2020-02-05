package co.myechelon.a2myechelon;

import android.media.Image;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Teacher on 6/7/2018.
 */

public class Products {

    public String price;
    public String title;
    public String uid;
    public String image;


    public Products(String price, String title, String image,String uid) {
        this.price = price;
        this.title = title;
        this.image = image;
        this.uid = uid;

    }
    public Products(){

    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {


        return image;
    }

    public void setImage(String image) {
        this.image = image;




    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

