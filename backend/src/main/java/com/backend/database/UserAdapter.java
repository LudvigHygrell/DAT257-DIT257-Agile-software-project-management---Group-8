package com.backend.database;

public abstract class UserAdapter extends Adapter {

    public static boolean login(String username, String password) {
        return true;
    }

    public static boolean is_username() {
        return true;
    }

    public static boolean is_email() {
        return true;
    }

    public static boolean register(String username, String email, String password) {
        return true;
    }

    public static void delete_user() {

    }

    public static void change_password(String username, String new_password) {

    }

    
    
}
