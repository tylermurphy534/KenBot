package net.tylermurphy.commands.twitch;

import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class SetTwitchRole implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		String status = Database.Twitch.get(event.getGuild().getIdLong(), "Status");
		
		if(status == null || !status.equals("complete")) {
			channel.sendMessage(":x: There is not TwitchAPI broadcast set in this server for you to delete.").queue();
			return;
		}
		
		List<Role> roles = event.getMessage().getMentionedRoles();
		if(roles.isEmpty()) {
			channel.sendMessage(":white_check_mark: Set Twitch Broadcast Role back to global ping.").queue();
			return;
		}
		
		Role role = roles.get(0);
		if(!role.isMentionable()) {
			channel.sendMessage(":x: Role must be mentionable.").queue();
			return;
		}
		
		Map<String,String> map = Database.Twitch.getStack(event.getGuild().getIdLong());
		map.put("RoleId", role.getId());
		Database.Twitch.set(map);
		
		channel.sendMessage(":white_check_mark: Set Twitch Broadcast Role to `"+role.getName()+"`.").queue();
	}

	public String getInvoke() {
		return "SetTwitchRole";
	}

	public String getUsage() {
		return "SetTwitchRole <@Role>";
	}

	public String getDescription() {
		return "Set the twitch broadcast role in the server. Leave the @Role argument blank to set it back to global ping.";
	}

	public Permission requiredPermission() {
		return Permission.MANAGE_SERVER;
	}

}
