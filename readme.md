Ken Discord Bot
===

Description
---
A multipurpose discord bot. It can play youtube videos, includes DJ permessions so all users dont have permission to mess with the bot when music is playing. Also, users can play many advanced games such as nunchi, blackjack, and they can also battle each other. The bot also has moderation tools including, kick, ban, tempmute, and others. It has social commands such as kiss, tickle, boop, slap, punch, lick, hug, pat, The bot also allows users to ship each other. Ken also has a leveling XP system for how much each user chats! Much more is also included.

Setup
---

You have to create a bot_config.json file and put the required data in the following format. To fully fill this table you need a discord bot token, a youtube api key from the google devloper console, a tennor api key from the tennor gif website, and a mariadb database specifying the connection and login information.

```
{
	"General": {
		"Bot_Token": "***************************",
		"Owner_User_Id": "1234567890123456",
		"Default_Prefix": "Ken ",
		"Debug": false,
		"Bot_Name": "Ken",
		"Support_Server": "ChFRPDF",
		"Welcome_Messaging": true,
		"Level_Messaging": true,
		"Accent_Color_Red": 250,
		"Accent_Color_Green": 0,
		"Accent_Color_Blue": 250
	},
	"Database": {
		"Host": "localhost",
		"Port": "3306",
		"Username": "root",
		"Password": "password",
		"Database_Name": "database"
	},
	"Youtube": {
		"ENABLED": true,
		"Api_Key": "*************************************"
	},
	"NSFW": {
		"ENABLED": true
	},
	"Tennor": {
		"ENABLED": false,
		"Api_Key": "**********"
	},
	"Twitch": {
		"ENABLED": true,
		"Client_Id": "******************************",
		"Client_Secret": "****************************",
		"Callback_URL": "https://somecallback.botthing.xyz/subscribe"
	}
}
```

# General Configuration
###### Token
This is where you place your discord bot token that you get on the discord devloper portal. Create an application and add a bot to that application. Copy the bots token and add it here. Make sure to never release this token EVER, or anyone can control your discord bot how ever they like.
###### Owner User ID
This is where you would place the the discord user id of the owner of the bot. Who ever is set to owner will be able to use 'Ken shutdown' to turn off the bot from discord.
###### Default Prefix
This is the prefix that is set by default for all servers the bot is in.
###### Debug
Currently has no function, will be added in the futures
###### Bot Name
The name of the bot to be displayed when needed
###### Support Server
The random generated part of discord.gg/******, this is the link to the support server for your bot in the help message.
###### Welcome Messaging
true/false if you want the bot to ever welcome people when they join a server (can be disabled by servers manually)
###### Level Messaging
true/false if you want the bot to ever tell people they leveled up (leveling can be disabled entirely by servers manually)
###### Accent Color
This is the RGB color value the bot uses when sending embeds.

# Database Configurations
###### Host
This is the hostname where the **mariadb** database server is run. Yes you must be running mariadb or anything else that is compatible. 
###### Port
Usually 3306 by default unless you changed it
###### Username / Password
This is the login information for your database
###### Databse Name
This is the name of the Database inside of mariada that kenbot will use

# API Configurations

###### Youtube
You can enable or disable using the youtube api for searching music. API Key from Google Devloper Console is required if you want to use the youtube api. Learn more [here](https://developers.google.com/youtube/v3/getting-started).
###### NSFW
true/false if you ever want the bot to ever be using NSFW commands and their respective APIs that they use.
###### Tennor
You can enable / disable this to disabled the gif command. Tennor Api key required. Learn more [here](https://tenor.com/gifapi/documentation).
###### Twitch
If you want your bot to be able to notify a server when someone is live on twitch, you will need to set this up. You first will need a twitch Clinet Id and Secret from its devloper page. Then for twitch callbacks, kenbot uses Springboot to create a REST API. The bot runs Springboot on its default port of 8080, so you will need to create a domain name that points directly to the server where the bot is running to port 8080. For example, somerandomdomainname.net could point to server_external_ip:8080. Or, you can setup a reverse proxy if that works better for you.

# Deployment

Kenbot uses gradle to build its project. So run the gradle script with the build command. Then in the /build/libs folder, there will be a bot.jar file. To run the bot, you must run that jar file with the bot_config.json file in the same directory. Final note, JAVA 11 or later is required or the program will refuse to run due to it being a later compile version. That is everything, I hope you enjoy!
