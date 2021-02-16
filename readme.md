Ken Discord Bot
===

Description
---
A multipurpose discord bot. It can play youtube videos and bandicamp urls, and it also includes DJ permessions so all users dont have permission to mess with the bot when music is playing. Also, users can play many advanced games such as nunchi, blackjack, and they can also battle each other. The bot also has moderation tools including, kick, ban, tempmute, and others. It also has social commands such as kiss, tickle, boop, slap, punch, lick, hug, pat, The bot also allows users to ship each other. Ken also has a leveling XP system for how much each user chats!

Setup
---

You have to create a bot_config.json file and put the required data in the following format. To fully fill this table you need a discord bot token, a youtube api key from the google devloper console, a tennor api key from the tennor gif website, and a mariadb database specifying the connection and login information.

>{
>	"TOKEN": "",
>	"YOUTUBE_API_KEY": "",
>	"OWNER_USER_ID": 0,
>	"TENNOR_API_KEY": "",
>	"DEFAULT_PREFIX": "Ken ",
>	"DATABASE_HOST": "",
>	"DATABASE_PORT": "",
>	"DATABASE_USER": "",
>	"DATABASE_PASSWORD": "",
>	"DATABASE_NAME": "",
>	"NSFW: false,
>	"DEBUG": false
>}

#### Token
This is where you place your discord bot token that you get on the discord devloper portal. Create an application and add a bot to that application. Copy the bots token and add it here. Make sure to never release this token EVER, or anyone can control your discord bot how ever they like.

#### Youtube Api Key
Ken bot has built in music futures, and with that comes searching for a video on youtube if they only put in a search term. So you will need to get a youtube api key on the google devloper console for APIs.

#### Owner User ID,
This is where you put your discord user id. All this value does, is when you run the shutdown command, it checks the user who sent it, with the owner user id. If there is a match, the bot will turn off. Thats the only use for this feild, so it isnt needed. If you want to not use it, make sure to atleast put a 0 or it will error.

#### Tennor API Key
Ken bot uses the tennor gif api for certin commands. So you will need to get a tennor api key to use with the bot.

#### Default Prefix
This is where you place the default prefix you want to use for your bot. Make sure that if you want a space after the prefix, that you place a space in the json file.

#### Database_XXXXX
Ken bot requires a MySQL or MariaDB database to function. This is where it stores all its information for guild, users, and everything else. So you will need a database for the bot to work. 
For **DATABASE_HOST**, put in the ip or url for where the server is hosted. For example, if its hosted on the same machine, it would be localhost, if its hosted on the same LAN network, it would be that machines local ip, and if its hosted outside the bot location (Not Recomended), its the outbound ip for the server. The ip is that long string of numbers that its formated like xxx.xxx.xxx.xxx. 
For **DATABASE_PORT_**, put in the port the server is listening on. By default, MySQL and MariaDB databased will listen on port 3306, but if you are using a diffrent port, place the correct one here.
For **DATABASE_USER**, put in the username for connecting to the server. You could use root (Not Recommended), or you could use a user that you created on the database.
For **DATABASE_PASSWORD**, put in the password for the user you listed in DATABASE_USER.
For **DATABASE_NAME** put the name for the database you created.

#### NSFW
If you dont want the NSFW features on the bot, disable them here by putting **false**, or if you want them, put **true**.

#### DEBUG
The name debug is a litle misleading, because right now, all it does is it forces the use of the default prefix in all servers. The main reason for this is if you were hosting a clone of the bot for devlopment pourposes. You most likily will not be needing this ever, so leave it at false.


Deployment
---
To build the bot go to the bot source code directory and run ./gradlew build in a lunix or powershell terminal or run gradlew.bat in windows command prompt. You will then have a runnable jar file in the ./build/libs/ directory. Copy that jar to where ever you want and run in a commandline java -jar "nameOfJar.jar". Make sure that the bot_config.json (Must be named that exactly) file you created is in the same directory as the jar file otherwise the bot will not work.

