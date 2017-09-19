package com.example.tolean.ibook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tolean.ibook.R;
import com.example.tolean.ibook.utils.CommonUtil;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by Tolean on 2017/9/16.
 */

public class ThemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private onItemClickListener mItemClickListener=null;
    private static Context mContext;
    private static int[] mColor;
    public ThemRecyclerViewAdapter(int[] color){
        mColor= color;
    }
    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        mItemClickListener=onItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        final View view_= LayoutInflater.from(parent.getContext()).inflate(R.layout.them_card,parent,false);
        ViewHolder viewHolder_=new ViewHolder(view_);
        viewHolder_.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemClickListener!=null){
                    //在这里获取在recyclerview中的位置
                    mItemClickListener.onClick((int)view_.getTag());
                }
            }
        });
        return viewHolder_;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).mCardView.setBackgroundColor(mColor[position]);
        //起到了标记坐标的作用
        holder.itemView.setTag(position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            mCardView=itemView.findViewById(R.id.them_card);
        }
    }
    @Override
    public int getItemCount() {
        return mColor.length;
    }
    public static interface onItemClickListener {
        public void onClick(int position);
    }
}
