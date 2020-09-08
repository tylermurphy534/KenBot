package net.tylermurphy.commands.essential;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Help implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		
		Message message = event.getMessage();
		User user = message.getAuthor();
		
		String help = 
				":white_check_mark: **Essential Commands**\n"+
				"```help | sends the help message```\n"+
				":ok_hand: Fun Commands:\n"+
				"```ping | pings the bot\n"+
				"whoareyou | do i realy have to explain this\n"+
				"percent <user> <input> | how much % input the user is\n"+
				"phone | create a text chat between two discord channels that use the phone command\n"+
				"meme | delivers a hot and fresh meme\n"+
				"joke | tells a funny joke\n"+
				"trumpquote | tells a trump quote```\n"+
				":heartpulse: **Social Commands**\n"+
				"```boop <user> | boop someones nose\n"+
				"cookie <user> | bake someone a cookie\n"+
				"gift <user> | give someone a gift\n"+
				"hug <user> | hug someone\n"+
				"kiss <user> | kiss someone\n"+
				"pet <user> | pet someone\n"+
				"slap <user> | slap someone\n"+
				"tickle <user> | tickle someone\n"+
				"lick <user> | lick someone\n"+
				"highfive <user> | high five someone\n"+
				"punch <user> | punch someone\n"+
				"ship <user/remove/empty> | ships you with your love intrest / removes your love intrest / no argumts to show your ship status\n"+
				"getship <user> | get someone elses ship status```\n"+
				":game_die: **Game Commands**\n"+
				"```blackjack | plays blackjack\n"+
				"nunchi | plays nunchi\n"+
				"battle <user> | battle someone / cancel a battle request\n"+
				"dice <sides> | rolls a dice with <sides> sides\n"+
				"multidice <sides> <numDice> | rolls <numDice> dice, each with <sides> sides\n"+
				"coinflip | flips a coin\n"+
				"8ball <question> | asks the 8ball a question```\n"+
				":musical_note: **Music Commands**\n"+
				"```join | connects the bot to your vc\n"+
				"leave | disconnects the bot from your vc\n"+
				"play <url/youtube search> | plays a youtube, soundcloud, or bandcamp url\n"+
				"stop | cleares queue and stops current track\n"+
				"queue | shows queue\n"+
				"skip | skips current track\n"+
				"np | shows now playing\n"+
				"pause | pauses current track\n"+
				"resume | resumes current track\n"+
				"loop | loops current track```\n";
		String help2 = 
				":test_tube: XP or Level Commands\n"+
				"```xp | shows your current xp amount\n"+
				"level | shows your current level\n"+
				"top | shows the top users in your guild```\n"+
				":hammer: **Admin Commands**\n"+
				"```kick <user> <reason> | kicks a user\n"+
				"ban <user> <reason> | bans a user\n"+
				"softban <user> <reason> | bans a user without deleting last 24h messages\n"+
				"unban <user> | unbans a user\n"+
				"mute <user> <reason> | mutes a user\n"+
				"tempmute <user> <time> <reason> | tempmutes a user\n"+
				"unmute <user> | unmutes a user\n"+
				"purge <amount> | deletes a set number of messages in a channel that are not older than 2 weeks \n"+
				"selfrole <channelId> <messageId> <roleId/roleMention/roleName> <emoji> | adds self role emoji toggle to specified message for the specified role\n"+
				"delselfrole <channelId> <messageId> | removes all self roles on specified message\n"+
				"logchannel <channelMention/remove> | sets log channel to mention or current channel if no args, removes log channel if arg passed is remove\n"+
				"serverstats | toggles on and off server stats\n"+
				"warn <user> <reason> | warns a user\n"+
				"warns <user> | shows a users warn amount\n"+
				"clearwarns <user> | clears a users warns\n"+
				"setwarnaction <warns> <action> | sets a <action> (kick,ban,mute) to occur on a user when they get so many <warns>```\n"+
				":robot: **Webhooks**\n"+
				"```createwebhook <prefix> | create a webhook with a chat prefix of <prefix>\n"+
				"deletewebhook <prefix> | deleted the webhook with the prefix <prefix>\n"+
				"setwebhookname <prefix> <name> | set the webhook name to <name> that has the prefix <prefix>\n"+
				"setwebhookURL <prefix> <url> | set the webhook avatar url to <url> that has the prefix <prefix>```\n";
		String help3 =
				":tools: **AutoMod**\n"+
				"```allcaps <settings> | set allcaps automod settings \n"+
				"duplicatetext <settings> | set duplicatetext automod settings \n"+
				"emojispam <settings> | set emojispam automod settings \n"+
				"mentionsspam <settings> | set mentionsspam automod settings \n"+
				"messagespam <settings> | set messagespam automod settings \n"+
				"AutoMod Settings: '' (Blank gets current setting), 'False' (Disable),'Warn','WarnAndDelete','Delete','Mute' (for 30 min),'Kick','Ban'```\n"+
				":octagonal_sign: **NSFW**\n"+
				"```blowjob | posts a nsfw image of blow job\n"+
				"boobs | posts a nsfw image of boobs\n"+
				"cum | posts a nsfw image of cum\n"+
				"hentai | posts a nsfw image of hentai\n"+
				"hentaigif | posts a nsfw image of hentaigif\n"+
				"kuni | posts a nsfw image of kuni\n"+
				"lewd | posts a nsfw image of lewd\n"+
				"lewdkemo | posts a nsfw image of lewd kemo\n"+
				"pussy | posts a nsfw image of pussy\n"+
				"smallboobs | posts a nsfw image of small boobs\n"+
				"spank | posts a nsfw image of spank\n"+
				"tits | posts a nsfw image of tits```\n"+
				"e621 <tags...> | searches e621```\n"+
				"rule34 <tags...> | searches rule34```\n";
		user.openPrivateChannel().queue((channel) -> {
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setDescription(help);
			EmbedBuilder builder2 = EmbedUtils.getDefaultEmbed()
					.setDescription(help2);
			EmbedBuilder builder3 = EmbedUtils.getDefaultEmbed()
					.setDescription(help3);
			channel.sendMessage(builder.build()).queue();
			channel.sendMessage(builder2.build()).queue();
			channel.sendMessage(builder3.build()).queue();
		});
	}
	
	public String getInvoke() { return "help"; }
	
}
