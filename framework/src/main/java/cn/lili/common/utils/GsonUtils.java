package cn.lili.common.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    private GsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String toJson(Object object) {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(object);
    }

    public static String toJsonLowerCaseWithUnderscores(Object object) {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
            .fromJson(json, classOfT);
    }
    public static <T> T fromJsonIgnoreSnake(String json, Class<T> classOfT) {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setDateFormat("yyyy-MM-dd HH:mm:ss").create()
            .fromJson(json, classOfT);
    }
    public static <T> T fromJsonOriginFiled(String json, Class<T> classOfT) {
        return new GsonBuilder().create().fromJson(json, classOfT);
    }

}
