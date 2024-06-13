package com.example.bookrack.utilities;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;

import java.util.Random;


public class AppUtility {

    public static String sanitizeInput(String input){
        if(input == null){
            return input;
        }
        input = input.trim();
        return input;
    }

    public static Integer genterateRandomNumber(int minRange,int maxRange){
        Integer randomNumber = new Random().nextInt(9000)+1000;
        return randomNumber;
    }

    public static String[] extractCommaSeparatedData(String rawData){
        if(rawData != null){
            String[] extractedData = rawData.split(",");
            return extractedData;
        }
        return null;
    }

    public static String convertToJSON(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
