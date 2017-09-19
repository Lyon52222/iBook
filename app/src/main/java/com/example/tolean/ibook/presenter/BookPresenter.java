package com.example.tolean.ibook.presenter;

import com.example.tolean.ibook.bean.Book;
import com.example.tolean.ibook.bean.BookResponse;
import com.example.tolean.ibook.douban.RequestInterface;
import com.example.tolean.ibook.utils.CommonUtil;
import com.example.tolean.ibook.viewinterface.IGetBookDetailView;
import com.example.tolean.ibook.viewinterface.IGetBookSearch;
import com.example.tolean.ibook.viewinterface.IGetBookView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tolean on 2017/9/10.
 */

public class BookPresenter {
    private static volatile BookPresenter sBookPresenter=null;
    private Retrofit mRetrofit;
    private RequestInterface mResponseInterface;

    private BookPresenter(){
        mRetrofit=new Retrofit.Builder().baseUrl(CommonUtil.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        mResponseInterface=mRetrofit.create(RequestInterface.class);
    }
    //单列模式,双重效验锁
    public static BookPresenter getSingletonPresenter(){
        if (sBookPresenter==null){
            synchronized (BookPresenter.class){
                if(sBookPresenter==null){
                    sBookPresenter=new BookPresenter();
                }
            }
        }
        return sBookPresenter;
    }
//  q 查询关键字
//  tag 查询的tag
//  q和tag必传其一
//  start 取结果的offset 默认为0
//  count 取结果的条数  默认为20，最大为100
    public void getBooksByTag(final IGetBookView iGetBookView, int start, String tag, final boolean isLoadMore){
        Map<String,String>options=new HashMap<String, String>();
        options.put("q","");
        options.put("tag",tag);
        options.put("start",String.valueOf(start));
        options.put("count",String.valueOf(CommonUtil.SEARCH_BOOK_COUNT));
        Call<BookResponse>call_=mResponseInterface.getBookData(options);
        call_.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if(response!=null&&response.body()!=null){
                    iGetBookView.onSuccess(response.body().getBooks(),isLoadMore);
                }else{
                    iGetBookView.onFailure();
                }
            }
            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                iGetBookView.onFailure();
            }
        });
    }
    public void getBookByKeyWord(final IGetBookSearch iGetBookSearch, int start, String keyword, final boolean isloadmore){
        Map<String,String>options=new HashMap<String, String>();
        options.put("q",keyword);
        options.put("tag","");
        options.put("start",String.valueOf(start));
        options.put("count",String.valueOf(CommonUtil.SEARCH_BOOK_COUNT));
        Call<BookResponse>call_=mResponseInterface.getBookData(options);
        call_.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if(response!=null&&response.body()!=null){
                    iGetBookSearch.onSuccess(response.body().getBooks(),response.body().getTotal(),isloadmore);
                }else{
                    iGetBookSearch.onFailure();
                }
            }
            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                iGetBookSearch.onFailure();
            }
        });
    }
    public void getBooksById(final IGetBookDetailView iGetBookDetailView,String id){
        Call<Book>call_=mResponseInterface.getBookDetail(id);
        call_.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if(response!=null&&response.body()!=null){
                    iGetBookDetailView.onSuccess(response.body());
                }else {
                    iGetBookDetailView.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                iGetBookDetailView.onFailure();
            }
        });
    }
    public void getBookByIbsn(final IGetBookDetailView iGetBookDetailView,String ibsn){
        Call<Book>call_=mResponseInterface.getBookScan(ibsn);
        call_.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if(response!=null&&response.body()!=null){
                    iGetBookDetailView.onSuccess(response.body());
                }else {
                    iGetBookDetailView.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                iGetBookDetailView.onFailure();
            }
        });
    }
}
