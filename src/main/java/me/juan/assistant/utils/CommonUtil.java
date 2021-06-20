package me.juan.assistant.utils;

import com.microsoft.bot.schema.teams.TeamsChannelAccount;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;

public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static void info(Object o) {
        logger.info(o instanceof String ? (String) o : ToStringBuilder.reflectionToString(o));
    }

    public static Date getDate() {
        return new Date(getCurrentTimeMilis());
       /* int day = dt.getDayOfMonth();
        int year = dt.getYear();
        int month = dt.getMonthOfYear();
        int hours = dt.getHourOfDay();
        int minutes = dt.getMinuteOfHour();*/
    }

    public static long getCurrentTimeMilis() {
        return getJodaDate().getMillis();
    }

    public static DateTime getJodaDate() {
        return new DateTime();
    }

    public static Timestamp dateToTimestamp(String date) {
        return Timestamp.from(Instant.parse(date, DateTimeFormat.forPattern("dd/MM/yyyy")).toDate().toInstant());
    }

    public static Timestamp timeToTimestamp(String time) {
        return Timestamp.from(Instant.parse(time, DateTimeFormat.forPattern("HH:mm")).toDate().toInstant());
    }

    public static TeamsChannelAccount getTeamsChannelAccount() {
        TeamsChannelAccount teamsChannelAccount = new TeamsChannelAccount();
        teamsChannelAccount.setName("NAME");
        teamsChannelAccount.setEmail("juan.campino@campusucc.edu.co");
        teamsChannelAccount.setGivenName("givenName");
        teamsChannelAccount.setUserPrincipalName("principalName");
        teamsChannelAccount.setId("teamsID");
        return teamsChannelAccount;
    }

}
