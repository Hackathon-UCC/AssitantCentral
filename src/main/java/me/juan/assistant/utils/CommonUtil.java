package me.juan.assistant.utils;

import com.microsoft.bot.schema.teams.TeamsChannelAccount;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static void info(Object o) {
        logger.info(o instanceof String ? (String) o : ToStringBuilder.reflectionToString(o));
    }

    public static void getDate() {
        ZoneId zoneId = ZoneId.of( "America/Montreal" );
      //  ZonedDateTime zdt = ZonedDateTime.ofInstant( instant , zoneId );
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
