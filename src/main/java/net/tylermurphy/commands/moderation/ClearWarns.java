package net.tylermurphy.commands.moderation;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class ClearWarns implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		Member member = event.getMember();
		List<Member> metionedMembers = event.getMessage().getMentionedMembers();
		
		if(metionedMembers.isEmpty()) {
			channel.sendMessage(":x: Missing Arguments").queue();
			return;
		}
		
		if(metionedMembers.size() > 1) {
			channel.sendMessage(":x: Please only mention one user.").queue();
			return;
		}
		
		Member target = metionedMembers.get(0);
		
		if (!member.hasPermission(Permission.KICK_MEMBERS) || !member.canInteract(target)) {
			channel.sendMessage(":x: You must have the kick members permission to use this command.").queue();
			return;
		}
		
		DatabaseManager.UserSettings.set(target.getUser().getIdLong(), event.getGuild().getIdLong(), "Warns", "0");
		channel.sendMessage(String.format("%s cleared %s's warns",event.getAuthor(),target)).queue();
		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Infraction Notice")
				.setColor(Color.yellow)
				.setDescription(String.format("%s cleared your warns in %s",event.getAuthor(),event.getGuild()));
		
		target.getUser().openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(builder.build()).queue();
		});
	}

	public String getInvoke() {
		return "clearwarn";
	}

}
