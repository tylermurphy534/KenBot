package net.tylermurphy.commands.moderation;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

import java.util.stream.Collectors;

import me.duncte123.botcommons.messaging.EmbedUtils;

public class Unban implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		
		if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
			channel.sendMessage(":x: You dont have permission to use this command.").queue();
			return;
		}
			
		if (!event.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
			channel.sendMessage(":x: I can't unban that user or I don't have permission to unban members.").queue();
			return;
		}
		
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		String argsJoined = String.join(" ", args);
		
		event.getGuild().retrieveBanList().queue(bans -> {
			 
			List<User> goodUsers = bans.stream().filter(ban -> isCorrectUser(ban, argsJoined))
					.map(Guild.Ban::getUser).collect(Collectors.toList());
			
			if(goodUsers.isEmpty()) {
				channel.sendMessage(":x: This user is not banned.").queue();
				return;
			}
			
			User target = goodUsers.get(0);
			
			String mod = String.format("%#s", event.getAuthor());
			String bannedUser = String.format("%#s", target);
			
			event.getGuild().unban(target).reason("Unbanned by " + mod).queue();
			channel.sendMessage(String.format("%s was unbanned by %s.",bannedUser,mod)).queue();

			EmbedBuilder builder = new EmbedBuilder()
					.setTitle("Infraction Notice")
					.setColor(Color.yellow)
					.setDescription(String.format("%s unbanned you in %s.",event.getAuthor(),event.getGuild()));
			
			target.openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(builder.build()).queue();
			});
			
		});
		
	}

	public String getInvoke() {
		return "unban";
	}
	
	private boolean isCorrectUser(Guild.Ban ban, String arg) {
		User bannedUser = ban.getUser();
		
		return bannedUser.getName().equalsIgnoreCase(arg) || bannedUser.getId().equals(arg) || String.format("%#s", bannedUser).equalsIgnoreCase(arg);
	}
	
	public String getUsage() {
		return "Unban <@User>";
	}
	
	public String getDescription() {
		return "Unban a user";
	}
	
	public Permission requiredPermission() {
		return Permission.BAN_MEMBERS;
	}
	
}
