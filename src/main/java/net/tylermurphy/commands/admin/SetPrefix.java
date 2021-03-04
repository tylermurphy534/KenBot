package net.tylermurphy.commands.admin;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Config;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class SetPrefix implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		final TextChannel channel = event.getChannel();
        final Member member = event.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage(":x: You must have the Manage Server permission to use this command.").queue();
            return;
        }

        if (args.isEmpty()) {
        	Database.GuildSettings.set(event.getGuild().getIdLong(), "prefix", Config.PREFIX);
        	channel.sendMessage("Reset to default Prefix").queue();
            return;
        }

        final String newPrefix = String.join("", args);
        Database.GuildSettings.set(event.getGuild().getIdLong(), "prefix", newPrefix);
        channel.sendMessageFormat("New prefix has been set to `%s`", newPrefix).queue();
    }

	public String getInvoke() {
		return "setprefix";
	}
	
	public String getUsage() {
		return "SetPrefix <empty to reset, prefix>";
	}
	
	public String getDescription() {
		return "Set Bot Prefix";
	}
	
	public Permission requiredPermission() {
		return Permission.ADMINISTRATOR;
	}

}