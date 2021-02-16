package net.tylermurphy.commands.admin;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Purge implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		Member member = event.getMember();
		Member selfMember = event.getGuild().getSelfMember();
		
		if(!member.hasPermission(Permission.MESSAGE_MANAGE)) {
			channel.sendMessage(":x: You dont have permission to run this command.").queue();
			return;
		}
		
		if(!selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
			channel.sendMessage(":x: I don't have permission to manage messages.").queue();
			return;
		}
		
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		int amount;
		String arg = args.get(0);
		
		try {
			amount = Integer.parseInt(arg);
		}catch(NumberFormatException e) {
			channel.sendMessage(String.format(":x: %s is not a valid number.",arg)).queue();
			return;
		}
		
		if(amount < 2 || amount > 100) {
			channel.sendMessage(":x: Amount must be at least 2 and at most 100.").queue();
			return;
		}
		
		channel.getIterableHistory()
			.takeAsync(amount)
			.thenApplyAsync((messages) -> {
				List<Message> goodMessages = messages.stream()
						.filter((m) -> !m.getTimeCreated().isAfter(
								OffsetDateTime.now().plus(2, ChronoUnit.WEEKS)
						))
						.collect(Collectors.toList());
				
				channel.purgeMessages(goodMessages);
				
				return goodMessages.size();
			})
		.whenCompleteAsync(
				(count, thr) -> channel.sendMessageFormat("Deleted `%d` messages", count).queue(
					(message) -> message.delete().queueAfter(5, TimeUnit.SECONDS)
				)
		)
		.exceptionally(thr -> {
			String cause = "";
			
			if(thr.getCause() != null) {
				cause = " caused by: " + thr.getCause().getMessage();
			}
			
			channel.sendMessageFormat("error: %s%s", thr.getMessage(), cause).queue();
			
			return 0;
		});
	}

	public String getInvoke() {
		return "purge";
	}
	
	public String getUsage() {
		return "Purge <Amount>";
	}
	
	public String getDescription() {
		return "Purge messages in a text channel";
	}
	
	public Permission requiredPermission() {
		return Permission.MESSAGE_MANAGE;
	}

}
