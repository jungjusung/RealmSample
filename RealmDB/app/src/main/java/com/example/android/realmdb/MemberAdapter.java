package com.example.android.realmdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jusung on 2017. 1. 18..
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberViewHolder>{

    String TAG;
    private int mNumberItems;
    private static int viewHolderCount;
    Context context;


    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    public MemberAdapter(int items,Context context,ListItemClickListener listener) {
        TAG=this.getClass().getName();
        this.context=context;

        mNumberItems = items;
        mOnClickListener=listener;
        viewHolderCount=0;
    }



    public MemberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context=viewGroup.getContext();
        int listItem=R.layout.list_item;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view=inflater.inflate(listItem,viewGroup,shouldAttachToParentImmediately);
        MemberViewHolder memberViewHolder=new MemberViewHolder(view,context,mOnClickListener);

        viewHolderCount++;
        return memberViewHolder;
    }

    public void setItem(int size){
        mNumberItems=size;
    }
    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public void removeViewHolder(){
        viewHolderCount--;
    }
}
