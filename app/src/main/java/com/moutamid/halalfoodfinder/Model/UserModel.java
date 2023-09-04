package com.moutamid.halalfoodfinder.Model;


import com.fxn.stash.Stash;

public class UserModel {

    public String Name;
    public String Age;
    public String CNIC;

    public UserModel(String name, String age, String CNIC) {
        Name = name;
        Age = age;
        this.CNIC = CNIC;
    }

//    UserModel userModel = new UserModel();
//    Stash.
//    UserModel userNew = (UserModel) Stash.getObject("TAG_DATA_OBJECT", UserModel.class);
}
