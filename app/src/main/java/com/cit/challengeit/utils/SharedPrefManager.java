package com.cit.challengeit.utils;

import com.cit.challengeit.network.User;
import com.orhanobut.hawk.Hawk;


public class SharedPrefManager {

    private static final String USER = "USER";

    public static void setUser(User user) {
        Hawk.put(USER, user);
    }

    public static User getUser() {
        return Hawk.get(USER, new User());
    }
}