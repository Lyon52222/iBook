package com.example.tolean.ibook.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tolean.ibook.R;
import com.example.tolean.ibook.bean.Book;
import com.example.tolean.ibook.utils.CommonUtil;
import com.example.tolean.ibook.utils.GsonUtil;
import com.example.tolean.ibook.view.BookDetailActivity;
import com.example.tolean.ibook.view.MainActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tolean on 2017/9/9.
 */

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int mPosition;
    private int add_count;
    private List<Book> mBooks=new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context mContext=null;
    public BookRecyclerViewAdapter(int position,List<Book>books){
        mPosition=position;
        mBooks=books;
        add_count=books.size()==0?(int)CommonUtil.SEARCH_BOOK_COUNT:books.size();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null) {
            mContext = parent.getContext();
            mLayoutInflater=LayoutInflater.from(mContext);
        }
        RecyclerView.ViewHolder viewHolder_=null;
        if(viewType== CommonUtil.RECYCLERVIEW_ITEM){
            View view_=mLayoutInflater.inflate(R.layout.recyclerview_item,parent,false);
            viewHolder_= new ItemViewHolder(view_);
        }else {
            View view_=mLayoutInflater.inflate(R.layout.recyclerview_bottom,parent,false);
            viewHolder_=new BottomViewHolder(view_);
        }
        return viewHolder_;
    }
    public void addItems(List<Book>books){
        mBooks.addAll(books);
        add_count=books.size();
    }
    public void setAdd_count(int add_count){
        this.add_count=add_count;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BottomViewHolder){
            if(add_count<CommonUtil.SEARCH_BOOK_COUNT){
                ((BottomViewHolder) holder).tv.setText(R.string.no_more_warning);
                ((BottomViewHolder) holder).mProgressBar.setVisibility(View.GONE);
            }else{
                ((BottomViewHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((BottomViewHolder) holder).tv.setText(R.string.loading);
            }
        }else{
            final Book book_=mBooks.get(position);
            for (String author:book_.getAuthor()){
                ((ItemViewHolder)holder).author.setText("作者:");
                ((ItemViewHolder)holder).author.append(author+" ");
            }
            ((ItemViewHolder)holder).price.setText("价格:"+book_.getPrice());
            ((ItemViewHolder)holder).title.setText(book_.getTitle());
            ((ItemViewHolder)holder).publisher.setText("出版社:"+book_.getPublisher());
            ((ItemViewHolder)holder).rate_number.setText(book_.getRating().getAverage());
            ((ItemViewHolder)holder).rate.setRating(Float.parseFloat(book_.getRating().getAverage())/2);
            Glide.with(mContext).load(book_.getImage()).placeholder(R.drawable.ic_book_subjectcover_default).into(((ItemViewHolder)holder).iv);
            ((ItemViewHolder)holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle_=new Bundle();
                    //在这里将Book在转换为Json 传递. 避免二次请求
                    bundle_.putString("book_json", GsonUtil.bookToGson(book_));
                    bundle_.putString("book_id",book_.getId());
                    bundle_.putInt("mode",BookDetailActivity.MODE_ID);
                    Intent intent_=new Intent(mContext, BookDetailActivity.class);
                    intent_.putExtras(bundle_);
                    //实现共享元素
                    mContext.startActivity(intent_, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,((ItemViewHolder)holder).iv,"sharedView").toBundle());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mBooks.size()+1;
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.book_image)
        ImageView iv;
        @BindView(R.id.book_author)
        TextView author;
        @BindView(R.id.book_publisher)
        TextView publisher;
        @BindView(R.id.book_price)
        TextView price;
        @BindView(R.id.book_rate_number)
        TextView rate_number;
        @BindView(R.id.book_rate)
        RatingBar rate;
        @BindView(R.id.book_title)
        TextView title;
        @BindView(R.id.list_item_layout)
        LinearLayout layout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    protected class BottomViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_recyclerview_bottom)
        TextView tv;
        @BindView(R.id.progress_recyclerview_bottom)
        ProgressBar mProgressBar;
        public BottomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position==mBooks.size()){
            return CommonUtil.RECYCLERVIEW_BOTTOM;
        }else{
            return CommonUtil.RECYCLERVIEW_ITEM;
        }
    }
}
