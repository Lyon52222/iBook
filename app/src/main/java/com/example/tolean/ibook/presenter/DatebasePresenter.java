package com.example.tolean.ibook.presenter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tolean.ibook.bean.Book;
import com.example.tolean.ibook.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tolean on 2017/9/17.
 */

public class DatebasePresenter extends SQLiteOpenHelper {
    private static final String BOOKDATEBASENAME="MyBookDatebase";
    private static volatile DatebasePresenter sDatebasePresenter;
    private SQLiteDatabase mDatabase;

    public DatebasePresenter(Context context) {
        super(context, BOOKDATEBASENAME, null, 1);
    }

    //    private DatebasePresenter(Context context){
//        super(context,BOOKDATEBASENAME,null);
//    }

    public List<Book> getBooks(){
        ArrayList<Book> books_=new ArrayList<Book>();
        mDatabase=getReadableDatabase();
        Cursor cursor_=mDatabase.query("books",null,null,null,null,null,null);
        if(cursor_.moveToFirst()){
            do {
                books_.add(GsonUtil.gsonToBook(cursor_.getString(2)));
            }while (cursor_.moveToNext());
        }
        return books_;
    }
    public static DatebasePresenter getSingletonPresenter(Context context){
        if(sDatebasePresenter==null){
            synchronized (DatebasePresenter.class){
                if (sDatebasePresenter == null) {
                    sDatebasePresenter=new DatebasePresenter(context);
                }
            }
        }
        return sDatebasePresenter;
    }
    public void saveInDatebase(Book book){
        if(judgeIsCollect(book)) return;
        mDatabase=getWritableDatabase();
        mDatabase.execSQL("insert into books values(null,?,?)",new String[]{book.getId(), GsonUtil.bookToGson(book)});
    }
    public boolean judgeIsCollect(Book book){
        mDatabase=getReadableDatabase();
        Cursor cursor_=mDatabase.rawQuery("select book_id from books where book_id = ?",new String[]{book.getId()});
        int count_=cursor_.getCount();
        cursor_.close();
        return count_!=0;
    }
    public void deleteInDatebase(Book book){
        if(judgeIsCollect(book)){
            mDatabase=getWritableDatabase();
            mDatabase.execSQL("delete from books where book_id = ?",new String[]{book.getId()});
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists books(id integer primary key autoincrement," +
                "book_id text,book_json text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
