package net.tylermurphy.commands.fun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.commands.ICommand;

public class Phone extends ListenerAdapter implements ICommand {
	
	private static List<TextChannel> waitingCalls = new ArrayList<TextChannel>(); 
	private static HashMap<TextChannel,TextChannel> currentCalls = new HashMap<TextChannel,TextChannel>();
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		if(currentCalls.containsKey(channel)) {
			TextChannel callerChannel = currentCalls.get(channel);
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("Caller has hung up");
			EmbedBuilder embed2 = EmbedUtils.getDefaultEmbed()
					.setDescription("You have hung up");
			currentCalls.get(channel).sendMessage(embed.build()).queue();
			channel.sendMessage(embed2.build()).queue();
			currentCalls.remove(channel);
			currentCalls.remove(callerChannel);
			return;
		}
		if(waitingCalls.contains(channel)) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("You have canceled the search for a caller");
			channel.sendMessage(embed.build()).queue();
			waitingCalls.remove(channel);
			return;
		}
		if(waitingCalls.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("Looking for caller...");
			channel.sendMessage(embed.build()).queue();
			waitingCalls.add(channel);
			return;
		}
		TextChannel callerChannel = waitingCalls.remove(0);
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setDescription("Found Caller... Connecting...");
		channel.sendMessage(embed.build()).queue();
		callerChannel.sendMessage(embed.build()).queue();
		currentCalls.put(channel, callerChannel);
		currentCalls.put(callerChannel, channel);
		EmbedBuilder embed2 = EmbedUtils.getDefaultEmbed()
				.setDescription("Connected!");
		channel.sendMessage(embed2.build()).queue();
		callerChannel.sendMessage(embed2.build()).queue();
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		TextChannel channel = event.getChannel();
		TextChannel callerChannel = currentCalls.get(channel);
		if(callerChannel == null) return;
		callerChannel.sendMessage(String.format("`%s#%s:` %s", event.getAuthor().getName(), event.getAuthor().getDiscriminator(), event.getMessage().getContentRaw())).queue();
	}

	public String getInvoke() {
		return "phone";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "When two channels in any servers use the phone command, a text chat will open between them allowing them to talk to eachother. Run the command again to hang up.";
	}

	public Permission requiredPermission() {
		return null;
	}
	
}
