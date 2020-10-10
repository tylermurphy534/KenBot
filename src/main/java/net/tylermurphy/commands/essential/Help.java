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
				"```help,description,usage```"+
				":ok_hand: Fun Commands:\n"+
				"```joke,kill,meme,phone,ping,percent,trumpQuote,whoAreYou\n```"+
				":heartpulse: **Social Commands**\n"+
				"```boop,cookie,getShip,gift,highFive,hug,kiss,lick,pet,punch,ship,slap,tickle```"+
				":game_die: **Game Commands**\n"+
				"```battle,blackjack,coinflip,dice,eightball,multiDice,nunchi```"+
				":musical_note: **Music Commands**\n"+
				"```join,leave,loop,loopQueue,np,pause,play,queue,remove,resume,skip,stop```"+
				":test_tube: XP or Level Commands\n"+
				"```xp,level,top```"+
				":hammer: **Admin Commands**\n"+
				"```ban,clearWarns,delSelfRole,kick,logChannel,mute,purge,selfRole,serverStats,setPrefix,setPrefixWithSpace,setWarnAction,softBan,tempMute,unban,unmute,warn,warns```"+
				":robot: **Webhooks**\n"+
				"```createWebhook,deleteWebhook,setWebhookName,setWebhookURL```"+
				":tools: **AutoMod**\n"+
				"```allcaps,duplicatetext,emojispam,mentionsspam,messagespam```"+
				":octagonal_sign: **NSFW**\n"+
				"```blowjob,boobs,cum,e621,hentai,hentaiGif,kuni,lewd,lewdKemo,pussy,rule34,spank,tits```";
		user.openPrivateChannel().queue((channel) -> {
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setDescription(help);
			channel.sendMessage(builder.build()).queue();
		});
	}
	
	public String getInvoke() { return "help"; }
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "";
	}
	
}
