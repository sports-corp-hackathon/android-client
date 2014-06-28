package com.github.ischack.android.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by david on 6/27/14 for android
 */
public class Game {

    private String name;
    private Uri imageUrl;
    private WeakReference<Bitmap> image;
    private String rules;
    private String information;

    public Game() {
        image = new WeakReference<Bitmap>(null);
        name = "<GAME NAME>";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage()
    {
        Bitmap bitmap = image.get();
        return bitmap;
    }

    public void setImage(Bitmap b) {
        image = new WeakReference<Bitmap>(b);
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public static class ViewHolder {
        ImageView iv;
        TextView tv;
        
        public ViewHolder(ImageView iv, TextView tv) {
            this.tv = tv;
            this.iv = iv;
        }

        public ImageView getImageView() {
            return iv;
        }

        public TextView getTextView() {
            return tv;
        }
    }
}

