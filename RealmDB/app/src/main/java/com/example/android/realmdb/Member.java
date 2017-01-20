package com.example.android.realmdb;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jusung on 2017. 1. 17..
 */

public class Member extends RealmObject {

    @PrimaryKey
    private int member_id;

    private String member_name;
    private int member_age;
    private String member_gender;
    private String member_email;
    private String member_regdata;

    private int bind;

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public int getMember_age() {
        return member_age;
    }

    public void setMember_age(int member_age) {
        this.member_age = member_age;
    }

    public String getMember_gender() {
        return member_gender;
    }

    public void setMember_gender(String member_gender) {
        this.member_gender = member_gender;
    }

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    public String getMember_regdata() {
        return member_regdata;
    }

    public void setMember_regdata(String member_regdata) {
        this.member_regdata = member_regdata;
    }
}
