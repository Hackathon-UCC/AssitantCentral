package me.juan.assistant.utils;

import com.microsoft.bot.schema.teams.TeamsChannelAccount;
import lombok.NonNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        return new DateTime(DateTimeZone.forID("America/Bogota"));
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
