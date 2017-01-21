package com.example.android.realmdb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.util.regex.Pattern;

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
    private MemberAdapter mMemberAdapter;
    private RecyclerView mMemberList;
    static int SEQUNCE_NUMBER=0;
    //DB의 시퀀스를 표현해주기 위한 전역변수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextMemberName=(EditText)findViewById(R.id.ed_member_name);
        mEditTextMemberAge=(EditText)findViewById(R.id.ed_member_age);
        mEditTextMemberAge.setFilters(new InputFilter[]{filterNum});
        mEditTextMemberGender=(EditText)findViewById(R.id.ed_member_gender);
        mEditTextMemberGender.setFilters(new InputFilter[]{filterGender,new InputFilter.LengthFilter(1)});
        mEditTextMemberEmail=(EditText)findViewById(R.id.ed_member_email);

        mButtonAddMember=(Button)findViewById(R.id.bt_add_member);
        mButtonAddMember.setOnClickListener(this);

        mMemberList=(RecyclerView)findViewById(R.id.rv_memberList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mMemberList.setLayoutManager(layoutManager);
        mMemberList.setHasFixedSize(true);

        //현재 가지고 있는 디비의 전체크기 만큼, 어답터의 크기를 정해주게 된다
        //init으로 디비의 접근하고, 해당 쿼리문을 각 메서드로 호출해서 결과 값을 얻는다.

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

            //Insert 과정
            Realm.init(context);
            final Realm realm = Realm.getDefaultInstance();

            // DB데이터 중 현재 시간을 표기하기 위한 Date 객체
            long now = System.currentTimeMillis();
            Date date = new Date(now);

            int memberSize = getMemberSize(realm);

            Toast.makeText(context,"memberSize : "+memberSize, Toast.LENGTH_SHORT).show();

            Member member = new Member();
            SEQUNCE_NUMBER+=1;

            // 트랜잭션을 열고, 디비 데이터를 insert 완료하면 커미을 하게 된다.
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
                    //해당 작업을 동기화로 실행 완료시 성공시 해당 작업을 수행
                    //데이터의 증가를 Toast로 표기한다.

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

                                //Delete 시 수행 과정
                                //전체 디비 데이터중, 리싸이클러뷰의 뷰홀더의 위치와 같은 것을 제거해준다.

                                RealmResults<Member> result = realm.where(Member.class).findAll();
                                result.deleteFromRealm(itemNum-1);

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
    public InputFilter filterNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[0-9]*$");
            if (!ps.matcher(source).matches()) {
                Toast.makeText(MainActivity.this,"숫자만을 입력하세요.", Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        }
    };
    public InputFilter filterGender = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[M,F]*$");
            if (!ps.matcher(source).matches()) {
                Toast.makeText(MainActivity.this,"M,F만 입력하세요.", Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        }
    };

}
