package com.example.tolean.ibook.douban;

import com.example.tolean.ibook.bean.Book;
import com.example.tolean.ibook.bean.BookResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Tolean on 2017/9/10.
 */

public interface RequestInterface {
    /*
    //获取详情数据
    @GET("book/search")
    Call<BooksResponse> getBookData(@Query("q") String name,
                           @Query("tag") String tag,
                           @Query("start") int start,
                           @Query("count") int count);
*/
//根据tag查找图书
    @GET("book/search")
    Call<BookResponse> getBookData(@QueryMap Map<String, String> options);
    //根据id查找图书
    @GET("book/{id}")
    Call<Book> getBookDetail(@Path("id") String id);
    //根据条形码查找图书
    @GET("book/isbn/{name}")
    Call<Book> getBookScan(@Path("name") String id);
}
