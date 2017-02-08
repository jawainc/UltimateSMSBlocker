package com.example.jawad.smsblocker;

import java.util.Date;

/**
 * Created by jawad on 06/05/2016.
 */
public class BlockList {


    private int _id,_is_range;
    private String _title_number,_title_range_number,_number,_range_number;
    private Date _created_at;



    public BlockList(){}

    public BlockList(int id, String number, String range_number, int is_range, Date created_at){
        _id = id;
        _title_number = number;
        _title_range_number = range_number;
        _number = Helper.formatToOnlyNumber(number);
        _range_number = Helper.formatToOnlyNumber(range_number);
        _is_range = is_range;
        _created_at = created_at;
    }

    public BlockList(String number, String range_number, int is_range){
        _title_number = number;
        _title_range_number = range_number;
        _number = Helper.formatToOnlyNumber(number);
        _range_number = Helper.formatToOnlyNumber(range_number);
        _is_range = is_range;
    }
    public BlockList(int id, String number, String range_number, int is_range){
        _id = id;
        _title_number = number;
        _title_range_number = range_number;
        _is_range = is_range;
    }


    // SET , GET _id
    public void set_id(int id){
        _id = id;
    }
    public int get_id(){
        return _id;
    }

    //  SET, GET _title_number
    public  void set_title_number(String number){
        _title_number = number;
    }
    public String get_title_number(){
        return _title_number;
    }

    //    SET, GET _title_range_number
    public  void set_title_range_number(String range_number){
        _title_range_number = range_number;
    }
    public String get_title_range_number(){
        return _title_range_number;
    }

//  SET, GET _number
    public  void set_number(String number){
        _number = Helper.formatToOnlyNumber(number);
    }
    public String get_number(){
        return _number;
    }

//    SET, GET _range_number
    public  void set_range_number(String range_number){
        _range_number = Helper.formatToOnlyNumber(range_number);
    }
    public String get_range_number(){
        return _range_number;
    }

//    SET,GET _is_range
    public void set_is_range(int is_range){
        _is_range = is_range;
    }
    public int get_is_range(){
        return _is_range;
    }

//    SET,GET _created_at
    public void set_created_at(Date created_at){
        _created_at = created_at;
    }
    public Date get_created_at(){return _created_at;}



//    check number is ranged
    public Boolean is_ranged_number(){
        return (_is_range == 1);
    }


}
