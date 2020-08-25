package net.tylermurphy.commands.fun;

import java.util.HashMap;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Precent implements ICommand {

	private HashMap<Member,HashMap<String,Integer>> percents = new HashMap<Member,HashMap<String,Integer>>();
	
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		
		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();

		if(args.size() < 2) {
			channel.sendMessage(":x: Missing Arguments").queue();
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
	
}
