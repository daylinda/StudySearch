package com.example.groupprojectapplication;
// @author Davina
public class ConvertTimeDay {

    String[] days = new String[]{"Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"};
    public String convertToTime(String timeFromUser,String dayPosition){
        String sendtime = "";
        String timefromuser = timeFromUser;
        String[] t = timefromuser.split(":",3);
        for (String ta:t
        ) {
            sendtime = sendtime +ta;
        }
        sendtime = dayPosition.toString() +sendtime;
        return sendtime;

    }

    public String getDayAndTime(String combined) {
        combined = Integer.valueOf(combined).toString();
        combined = String.format("%5s", combined).replace(' ', '0');
        System.out.println(combined);
        if(combined==null){
            return "";
        }

        if(combined.isEmpty()){
            return combined;
        }
        Character dayPosition = combined.charAt(0);
        String day = days[Integer.parseInt(dayPosition.toString())];
        if(combined.substring(1).length()<4){
            return combined;
        }
        String time1 = combined.substring(1,3);
        String time2 = combined.substring(3);
        String dayAndTime = "On " + day + " at "+ time1+":"+time2;
        return dayAndTime;
    }

    public String getDay(String combined){
        combined = Integer.valueOf(combined).toString();
        combined = String.format("%5s", combined).replace(' ', '0');
        if(combined.isEmpty()||combined==null){
            return "";
        }
        Character dayPosition = combined.charAt(0);
        String day = days[Integer.parseInt(dayPosition.toString())];
        return day;
    }

    public String getTime(String combined){
        combined = Integer.valueOf(combined).toString();
        combined = String.format("%5s", combined).replace(' ', '0');
        if(combined==null){
            return "";
        }
        if(combined.isEmpty()||combined.substring(1).length()<4){
            return combined;
        }
        String time1 = combined.substring(1,3);
        String time2 = combined.substring(3);
        String time =  time1+":"+time2;
        return time;

    }
}
