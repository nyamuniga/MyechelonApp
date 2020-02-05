package co.myechelon.a2myechelon;

import java.util.Date;

/**
 * Created by Teacher on 6/15/2018.
 */

public class Texts {
    private String message,product_name,from,to;
    private long sendtime;

    public Texts() {

    }

    public Texts(String message, String product_name,String from,String to, long sendtime) {
        this.message = message;
        this.product_name = product_name;
        this.sendtime = sendtime;
        this.from=from;
        this.to=to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Long getSendtime() {
        return sendtime;
    }

    public void setSendtime(Long sendtime) {
        this.sendtime = sendtime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
