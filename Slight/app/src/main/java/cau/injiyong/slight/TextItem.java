package cau.injiyong.slight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TextItem {

    private String text;
    private String date;
    private String photoUrl;

    public TextItem() {
    }

    public TextItem(String text, String date, String photoUrl) {
        this.text = text;
        this.date = date;
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}