package com.example.tolean.ibook.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tolean.ibook.R;
import com.example.tolean.ibook.bean.Book;
import com.example.tolean.ibook.utils.GsonUtil;
import com.example.tolean.ibook.view.BookDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tolean on 2017/9/18.
 */

public class CoverFlowAdapter extends BaseAdapter {
    private Context mContext;
    private List<Book>mBooks=new ArrayList<>();
    public CoverFlowAdapter(Context context, List<Book> books){
        mBooks.clear();
        mBooks.addAll(books);
        mContext=context;
    }
    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int i) {
        return mBooks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Book book_=mBooks.get(i);
         View rowView=view;
        if(rowView==null){
            LayoutInflater inflater_=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView=inflater_.inflate( R.layout.favorites_item,null);
            ViewHolder viewHolder_=new ViewHolder();
            viewHolder_.mImageView=rowView.findViewById(R.id.image);
            rowView.setTag(viewHolder_);
        }
        final ViewHolder holder_=(ViewHolder)rowView.getTag();
        Glide.with(mContext).load(mBooks.get(i).getImages().getLarge()).into(holder_.mImageView);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle_=new Bundle();
                //在这里将Book在转换为Json 传递. 避免二次请求
                bundle_.putString("book_json", GsonUtil.bookToGson(book_));
                bundle_.putString("book_id",book_.getId());
                bundle_.putInt("mode", BookDetailActivity.MODE_ID);
                Intent intent_=new Intent(mContext, BookDetailActivity.class);
                intent_.putExtras(bundle_);
                mContext.startActivity(intent_);
//                ((Activity)mContext).startActivityForResult(intent_,0);
                //实现共享元素
//                mContext.startActivity(intent_, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,holder_.mImageView,"sharedView").toBundle());
            }
        });
        return rowView;
    }
    public void refreshdate(List<Book>books){
        mBooks.clear();
        mBooks.addAll(books);
//        notifyDataSetInvalidated();
    }
    public Book getBook(int position){
        return mBooks.get(position);
    }
    public void setTitle(View view,int position){
        ((Toolbar)view).setTitle(mBooks.get(position).getTitle());
    }
    static class ViewHolder{
        public ImageView mImageView;
    }
}
