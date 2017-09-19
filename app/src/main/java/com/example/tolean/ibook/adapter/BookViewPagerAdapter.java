package com.example.tolean.ibook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tolean.ibook.utils.CommonUtil;
import com.example.tolean.ibook.view.BookListFragment;

/**
 * Created by Tolean on 2017/9/9.
 */

public class BookViewPagerAdapter extends FragmentStatePagerAdapter {
    String[] mBookTags;
    public BookViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mBookTags= CommonUtil.VIEWPAGER_TAGS;
    }

    @Override
    public Fragment getItem(int position) {
        return  BookListFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mBookTags[position];
    }
    @Override
    public int getCount() {
        return mBookTags.length;
    }

}
