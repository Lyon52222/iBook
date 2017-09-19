package com.example.tolean.ibook.utils;

/**
 * Created by Tolean on 2017/9/13.
 */

public class TextUtil {
    public static boolean isEmpty(String string){
        if(string==null||string.equals("")){
            return true;
        }else{
            return false;
        }
    }
}
