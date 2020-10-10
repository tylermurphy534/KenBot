package net.tylermurphy;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.vdurmont.emoji.EmojiParser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.moderation.TempMute;
import net.tylermurphy.commands.moderation.Warn;
import net.tylermurphy.database.DatabaseManager;

public class AutoModManager {
	
	@SuppressWarnings("serial")
	HashMap<String,String> names = new HashMap<String,String>(){{
		put("autoModCaps","Caps Spamming");
		put("autoModMentions","Mass Mentions");
		put("autoModSpam","Spamming Messages");
		put("autoModDuplicate","Duplicate Text");
		put("autoModEmoji","Emoji Spam");
	}};
	
	HashMap<Long,Integer> spammedTimes = new HashMap<Long,Integer>();
	HashMap<Long,Long> lastTimes = new HashMap<Long,Long>();
	HashMap<Long,String> lastMessage = new HashMap<Long,String>();
	
	@SuppressWarnings("serial")
	public void handleMessage(GuildMessageReceivedEvent event) {
		String content = event.getMessage().getContentRaw();
		long guildId = event.getGuild().getIdLong();
		HashMap<String,String> data = new HashMap<String,String>(){{
			put("autoModCaps", DatabaseManager.GuildSettings.get(guildId, "autoModCaps"));
			put("autoModMentions", DatabaseManager.GuildSettings.get(guildId, "autoModMentions"));
			put("autoModSpam", DatabaseManager.GuildSettings.get(guildId, "autoModSpam"));
			put("autoModDuplicate", DatabaseManager.GuildSettings.get(guildId, "autoModDuplicate"));
			put("autoModEmoji", DatabaseManager.GuildSettings.get(guildId, "autoModEmoji"));
		}};
		if(event.getAuthor().isBot() || event.getMember().hasPermission(Permission.ADMINISTRATOR) || event.getMember().hasPermission(Permission.MANAGE_SERVER) || event.getMember().hasPermission(Permission.MESSAGE_MANAGE))
			return;
		if(content.toUpperCase().equals(content) && content.length() > 8) {
			handleAction(event, "autoModCaps", data);
		}
		if(event.getMessage().getMentionedRoles().size() + event.getMessage().getMentionedUsers().size() >= 5) {
			handleAction(event, "autoModMentions", data);
		}
		if(lastTimes.containsKey(event.getAuthor().getIdLong())) {
			int times;
			if(spammedTimes.containsKey(event.getAuthor().getIdLong())) 
				times = spammedTimes.get(event.getAuthor().getIdLong());
			else 
				times = 0;
			if(System.nanoTime() - lastTimes.get(event.getAuthor().getIdLong()) < 1000000000L/2L) 
				times++;
			else 
				times = 0;
			if(times > 4) 
				handleAction(event, "autoModSpam", data);
			spammedTimes.put(event.getAuthor().getIdLong(), 0);
		}
		if(lastMessage.containsKey(event.getAuthor().getIdLong())) {
			if(lastMessage.get(event.getAuthor().getIdLong()).equals(content) && content.length() > 10 && System.nanoTime() - lastTimes.get(event.getAuthor().getIdLong()) < 1000000000L/2L) {
				handleAction(event, "autoModDuplicate", data);
			}
		}
		if(EmojiParser.extractEmojis(content).size() >= 10) {
			handleAction(event, "autoModEmoji", data);
		}
		lastMessage.put(event.getAuthor().getIdLong(), content);
		lastTimes.put(event.getAuthor().getIdLong(), System.nanoTime());
		return;
	}
	
	private void handleAction(GuildMessageReceivedEvent event, String column, HashMap<String,String> data) {
		if(data.get(column) == null || data.get(column).equals("false")) {
			return;
		} else if(data.get(column).equalsIgnoreCase("warn")) {
			int warns = Integer.parseInt(DatabaseManager.UserSettings.get(event.getMember().getUser().getIdLong(), event.getGuild().getIdLong(), "Warns"));
			warn(event,column,warns+1);
			Warn.HandleWarn(event, event.getMember(), DatabaseManager.WarnActions.get(event.getGuild().getIdLong(), warns), names.get(column));
		} else if(data.get(column).equalsIgnoreCase("warnanddelete")) {
			int warns = Integer.parseInt(DatabaseManager.UserSettings.get(event.getMember().getUser().getIdLong(), event.getGuild().getIdLong(), "Warns"));
			warn(event,column,warns+1);
			Warn.HandleWarn(event, event.getMember(), DatabaseManager.WarnActions.get(event.getGuild().getIdLong(), warns), names.get(column));
			event.getMessage().delete().queue();
		} else if(data.get(column).equalsIgnoreCase("delete")) {
			event.getMessage().delete().queue();
		} else if(data.get(column).equalsIgnoreCase("mute")) {
			event.getMessage().delete().queue();
			Role mutedRole;
			List<Role> roles = event.getGuild().getRolesByName("muted", true);
			try{ mutedRole = roles.get(0); }catch(Exception e) { return; }
			new TempMute().tempmute(event.getMember(),30,TimeUnit.MINUTES,mutedRole);
			event.getChannel().sendMessage(String.format("Ken AutoModeration tempmuted %s for 30m, for reason: `%s`",event.getAuthor(),names.get(column))).queue();
			EmbedBuilder builder = new EmbedBuilder()
					.setTitle("Infraction Notice")
					.setColor(Color.yellow)
					.setDescription(String.format("Ken AutoModeration tempmuted you in %s for 30m, for reason: `%s`",event.getGuild(),names.get(column)));
			event.getAuthor().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(builder.build()).queue();
			});
		} else if(data.get(column).equalsIgnoreCase("kick")) {
			event.getMessage().delete().queue();
			event.getGuild().kick(event.getMember()).queue();
			event.getChannel().sendMessage(String.format("Ken AutoModeration kicked %s, for reason: `%s`",event.getAuthor(),names.get(column))).queue();
			EmbedBuilder builder = new EmbedBuilder()
					.setTitle("Infraction Notice")
					.setColor(Color.yellow)
					.setDescription(String.format("Ken AutoModeration kicked you in %s, for reason: `%s`",event.getGuild().getName(),names.get(column)));
			event.getAuthor().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(builder.build()).queue();
			});
		} else if(data.get(column).equalsIgnoreCase("ban")) {
			event.getMessage().delete().queue();
			event.getGuild().ban(event.getMember(), 0, "Ken AutoModeration: "+names.get(column)).queue();
			event.getChannel().sendMessage(String.format("Ken AutoModeration softbanned %s, for reason: `%s`",event.getAuthor(),names.get(column))).queue();
			EmbedBuilder builder = new EmbedBuilder()
					.setTitle("Infraction Notice")
					.setColor(Color.yellow)
					.setDescription(String.format("Ken AutoModeration softbanned you in %s, for reason: `%s`",event.getGuild().getName(),names.get(column)));
			event.getAuthor().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(builder.build()).queue();
			});
		} 
	}
	
	private void warn(GuildMessageReceivedEvent event, String column, int warns) {
		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Warning")
				.setColor(Color.yellow)
				.setDescription(String.format("%s, you have been warned for %s",event.getAuthor(),names.get(column)));
		event.getChannel().sendMessage(builder.build()).queue();
		DatabaseManager.UserSettings.set(event.getMember().getUser().getIdLong(), event.getGuild().getIdLong(), "Warns", String.valueOf(warns));
	}
	
}