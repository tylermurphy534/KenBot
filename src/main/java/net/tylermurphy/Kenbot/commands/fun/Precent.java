package net.tylermurphy.Kenbot.commands.fun;

import java.util.HashMap;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;

public class Precent implements ICommand {

	private HashMap<Member,HashMap<String,Integer>> percents = new HashMap<Member,HashMap<String,Integer>>();
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();

		if(args.size() < 2) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		if(event.getMessage().getMentionedMembers().size() < 1 || event.getMessage().getMentionedMembers().size() > 1) {
			channel.sendMessage(":x: Please mention one server member").queue();
			return;
		}
		if(args.get(2).startsWith("<@")){
			channel.sendMessage(":x: Invalid first argument").queue();
			return;
		}
		int percent;
		HashMap<String, Integer> userPercents = percents.get(event.getMessage().getMentionedMembers().get(0));
		if(userPercents == null) userPercents = new HashMap<String,Integer>();
		if(userPercents.containsKey(args.get(1)))
			percent = userPercents.get(args.get(1));
		else {
			percent = (int) Math.round(Math.random()*100);
			userPercents.put(args.get(1),percent);
		}
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setDescription(String.format("%s is %s%% %s!", args.get(0),percent,args.get(1)));
		channel.sendMessage(embed.build()).queue();
		percents.put(event.getMessage().getMentionedMembers().get(0), userPercents);
	}
	
	public String getInvoke() { return "percent"; }
	
	public String getUsage() {
		return "Percent <@User> <% Of What>";
	}
	
	public String getDescription() {
		return "Tells you how much % of something a user is";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
