/*
*    File Name  : DateTime.java
*    Author     : Joseph Schell
*    Date       : 12/21/2018
*/
package scheduletrackerapp.model;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTime
{
    private static final long FIFTEEN_MINS = MILLISECONDS.convert(15, MINUTES);
    private static final long SEVEN_DAYS = MILLISECONDS.convert(10080, MINUTES);
    private static final long THIRTY_DAYS = MILLISECONDS.convert(43800, MINUTES);
    private static final DateFormat DATE_FORMAT = 
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /*
    *   Finds current time, then checks to see if any appointments are within
    *   15 minutes of the current time
    *
    *   @param: String today
    *           String date
    */
    public static boolean checkFifteenMinutes(String today, 
            String date) throws ParseException
    {
        Date startTime = DATE_FORMAT.parse(date);
        Date currentTime = DATE_FORMAT.parse(today);
        long time = startTime.getTime() - currentTime.getTime();
        
        if(time <= 0)
        {
            return false;
        }

        return time <= FIFTEEN_MINS;
    }

    /*
    *   Finds current time, then checks to see if any appointments are within
    *   7 days of the current time
    *
    *   @param: String today
    *           String date
    */
    public static boolean checkWeek(String today, 
            String date) throws ParseException
    {
        Date startTime = DATE_FORMAT.parse(date);
        Date currentTime = DATE_FORMAT.parse(today);
        long time = startTime.getTime() - currentTime.getTime();
        
        if(time <= 0)
        {
            // Will not display appointments that have already been held
            return false;
        }

        return time <= SEVEN_DAYS;
    }
    
    /*
    *   Finds current time, then checks to see if any appointments are within
    *   30 days of the current time
    *
    *   @param: String today
    *           String date
    */
    public static boolean checkMonth(String today, 
            String date) throws ParseException
    {
        Date startTime = DATE_FORMAT.parse(date);
        Date currentTime = DATE_FORMAT.parse(today);
        long time = startTime.getTime() - currentTime.getTime();
        
        if(time <= 0)
        {
            // Will not display appointments that have already been held
            return false;
        }

        return time <= THIRTY_DAYS;
    }
    
    /*
    *   Sets UTC date
    *
    *   @param: String date
    */
    public static String setDateUTC(String date)
    {
        ZoneId timeZone = ZoneId.systemDefault();
        LocalDateTime dateLocalDateTime = LocalDateTime.parse(date, DATETIME_FORMATTER);
        ZonedDateTime dateZonedDateTime = dateLocalDateTime.atZone(timeZone);
        ZonedDateTime UTCZonedDateTime = dateZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime UTCLocalDateTime = UTCZonedDateTime.toLocalDateTime();
        Timestamp UTCTimestamp = Timestamp.valueOf(UTCLocalDateTime);
        
        return UTCTimestamp.toString();
    }
    
    /*
    *   Sets local date
    *
    *   @param: String date
    */
    public static String setDateLocal(String date) throws ParseException
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        simpleDateFormat.setTimeZone(utcZone);
        Date myDate = simpleDateFormat.parse(date);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        String formattedDate = simpleDateFormat.format(myDate);
        
        return formattedDate;
    }
}
