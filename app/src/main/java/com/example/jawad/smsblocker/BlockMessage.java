package com.example.jawad.smsblocker;

import java.util.Date;

/**
 * Created by jawad on 06/05/2016.
 */
public class BlockMessage {


    private int _id,_block_list_id,_is_new;
    private String _number,_message;
    private Date _created_at;

    // Inner Join
    private int _is_range;
    private String _title_number,_title_range_number;



    public BlockMessage(){}

    public BlockMessage(int id, int block_list_id, String number, String message, int is_new, Date created_at){
        _id = id;
        _block_list_id = block_list_id;
        _number = Helper.formatToOnlyNumber(number);
        _message = message;
        _is_new = is_new;
        _created_at = created_at;
    }

    public BlockMessage(int block_list_id, String number, String message){
        _block_list_id = block_list_id;
        _number = Helper.formatToOnlyNumber(number);
        _message = message;
    }


    // SET , GET _id
    public void set_id(int id){
        _id = id;
    }
    public int get_id(){
        return _id;
    }

    // SET , GET _block_list_id
    public void set_block_list_id(int id){
        _block_list_id = id;
    }
    public int get_block_list_id(){
        return _block_list_id;
    }

    //  SET, GET _number
    public  void set_number(String number){
        _number = number;
    }
    public String get_number(){
        return _number;
    }

//    SET, GET _message
    public  void set_message(String message){
        _message = message;
    }
    public String get_message(){
        return _message;
    }


    // SET , GET _is_new
    public void set_is_new(int is_new){
        _is_new = is_new;
    }
    public int get_is_new(){
        return _is_new;
    }

//    SET,GET _created_at
    public void set_created_at(Date created_at){
        _created_at = created_at;
    }
    public Date get_created_at(){return _created_at;}


    // InnerJoin GET SET
    public void set_is_range(int is_range){_is_range = is_range;}
    public int get_is_range(){return _is_range;}

    public void set_title_number(String title_number){_title_number = title_number;}
    public String get_title_number(){return _title_number;}

    public void set_title_range_number(String title_range_number){_title_range_number = title_range_number;}
    public String get_title_range_number(){return _title_range_number;}


    /**
     * is_new
     * @return boolean
     */
    public boolean is_new(){
        boolean new_msg = false;
        if(_is_new == 1)
            new_msg  = true;

        return new_msg;
    }

}
