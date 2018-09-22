package com.example.rathinchopra.assignmentreminder_chopra_islam.model;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-25.
 */

public class LoginInformation {

    //declaring the field variables

    //string for username
    private String username;

    //string for password
    private String password;
    private long dbId;

    //contrsuctor
    public LoginInformation(String username, String password){
        this.username = username;
        this.password = password;
    }

    //getters and setters for the variables
    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public void setDbId(long dbId){
        this.dbId = dbId;
    }

    public long getDbId(){
        return dbId;
    }
}
