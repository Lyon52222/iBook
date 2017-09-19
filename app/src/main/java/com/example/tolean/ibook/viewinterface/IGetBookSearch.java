package com.example.tolean.ibook.viewinterface;

import com.example.tolean.ibook.bean.Book;

import java.util.List;

/**
 * Created by Tolean on 2017/9/10.
 */

public interface IGetBookSearch {
    void onSuccess(List<Book> books,int count,boolean isLoadMore);
    void onFailure();
}
