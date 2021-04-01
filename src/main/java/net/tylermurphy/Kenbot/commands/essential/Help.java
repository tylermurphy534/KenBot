package net.tylermurphy.Kenbot.commands.essential;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.Config;
import net.tylermurphy.Kenbot.commands.ICommand;

public class Help implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		Message message = event.getMessage();
		User user = message.getAuthor();
		user.openPrivateChannel().queue((channel) -> {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
			embed.appendDescription(
					"This is all the commands that are included with "+Config.BOT_NAME+"\n"+
					"If you want to know what a command does, run `"+Config.DEFAULT_PREFIX+" description {command}`\n"+
					"Or you need help with a command, run `"+Config.DEFAULT_PREFIX+" usage {command}`\n"+
					"Any questions? Join our [discord support server](https://discord.gg/"+Config.SUPPORT_SERVER+")"
					);
			embed.addField(
					":white_check_mark: Essential Commands",
					"`help`,`description`,`usage`",
					false);
			embed.addField(
					":ok_hand: Fun Commands",
					"`joke`,`kill`,`meme`,`phone`,`ping`,`percent`,`whoAreYou`, `eject`",
					false);
			embed.addField(
					":heartpulse: Social Commands",
					"`boop`,`cookie`,`gift`,`highFive`,`hug`,`kiss`,`lick`,`pet`,`punch`,`slap`,`tickle`,`dance`,`cry`,`pout`, `stats`",
					false);
			embed.addField(
					":ring: Relationship Commands",
					"`status`,`propose`,`acceptProposal`,`rejectProposal`,`divorce`",
					false);
			embed.addField(
					":game_die: Game Commands",
					"`battle`,`blackjack`,`coinflip`,`dice`,`8ball`,`multiDice`,`nunchi`",
					false);
			embed.addField(
					":musical_note: Music Commands",
					"`join`,`leave`,`loop`,`loopQueue`,`np`,`pause`,`play`,`queue`,`remove`,`resume`,`skip`,`stop`",
					false);
			embed.addField(
					":medal: Rankings / Levels",
					"`xp`,`level`,`top`,`toggleLeveling`",
					false);
			if(Config.TWITCH_ENABLED) embed.addField(
					":signal_strength: Twitch Intergration",
					"`broadcastWhenLive`,`removeTwitchBroadcast`,`setTwitchChannel`,`setTwitchRole`",
					false);
			embed.addField(
					":robot: Webhook Commands (Send messages with a diffrent name and pfp)",
					"`createWebhook`,`deleteWebhook`,`setWebhookName`,`setWebhookAvatar`",
					false);
			embed.addField(
					":wrench: Admin Commands",
					"`delSelfRole`,`selfRole`,`logChannel`,`purge`,`serverStats`,`toggleWelcomeMessages`",
					false);
			embed.addField(
					":hammer: Moderation Commands",
					"`ban`,`kick`,`mute`,`setWarnAction`,`softBan`,`tempMute`,`unban`,`unmute`,`warn`,`warns`,`delwarn`,`clearWarns`",
					false);
			embed.addField(
					":tools: Auto Moderation Commands",
					"`allcaps`,`duplicatetext`,`emojispam`,`mentionsspam`,`messagespam`",
					false);
			if(Config.NSFW_ENABLED) embed.addField(
					":octagonal_sign: 18+ Commands",
					"`nhentai`,`e621`,`hentai`,`rule34`",
					false);
			embed.setAuthor(Config.BOT_NAME+" Discord Bot Commmand List", null, event.getAuthor().getAvatarUrl());
			
			channel.sendMessage(embed.build()).queue();
		});
	}
	
	public String getInvoke() { return "help"; }
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
