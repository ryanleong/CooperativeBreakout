package com.unimelb.breakout.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * @author Siyuan Zhang
 * 
 * Simple wrapper that converts JSON string into object.
 *
 */
public class JsonUtils {
    private static final Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object jsonObject) {
        return gson.toJson(jsonObject);
    }
}

