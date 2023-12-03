package com.sgms.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public final class MyUtils {

    public static String FXML_PATH = "/com/sgms/app/";
    public static float finalScore(String ScoreReport,String defenseScore, int postponeDay) {
//      String[] splitScore = inputScore.split(",");
//      for (String str : splitScore) {
//          sum += Integer.parseInt(str);
//      }
        if(ScoreReport ==null || Float.parseFloat(ScoreReport) == 0 ){
            return 0;
        }

        if(defenseScore == null || Float.parseFloat(defenseScore) == 0 ){
            return 0;
        }

        float sum = Float.parseFloat(ScoreReport) + Float.parseFloat(defenseScore);


        if (postponeDay <= 0) {
            return sum / 2;
        } else {
            return (float) (sum / 2 - postponeDay*0.1);
        }
    }


    public static HashMap<String, String> genHashMap(ResultSet resultSet, String key, String value) {
        HashMap<String, String> projectMap = new HashMap<>();
        try {
            while (resultSet.next()) {
                String subjectname = resultSet.getString(key);
                String duedate = String.valueOf(resultSet.getObject(value));
                projectMap.put(subjectname, duedate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectMap;
    }

    public static int calculateDaysBetween(Date startDate, Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static boolean isEmpty(String temp) {
        return temp == null || ("").equals(temp);
    }

    public static String toUpperCase(String input) {
        if (input == null) {
            return null;

        }

        char[] charArray = input.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            if (Character.isLowerCase(charArray[i])) {
                charArray[i] = Character.toUpperCase(charArray[i]);
            }
        }

        return new String(charArray);
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
