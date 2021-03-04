package net.tylermurphy.commands.moderation;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class Warn implements ICommand {

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
		String reason = String.join(" ", args.subList(1, args.size()));
		
		if (!member.hasPermission(Permission.MANAGE_ROLES) || !member.canInteract(target)) {
			channel.sendMessage(":x: You must have the manage roles permission to use this command.").queue();
			return;
		}
		
		if(reason.length()>30) {
			channel.sendMessage(":x: Reason for a warning may not be more than 30 characters long.").queue();
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
		warns++;
		Database.UserSettings.set(target.getUser().getIdLong(), event.getGuild().getIdLong(), "Warns", String.valueOf(warns));
		Database.Warnings.insert(target.getIdLong(), event.getGuild().getIdLong(), reason);
		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Infraction Notice")
				.setColor(Color.yellow)
				.setDescription(String.format("%s warned you in %s, for reason: `%s`",event.getAuthor(),event.getGuild(),reason));
		channel.sendMessageFormat("%s warned %s, for reason: `%s`", event.getAuthor(), target, reason).queue();
		target.getUser().openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(builder.build()).queue();
		});
		HandleWarn(event, target, Database.WarnActions.get(event.getGuild().getIdLong(), warns), reason);
	}
	
	public static void HandleWarn(GuildMessageReceivedEvent event, Member target, String action, String reason) {
		if(action == null) {
			return;
		} else if(action.equalsIgnoreCase("mute")) {
			Role mutedRole;
			List<Role> roles = event.getGuild().getRolesByName("muted", true);
			try{ mutedRole = roles.get(0); }catch(Exception e) { return; }
			new TempMute().tempmute(event.getMember(),30,TimeUnit.MINUTES,mutedRole);
			event.getChannel().sendMessage(String.format("Ken Warn System tempmuted %s for 30m, for reason: `%s`",event.getAuthor(),reason)).queue();
			EmbedBuilder builder = new EmbedBuilder()
					.setTitle("Infraction Notice")
					.setColor(Color.yellow)
					.setDescription(String.format("Ken Warn System tempmuted you in %s for 30m, for reason: `%s`",event.getGuild(),reason));
			event.getAuthor().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(builder.build()).queue();
			});
		} else if(action.equalsIgnoreCase("kick")) {
			event.getMessage().delete().queue();
			event.getGuild().kick(event.getMember()).queue();
			event.getChannel().sendMessage(String.format("Ken Warn System kicked %s, for reason: `%s`",event.getAuthor(),reason)).queue();
			EmbedBuilder builder = new EmbedBuilder()
					.setTitle("Infraction Notice")
					.setColor(Color.yellow)
					.setDescription(String.format("Ken Warn System kicked you in %s, for reason: `%s`",event.getGuild().getName(),reason));
			event.getAuthor().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(builder.build()).queue();
			});
		} else if(action.equalsIgnoreCase("ban")) {
			event.getMessage().delete().queue();
			event.getGuild().ban(event.getMember(), 0, "Ken Warn System: "+reason).queue();
			event.getChannel().sendMessage(String.format("Ken Warn System softbanned %s, for reason: `%s`",event.getAuthor(),reason)).queue();
			EmbedBuilder builder = new EmbedBuilder()
					.setTitle("Infraction Notice")
					.setColor(Color.yellow)
					.setDescription(String.format("Ken Warn System softbanned you in %s, for reason: `%s`",event.getGuild().getName(),reason));
			event.getAuthor().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(builder.build()).queue();
			});
		} 
	}

	public String getInvoke() {
		return "warn";
	}
	
	public String getUsage() {
		return "Warn <@User> <reason>";
	}
	
	public String getDescription() {
		return "Warn a user";
	}
	
	public Permission requiredPermission() {
		return Permission.MANAGE_ROLES;
	}

}
