package org.grow;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigFactory;

import java.util.List;

/**
 *
 * Description. Read environment configuration from *.properties file for defined environment.
 *
 * Environment (qa, ephemeral) can be defined from commandline (via sys props). Example: 'mvn -Denv=qa test'
 * If env has not defined, means that we run tests from IDE, default.properties will be used.
 *
 * Note. Please, note that load strategy = MERGE (more info http://owner.aeonbits.org/docs/loading-strategies/)
 *       In this case, for every property all the specified URLs will be queries, and the first resource defining
 *       the property will prevail. More in detail, this is what will happen:
 *       - First, it will try to load the given property from system properteis (maven parameters);
 *         if the given property is found the associated value will be returned.
 *       - Then it will try to load the given property from ~/resources/${env}-env.properties;
 *         if the property is found the value associated will be returned.
 *       - Finally, it will try to load the given property from the ~/resources/default.properties;
 *
 * Note. If any property has missed in all *.property files, NullPointerException will be shown when you try to read it
 *       for the first time.
 */

@LoadPolicy(LoadType.MERGE)
@Sources({"classpath:default.properties"})
public interface EnvConfig extends Config {

   EnvConfig ENV_CONFIG = ConfigFactory.create(EnvConfig.class);

   /* Settings */
   @Key("telegram.username")
   String telegramUsername();

   @Key("telegram.token")
   String telegramToken();

   @Key("admin.lemkivska.chat.id")
   long adminChatIdLemkivska();

   @Key("admin.stryiiska.chat.id")
   long adminChatIdStryiska();

   @Key("admin.debug.chat.id")
   long adminChatIdDebug();


   /* DB */
   @Key("db.url")
   String dbURL();

   @Key("db.user")
   String dbUser();

   @Key("db.pass")
   String dbPass();


   /* Google services */
   @Key("google.credentials")
   String googleCredentials();

   @Key("google.calendar.lemkivska.ids")
   List<String> calendarsIDsLemkivska();

   @Key("google.calendar.stryiska.ids")
   List<String> calendarIDsStryiska();

   @Key("google.calendar.debug.ids")
   List<String> calendarIDsForDebug();

   /* Viber */
   @Key("pipe.url")
   String viberPipeUrl();
}