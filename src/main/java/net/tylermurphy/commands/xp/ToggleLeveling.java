package net.tylermurphy.commands.xp;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class ToggleLeveling implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		String value = DatabaseManager.GuildSettings.get(event.getGuild().getIdLong(),"Leveling");
		if(value == null || value.equals("true"))
			value = "false";
		else
			value = "true";
		DatabaseManager.GuildSettings.set(event.getGuild().getIdLong(),"Leveling",value);
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setDescription("Set leveling status on this server to `"+value+"`");
		event.getChannel().sendMessage(builder.build()).queue();
	}

	public String getInvoke() {
		return "toggleleveling";
	}

	public String getUsage() {
		return "";
	}

	public String getDescription() {
		return "Toggles the level system of the bot";
	}

	public Permission requiredPermission() {
		return Permission.ADMINISTRATOR;
	}
	
}
