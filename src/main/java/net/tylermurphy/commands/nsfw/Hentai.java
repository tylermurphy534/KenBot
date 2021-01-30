package net.tylermurphy.commands.nsfw;

import java.util.Arrays;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Config;
import net.tylermurphy.apis.NekosLifeAPI;
import net.tylermurphy.commands.ICommand;

public class Hentai implements ICommand {

	private final List<String> aceptable_tags = Arrays.asList("bj","boobs","cum","kuni","lewd","lewdkemo","pussy","spank","tits","hentaigif");
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		if(!channel.isNSFW() && event.getGuild().getIdLong() != 740721864846082108L) {
			channel.sendMessage(":x: You can only use this in a NSFW channel").queue();
			return;
		}
		if(Config.NSFW == false) {
			channel.sendMessage(":x: NSFW is disabled by the bot host").queue();
			return;
		}
		if(args.isEmpty()) {
			String url = NekosLifeAPI.getUrlFromSearch("hentai");
			EmbedBuilder embed = EmbedUtils.embedImage(url);
			channel.sendMessage(embed.build()).queue();
			return;
		}
		if(aceptable_tags.contains(args.get(0))) {
			String tag = args.get(0).equalsIgnoreCase("gif") ? "Random_hentai_gif" : args.get(0).toLowerCase();
			String url = NekosLifeAPI.getUrlFromSearch(tag);
			EmbedBuilder embed = EmbedUtils.embedImage(url);
			channel.sendMessage(embed.build()).queue();
			return;
		}
		channel.sendMessage(":x: The specified tag is invalid, please use one of the tags listed in the command's usage.").queue();
		return;
	}

	public String getInvoke() {
		return "hentai";
	}
	
	public String getUsage() {
		return "hentai <tag: *none*/bj/boobs/cum/kuni/lewd/lewdkemo/pussy/spank/tits/gif>";
	}
	
	public String getDescription() {
		return "A NSFW Command";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
