package br.com.diovani.debezium.spring.boot.utils;

import lombok.experimental.UtilityClass;
import com.google.gson.Gson;

@UtilityClass
public class JSONUtils {

    public String toJSON(Object object){
        return new Gson().toJson(object);
    }
}
