package com.infinium.rajukoushik.traveldiaries;

/**
 * Created by rajukoushik on 13/11/16.
 */
public class UserDetailsManager {

    static  String username;
    static String firstname;
    static String lastname;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
