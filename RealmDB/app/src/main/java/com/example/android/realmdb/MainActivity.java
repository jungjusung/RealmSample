package com.example.android.realmdb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MemberAdapter.ListItemClickListener{

    EditText mEditTextMemberName;

    EditText mEditTextMemberAge;
    EditText mEditTextMemberGender;
    EditText mEditTextMemberEmail;


    Button mButtonAddMember;
    String TAG=this.getClass().getName();
    private static final int ITEM_NUM=10;
    private MemberAdapter mMemberAdapter;
    private RecyclerView mMemberList;
    static int SEQUNCE_NUMBER=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextMemberName=(EditText)findViewById(R.id.ed_member_name);
        mEditTextMemberAge=(EditText)findViewById(R.id.ed_member_age);
        mEditTextMemberGender=(EditText)findViewById(R.id.ed_member_gender);
        mEditTextMemberEmail=(EditText)findViewById(R.id.ed_member_email);

        mButtonAddMember=(Button)findViewById(R.id.bt_add_member);
        mButtonAddMember.setOnClickListener(this);

        mMemberList=(RecyclerView)findViewById(R.id.rv_memberList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mMemberList.setLayoutManager(layoutManager);
        mMemberList.setHasFixedSize(true);


        final Context context=this.getApplicationContext();
        Realm.init(context);
        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<Member> memberSize = realm.where(Member.class).findAll();
        mMemberAdapter=new MemberAdapter(memberSize.size(),context,this);
        mMemberList.setAdapter(mMemberAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.bt_add_member){

            final Context context=this.getApplicationContext();
            Realm.init(context);

            final Realm realm = Realm.getDefaultInstance();

            long now = System.currentTimeMillis();
            Date date = new Date(now);

            int memberSize = getMemberSize(realm);
            Toast.makeText(context,"memberSize : "+memberSize, Toast.LENGTH_SHORT).show();

            Member member = new Member();
            SEQUNCE_NUMBER+=1;

            realm.beginTransaction();
            member.setMember_id(SEQUNCE_NUMBER);
            member.setMember_name(mEditTextMemberName.getText().toString());
            member.setMember_age(Integer.parseInt(mEditTextMemberAge.getText().toString()));
            member.setMember_gender(mEditTextMemberGender.getText().toString());
            member.setMember_email(mEditTextMemberEmail.getText().toString());
            member.setMember_regdata(date.toString());
            realm.insertOrUpdate(member);
            realm.commitTransaction();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context,"memberSize : "+getMemberSize(realm), Toast.LENGTH_SHORT).show();
                    mEditTextMemberName.setText("");
                    mEditTextMemberAge.setText("");
                    mEditTextMemberGender.setText("");
                    mEditTextMemberEmail.setText("");

                    mMemberAdapter.setItem(getMemberSize(realm));
                    mMemberAdapter.notifyDataSetChanged();
                }
            });

        }
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {

        final int itemNum=clickedItemIndex+1;
        Context context=this.getApplicationContext();
        Realm.init(context);
        final Realm realm = Realm.getDefaultInstance();
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
        alert_confirm.setMessage("No."+itemNum+" 데이터를 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                RealmResults<Member> result = realm.where(Member.class).findAll();
                                result.deleteFromRealm(itemNum-1);
                                //result.deleteAllFromRealm();
                                //result = realm.where(Member.class).findAll();
                                Log.d(TAG,itemNum+"아이템 인덱스");
                                //Toast.makeText(MainActivity.this,item, Toast.LENGTH_SHORT).show();
                                mMemberAdapter.setItem(getMemberSize(realm));
                                mMemberAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }

    public int getMemberSize(Realm realm){
        RealmResults<Member> memberSize = realm.where(Member.class).findAll();
        return memberSize.size();
    }

}
