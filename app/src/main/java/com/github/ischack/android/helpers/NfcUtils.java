package com.github.ischack.android.helpers;

import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcV;

/**
 * Created by david on 6/28/14.
 */
public final class NfcUtils {

    public static final String[][] techList = new String[][] {
            new String[] {
                    NfcV.class.getName(),
                    NdefFormatable.class.getName()
            }
    };


    public static String ByteArrayToHexString(byte [] inarray) { //converts byte arrays to string
        if(inarray == null)
            return "null";

        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = inarray.length - 1 ; j >= 0 ; j--)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


}
