package net.tylermurphy.commands.moderation;

import java.awt.Color;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class RevokeWarn implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		Member member = event.getMember();
		List<Member> metionedMembers = event.getMessage().getMentionedMembers();
		
		if(metionedMembers.isEmpty() || args.size() < 2) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		if(metionedMembers.size() > 1) {
			channel.sendMessage(":x: Please only mention one user.").queue();
			return;
		}
		
		Member target = metionedMembers.get(0);
		String warnIdString = args.get(1);
		int warnId = 0;
		try {
			warnId = Integer.parseInt(warnIdString);
		} catch(Exception e) {
			channel.sendMessage(":x: Invalid Warn ID, Unable to parse into an integer.").queue();
			return;
		}
		
		if (!member.hasPermission(Permission.MANAGE_ROLES) || !member.canInteract(target)) {
			channel.sendMessage(":x: You must have the manage roles permission to use this command.").queue();
			return;
		}
		
		int warns = 0;
		String warnsString = Database.UserSettings.get(target.getUser().getIdLong(), event.getGuild().getIdLong(), "Warns");
		if(warnsString != null) {
			try {
				warns = Integer.parseInt(warnsString);
			} catch (Exception e) {
				// oop
			}
		}
		warns--;
		Database.UserSettings.set(target.getUser().getIdLong(), event.getGuild().getIdLong(), "Warns", String.valueOf(warns));
		Database.Warnings.revoke(warnId);
		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Infraction Notice")
				.setColor(Color.green)
				.setDescription(String.format("%s revoked your warn in %s",event.getAuthor(),event.getGuild()));
		channel.sendMessageFormat("%s revoked a warn for %s with id: `%s`", event.getAuthor(), warnId).queue();
		target.getUser().openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(builder.build()).queue();
		});
	}

	public String getInvoke() {
		return "delwarn";
	}
	
	public String getUsage() {
		return "DelWarn <@User> <WarnId>";
	}
	
	public String getDescription() {
		return "Remove a warn from a user";
	}
	
	public Permission requiredPermission() {
		return Permission.MANAGE_ROLES;
	}

}
