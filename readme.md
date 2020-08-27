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
>	"OWNER_USER_ID": ,
>	"TENNOR_API_KEY": "",
>	"DEFAULT_PREFIX": "Ken ",
>	"DATABASE_HOST": "",
>	"DATABASE_PORT": "",
>	"DATABASE_USER": "",
>	"DATABASE_PASSWORD": "",
>	"DATABASE_NAME": ""
>	"NSFW: false
>}

To build the bot go to the bot source code directory and run ./gradlew build in a lunix or powershell terminal or run gradlew.bat if on windows and without powershell. You will then have a runnable fat jar file in the ./build/libs/ directory. Copy that jar to where ever you want and run in a commandline java -jar "nameOfJar.jar".

