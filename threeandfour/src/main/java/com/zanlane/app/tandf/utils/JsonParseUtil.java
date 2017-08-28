package com.zanlane.app.tandf.utils;


import com.google.gson.Gson;

public class JsonParseUtil {


    public static <T> T parseToBean(String string, Class<T> cls) {
        T result = null;
        Gson gson = new Gson();
        result = gson.fromJson(string, cls);
        return result;
    }

}



