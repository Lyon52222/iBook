package com.example.tolean.ibook.view;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.example.tolean.ibook.R;
import com.example.tolean.ibook.adapter.BookRecyclerViewAdapter;
import com.example.tolean.ibook.base.BaseActivity;
import com.example.tolean.ibook.bean.Book;
import com.example.tolean.ibook.my_view.EmptyLayout;
import com.example.tolean.ibook.my_view.SearchView;
import com.example.tolean.ibook.presenter.BookPresenter;
import com.example.tolean.ibook.utils.CommonUtil;
import com.example.tolean.ibook.utils.ThemUtil;
import com.example.tolean.ibook.viewinterface.IGetBookSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchActivity extends BaseActivity implements IGetBookSearch {

    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.booklist_recyclerview)
    RecyclerView mBooklistRecyclerview;
    @BindView(R.id.booklist_swiperefresh)
    SwipeRefreshLayout mBooklistSwiperefresh;
    @BindView(R.id.booklist_fab)
    FloatingActionButton mBooklistFab;
    @BindView(R.id.booklist_empty_view)
    EmptyLayout mBooklistEmptyView;

    private BookPresenter mBookPresenter=null;
    private LinearLayoutManager mLayoutManager=null;
    private BookRecyclerViewAdapter mRecyclerViewAdapter=null;
    private String mKeyWord="";
    private int mStart=0;
    private List<Book> mBooks=new ArrayList<>();
    private int mLastVisiableItemPosition;
    private Unbinder mUnbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initThem();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);
        mUnbinder=ButterKnife.bind(this);
        mBookPresenter=BookPresenter.getSingletonPresenter();
        mLayoutManager=new LinearLayoutManager(this);
        mBooklistRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerViewAdapter=new BookRecyclerViewAdapter(0,mBooks);
        mBooklistRecyclerview.setAdapter(mRecyclerViewAdapter);
        mBooklistEmptyView.setVisibility(View.GONE);
        mBooklistRecyclerview.setVisibility(View.GONE);
        setListener();
    }

    @Override
    protected void initThem() {
        mSearchView.setBackgroundColor(ThemUtil.getThemColor());
        applyKitKatTranslucency(ThemUtil.getThemColor());
        int[][] states = new int[][]{
                new int[]{ -android.R.attr.state_checked}};
        int[] colors = new int[]{ThemUtil.getThemColor()};
        ColorStateList csl = new ColorStateList(states, colors);
        mBooklistFab.setBackgroundTintList(csl);
        mBooklistSwiperefresh.setColorSchemeColors(ThemUtil.getThemColor());
    }
    @Override
    protected void handleThemChange() {
        mSearchView.setBackgroundColor(ThemUtil.getThemColor());
        applyKitKatTranslucency(ThemUtil.getThemColor());
        int[][] states = new int[][]{
                new int[]{ -android.R.attr.state_checked}};
        int[] colors = new int[]{ThemUtil.getThemColor()};
        ColorStateList csl = new ColorStateList(states, colors);
        mBooklistFab.setBackgroundTintList(csl);
        mBooklistSwiperefresh.setColorSchemeColors(ThemUtil.getThemColor());
    }
    private void setListener(){
        mBooklistEmptyView.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        mSearchView.setSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearch(String text) {
                mKeyWord=text;
                mBooklistEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
                mBookPresenter.getBookByKeyWord(SearchActivity.this,mStart,mKeyWord,false);
            }
        });
        mBooklistSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mBooklistRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==mBooklistRecyclerview.SCROLL_STATE_IDLE&&mLastVisiableItemPosition+1==mRecyclerViewAdapter.getItemCount() ){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBookPresenter.getBookByKeyWord(SearchActivity.this,mStart+mRecyclerViewAdapter.getItemCount(), mKeyWord,true);
                        }
                    },1000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisiableItemPosition=mLayoutManager.findLastVisibleItemPosition();
                if(mLayoutManager.findFirstVisibleItemPosition()!=0){
                    mBooklistFab.setVisibility(View.VISIBLE);
                }else {
                    mBooklistFab.setVisibility(View.GONE);
                }
            }
        });
        mBooklistFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayoutManager.scrollToPosition(0);
            }
        });
    }

    private void refresh(){
        if(!mKeyWord.equals("")) {
            Random random_ = new Random();
            mStart = random_.nextInt(50);
            mBookPresenter.getBookByKeyWord(SearchActivity.this, mStart, mKeyWord, false);
        }else{
            mBooklistSwiperefresh.setRefreshing(false);
        }
    }
    @Override
    public void onSuccess(List<Book> books, int count, boolean isLoadMore) {
        if(count==0){
            mBooklistEmptyView.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
            mBooklistEmptyView.setVisibility(View.VISIBLE);
            mBooklistRecyclerview.setVisibility(View.GONE);
        }else {
            if (!isLoadMore) {
                Snackbar.make(mBooklistRecyclerview,"共搜索到"+String.valueOf(count)+"条数据",Snackbar.LENGTH_LONG).show();
                mBooks.clear();
                mBooks.addAll(books);
                if (mRecyclerViewAdapter != null) {
                    mRecyclerViewAdapter.setAdd_count(books.size());
                    mBooklistRecyclerview.setAdapter(mRecyclerViewAdapter);
                }
                mBooklistSwiperefresh.setRefreshing(false);
            } else {
                mRecyclerViewAdapter.addItems(books);

            }
            mRecyclerViewAdapter.notifyDataSetChanged();
            mBooklistEmptyView.setVisibility(View.GONE);
            mBooklistRecyclerview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure() {
        if(CommonUtil.isConnectivity(this)){
            mBooklistEmptyView.setErrorType(EmptyLayout.NODATA);
        }else{
            mBooklistEmptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
        }
        mBooklistSwiperefresh.setRefreshing(false);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
