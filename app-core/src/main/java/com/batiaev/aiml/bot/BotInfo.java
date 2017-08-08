package com.batiaev.aiml.bot;

/**
 * Bot info
 *
 * @author batiaev
 * @since 7/6/15
 */
public interface BotInfo {

    String getFirstname();

    String getLastname();

    String getLanguage();

    String getEmail();

    String getGender();

    String getVersion();

    String getBirthplace();

    String getJob();

    String getSpecies();

    String getBirthday();

    String getBirthdate();

    String getSign();

    String getReligion();

    String getBotmaster();

    String getValue(String param);
}
