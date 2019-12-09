package cau.injiyong.slight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TextItem {

    private String text;
    private String date;
    private String color;

    public TextItem() {
    }

    public TextItem(String text, String date, String color) {
        this.text = text;
        this.date = date;
        this.color = color;
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

    public String getColor() {
        return color;
    }

}