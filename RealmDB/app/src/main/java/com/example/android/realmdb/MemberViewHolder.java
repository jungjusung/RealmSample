package com.example.android.realmdb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Jusung on 2017. 1. 18..
 */

public class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mMemberId;
    private TextView mMemberName;
    private TextView mMemberAge;
    private TextView mMemberGender;
    private TextView mMemberEmail;


    private TextView mMemberRegdate;
    private Context context;
    private Realm realm;
    private RealmResults<Member> memberSize;

    private MemberAdapter.ListItemClickListener mOnClickListener;

    public MemberViewHolder(View itemView, Context context, MemberAdapter.ListItemClickListener mOnClickListener) {
        super(itemView);
        this.context = context;
        Realm.init(context);
        this.mOnClickListener = mOnClickListener;
        realm = Realm.getDefaultInstance();
        itemView.setOnClickListener(this);

        memberSize = realm.where(Member.class).findAll();

        mMemberId = (TextView) itemView.findViewById(R.id.tv_member_id);
        mMemberName = (TextView) itemView.findViewById(R.id.tv_member_name);
        mMemberAge = (TextView) itemView.findViewById(R.id.tv_member_age);
        mMemberGender = (TextView) itemView.findViewById(R.id.tv_member_gender);
        mMemberEmail = (TextView) itemView.findViewById(R.id.tv_member_email);
        mMemberRegdate = (TextView) itemView.findViewById(R.id.tv_member_regdate);
    }

    void bind(int listIndex) {
        RealmResults<Member> result = memberSize.where().findAll();
        Member member = result.get(listIndex);
        mMemberId.setText(String.valueOf(member.getMember_id()));
        mMemberName.setText(member.getMember_name());
        mMemberAge.setText(String.valueOf(member.getMember_age()));
        mMemberGender.setText(member.getMember_gender());
        mMemberEmail.setText(member.getMember_email());
        mMemberRegdate.setText(member.getMember_regdata());

    }

    @Override
    public void onClick(View view) {
        final int clickedPosition = getAdapterPosition();
        mOnClickListener.onListItemClick(clickedPosition);


    }
}
