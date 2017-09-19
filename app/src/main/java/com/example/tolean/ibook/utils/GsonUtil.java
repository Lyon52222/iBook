package com.example.tolean.ibook.utils;

import com.example.tolean.ibook.bean.Book;
import com.google.gson.Gson;

/**
 * Created by Tolean on 2017/9/17.
 */

public class GsonUtil {
    private static Gson mGson=new Gson();
    public static Book gsonToBook(String json){
        return mGson.fromJson(json,Book.class);
    }
    public static String bookToGson(Book book){
        return mGson.toJson(book);
    }
}
