# Sample Telegram Bot with Google Calendar Integration

This project is a sample Telegram bot written in Java that integrates with Google Calendar and uses a MySQL database for data persistence.

How it works
- Administrator workflow:
- Creates appointments directly in Google Calendar.
- Client workflow:
- Registers with the Telegram bot.
- Client data and subscription info are stored in a MySQL database.
- Automatically receives notifications about upcoming appointments.
- Bot features:
- Packaged as a standalone .jar file, ready to run on any bot hosting service.
- Scheduled to restart once per day.
- On restart, the bot scans all new Google Calendar events for the next day and sends reminders to registered clients.

Use cases
- Appointment reminders for small businesses.
- Simple event management through Google Calendar.
- Demonstration of integrating Java applications with Telegram Bot API, Google APIs, and MySQL database.


## Setup
### Server
 - Set server timezone = UK

### Google calendars
Note. All google calendars for working offices must start from prefix: Стрийська or Лемківська. 
If calendar's name does not contain non of these prefixes appointment will be sent to the developer.


## How to

### Setup access to google services
Project [sample](https://developers.google.com/sheets/api/quickstart/java).

Steps:
1. Create [google service account](https://developers.google.com/identity/protocols/oauth2/service-account);
2. Add Keys and download it to project dir;
3. Share e.g. calendar with service account (by setting e-mail).

### Setup bot's hosting
For 'pebblehost' service has used. Steps:
- login to https://panel.pebblehost.com
- setup two-factor authentication
- set language and java version: bot language > java bot
  - click install
  - go to file manager 
  - open pebble-java-config.json
  - set '{"main":null,"java_version":17}'
- upload server.jar via file manager > upload
- go to mysql db
  - import 3 DBs in order: therapists, clients, appointments
- start bot
- go to scheduled tasks
  - create scheduler: 0 7 * * *
  - create task: restart

For hosting 'Sparked' service has used. Steps:
- login to billing.sparkedhost.com
- request a new Discord Bot (basic bot hosting) service (JRE 17+)
- login to https://control.sparkedhost.us
- setup ssh keys for access
- setup SFTP connection
- create a new [mysql DB](https://help.sparkedhost.com/en/article/how-to-create-a-mysql-database-1yf1ruk/)
- upload all DB schemas
- login to phpMyAdmin console and run all queries from /resource/mysql dir starting from dn_schema.sql query (update db name first) to create tables, etc.
- update db.url, db.user, db.pass in default.properties
- start bot service (JRE 17+)
- create a new 'sending appointments' scheduler: execution time (0 10 * * *), type = server state, server state = running
- create a new task for the scheduler: action = send pawer action, time offset = 5, payload = restart.

### Upload bot
Steps:
- [generate](https://stackoverflow.com/questions/1082580/how-to-build-jars-from-intellij-idea-properly) *.jar file
- rename to server.jar
- upload (using filezilla) to sparked host
- run 
