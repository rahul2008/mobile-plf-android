package com.philips.platform.securedblibrary;

/**
 * Created by 310238655 on 11/10/2016.
 */

public class Contact {

    //private variables
    int _id;
    String _name;
    String _number;
    String _group_max_number;

    // Empty constructor
    public Contact(){

    }
    // constructor
    public Contact(String name, String _phone_number, String _groupmax_number){
        this._name = name;
        this._number = _phone_number;
        this._group_max_number= _groupmax_number;
    }

    // constructor
    public Contact(int id, String name, String _phone_number){
        this._id = id;
        this._name = name;
        this._number = _phone_number;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting  number
    public String getNumber(){
        return this._number;
    }

    // setting  number
    public void setNumber(String phone_number){
        this._number = phone_number;
    }

    // getting  group max number
    public String getGroupMaxNumber(){
        return this._group_max_number;
    }

    // setting  group max number
    public void setGroupMaxNumberNumber(String group_max_number){
        this._group_max_number = group_max_number;
    }
}