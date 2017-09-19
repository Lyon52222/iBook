package com.example.tolean.ibook.viewinterface;

import com.example.tolean.ibook.bean.Book;

import java.util.List;

/**
 * Created by Tolean on 2017/9/9.
 */

public interface IGetBookView {
    void onSuccess(List<Book>books, boolean isLoadMore);
    void onFailure();
}
