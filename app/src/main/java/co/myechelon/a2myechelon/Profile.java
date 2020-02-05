package co.myechelon.a2myechelon;

import android.widget.EditText;

/**
 * Created by Teacher on 6/7/2018.
 */

public class Profile {
   public String profile_email;
    public String profile_password;
    public String profile_username;
    public String profile_country;
    public String profile_city;
    public String profile_address;
    public String profile_contacts;
    public String profile_name;




public Profile(){


}

    public Profile(String profile_email,String profile_password,String profile_username,String profile_country,String profile_city,String profile_address,String profile_contacts,String profile_name) {
        this.profile_email=profile_email;
        this.profile_password=profile_password;
        this.profile_username=profile_username;
        this.profile_country=profile_country;
        this.profile_city=profile_city;
        this.profile_address=profile_address;
        this.profile_contacts=profile_contacts;
        this.profile_name=profile_name;

    }

    public String getProfile_email() {
        return profile_email;
    }
    public void setProfile_email(String profile_email) {
        this.profile_email = profile_email;
    }


    public String getProfile_password() {
        return profile_password;
    }
    public void setProfile_password(String profile_password) {
        this.profile_password = profile_password;
    }


    public String getProfile_username() {
        return profile_username;
    }
    public void setProfile_username(String profile_username) {
        this.profile_username = profile_username;
    }


    public String getProfile_country() {
        return profile_country;
    }
    public void setProfile_country(String profile_country) {
        this.profile_country = profile_country;
    }



    public String getProfile_city() {
        return profile_city;
    }
    public void setProfile_city(String profile_city) {
        this.profile_city = profile_city;
    }

    public String getProfile_address() {
        return profile_address;
    }
    public void setProfile_address(String profile_address) {
        this.profile_address = profile_address;
    }


    public String getProfile_contacts() {
        return profile_contacts;
    }
    public void setProfile_contacts(String profile_contacts) {
        this.profile_contacts = profile_contacts;
    }


    public String getProfile_name() {
        return profile_name;
    }
    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }


}
