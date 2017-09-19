package com.example.tolean.ibook.viewinterface;

import com.example.tolean.ibook.bean.Book;

/**
 * Created by Tolean on 2017/9/10.
 */

public interface IGetBookDetailView {
    void onSuccess(Book book);
    void onFailure();
}
