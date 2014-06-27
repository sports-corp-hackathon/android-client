package com.github.ischack.android.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.github.ischack.android.R;

/**
 * Created by david on 5/4/14
 * This implementation of TextWatcher makes sure that a given EditText is a valid email address.
 * If the email is invalid, it sets the given button to not enabled.
 * If it is valid, it sets a checkmark to the edittext and enables the button.
 */
public class EmailTextWatcher implements TextWatcher {
    private final EditText emailBox;
    private final Button button;
    private final Context context;

    private Drawable d;

    public EmailTextWatcher(Context c, EditText emailBox, Button button) {
        this.context = c;
        this.emailBox = emailBox;
        this.button = button;

        // Read your drawable from somewhere
        Drawable dr = c.getResources().getDrawable(R.drawable.check);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        // Scale it to 50 x 50
        d = new BitmapDrawable(c.getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            Log.d("Email", "Current string is not an email");
            emailBox.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            button.setEnabled(false);
        } else {
            Log.d("Email", "Current string is an email!");
            emailBox.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
            button.setEnabled(true);
        }
    }

}
