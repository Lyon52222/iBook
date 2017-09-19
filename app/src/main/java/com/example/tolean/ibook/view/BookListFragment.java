package com.example.tolean.ibook.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tolean.ibook.R;
import com.example.tolean.ibook.adapter.BookRecyclerViewAdapter;
import com.example.tolean.ibook.bean.Book;
import com.example.tolean.ibook.presenter.BookPresenter;
import com.example.tolean.ibook.utils.CommonUtil;
import com.example.tolean.ibook.utils.ThemUtil;
import com.example.tolean.ibook.viewinterface.IGetBookView;
import com.example.tolean.ibook.my_view.EmptyLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.RecyclerView.OnScrollListener;


/**
 * Created by Tolean on 2017/9/9.
 */

public class BookListFragment extends Fragment implements IGetBookView {
    @BindView(R.id.booklist_recyclerview)
    RecyclerView mBooklistRecyclerview;
    @BindView(R.id.booklist_swiperefresh)
    SwipeRefreshLayout mBooklistSwiperefresh;
    @BindView(R.id.booklist_fab)
    FloatingActionButton mBooklistFab;
    @BindView(R.id.booklist_empty_view)
    EmptyLayout mEmptyLayout;
    private Unbinder unbinder;
    private int mLastVisiableItemPosition;
    private int mPositon;
    private int mStart;
    private Activity mContext = null;
    private LinearLayoutManager mLayoutManager = null;
    private List<Book> mBooks = new ArrayList<>();
    private BookPresenter mBookPresenter = null;
    private BookRecyclerViewAdapter mRecyclerViewAdapter=null;
    private ThemChangeReceiver mThemChangeReceiver=null;
    //需要定义否则横竖屏切换会崩溃,
    public BookListFragment(){}
//    public BookListFragment(int position) {
//        mPositon = position;
//    }

    public static BookListFragment newInstance(int position) {
        
        Bundle args = new Bundle();
        args.putInt("position",position);
        BookListFragment fragment = new BookListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void initThem(){
        int[][] states = new int[][]{
                new int[]{ -android.R.attr.state_checked}};
        int[] colors = new int[]{ThemUtil.getThemColor()};
        ColorStateList csl = new ColorStateList(states, colors);
        mBooklistFab.setBackgroundTintList(csl);
        mBooklistSwiperefresh.setColorSchemeColors(ThemUtil.getThemColor());
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle args=getArguments();
        mPositon=args.getInt("position");
        mThemChangeReceiver=new ThemChangeReceiver();
        IntentFilter intentFilter_=new IntentFilter();
        intentFilter_.addAction(CommonUtil.BROADCASETHEMCHANGE);
        getActivity().registerReceiver(mThemChangeReceiver,intentFilter_);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBookPresenter = BookPresenter.getSingletonPresenter();
        mBookPresenter.getBooksByTag(this, 0, CommonUtil.VIEWPAGER_TAGS[mPositon], false);
        initThem();
        //mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booklist, container, false);
        ButterKnife.bind(this,view);
        mContext = this.getActivity();

        mBooklistSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mBooklistFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBooklistRecyclerview.scrollToPosition(0);
            }
        });
        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        mLayoutManager = new LinearLayoutManager(mContext);
        mBooklistRecyclerview.setLayoutManager(mLayoutManager);
        if(mRecyclerViewAdapter==null) {
            mRecyclerViewAdapter = new BookRecyclerViewAdapter(mPositon, mBooks);
        }
        mBooklistRecyclerview.setAdapter(mRecyclerViewAdapter);
        mBooklistRecyclerview.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==mBooklistRecyclerview.SCROLL_STATE_IDLE&&mLastVisiableItemPosition+1==mRecyclerViewAdapter.getItemCount() ){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBookPresenter.getBooksByTag(BookListFragment.this,mStart+mRecyclerViewAdapter.getItemCount(),CommonUtil.VIEWPAGER_TAGS[mPositon],true);
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
        unbinder=ButterKnife.bind(this, view);
        return view;
    }

    private void refresh(){
        Random random_=new Random();
        mStart=random_.nextInt(100);
        mBookPresenter.getBooksByTag(this,mStart,CommonUtil.VIEWPAGER_TAGS[mPositon],false);
    }
    @Override
    public void onSuccess(List<Book> books, boolean isLoadMore) {
        if(!isLoadMore){
            mBooks.clear();
            mBooks.addAll(books);
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mRecyclerViewAdapter!=null){
                        mRecyclerViewAdapter.setAdd_count(mBooks.size());
                        mBooklistRecyclerview.setAdapter(mRecyclerViewAdapter);
                        mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    }
                }
            });
//            mRecyclerViewAdapter.refresh(mBooks);
//            mRecyclerViewAdapter.notifyDataSetChanged();
            mBooklistSwiperefresh.setRefreshing(false);
        }else {
            mBooks.addAll(books);
            mRecyclerViewAdapter.addItems(books);
            mRecyclerViewAdapter.notifyDataSetChanged();
            mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        }
    }

    @Override
    public void onFailure() {
        if(CommonUtil.isConnectivity(mContext)){
            mEmptyLayout.setErrorType(EmptyLayout.NODATA);
        }else{
            mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }
        mBooklistSwiperefresh.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    private void handleThemChange(){
        int[][] states = new int[][]{
                new int[]{ -android.R.attr.state_checked}};
        int[] colors = new int[]{ThemUtil.getThemColor()};
        ColorStateList csl = new ColorStateList(states, colors);
        mBooklistFab.setBackgroundTintList(csl);
        mBooklistSwiperefresh.setColorSchemeColors(ThemUtil.getThemColor());
    }
    private class ThemChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleThemChange();
        }
    }
}
