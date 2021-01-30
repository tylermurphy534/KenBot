package net.tylermurphy.commands.nsfw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Config;
import net.tylermurphy.apis.nhentai.Comic;
import net.tylermurphy.apis.nhentai.NHentaiAPI;
import net.tylermurphy.commands.ICommand;

public class NHentai extends ListenerAdapter implements ICommand {
	
	private static final Map<String, Comic> comics = new HashMap<String,Comic>();
	
	private static final String CoverImageLink = "https://t.nhentai.net/galleries/%s/cover.%s";
	private static final String PageImageLink = "https://i.nhentai.net/galleries/%s/%d.%s";

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
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		String search = String.join("%20", args);
		Comic c = NHentaiAPI.getComicFromSearch(search);
		if(c == null) {
			if(search.length()==6) {
				try {
					Integer.parseInt(search);
					EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
					embed.setDescription("Invalid post id: `"+String.join(" ", args)+"`");
					channel.sendMessage(embed.build()).queue();
					return;
				} catch (Exception e) {}
			}
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
			embed.setDescription("No search results came up for query `"+String.join(" ", args)+"`");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		if(comics.containsKey(event.getChannel().getId())) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setTitle("Closed Previous Comic")
					.setDescription("There was a previous nHentai comic opened. Only one is allowed opened at a time in a channel so it was closed");
			channel.sendMessage(embed.build()).queue();
		}
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
		embed.setTitle(c.title);
		embed.setDescription(String.format("nHentai ID: `%s`", c.main_id));
		embed.setImage(String.format(CoverImageLink, c.media_id, c.cover_file_type));
		embed.setFooter(String.format("Page %s/%s", c.currentPage, c.pages));
		Message sentMessage = channel.sendMessage(embed.build()).complete();
		sentMessage.addReaction("U+2B05").queue();
		sentMessage.addReaction("U+25B6").queue();
		
		c.message = sentMessage;
		
		comics.put(channel.getId(), c);
	}
	
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if(!comics.containsKey(event.getChannel().getId())) return;
		if(event.getMember().getUser().isBot()) return;
		if(comics.get(event.getChannel().getId()).message.getIdLong() != event.getMessageIdLong()) return;
		
		MessageReaction reaction = event.getReaction();
        String emote;
		try {
			 emote = reaction.getReactionEmote().getAsCodepoints();
		} catch (Exception e) {
			return;
		}
		
		System.out.println(emote);

		Comic c = comics.get(event.getChannel().getId());
		//left
		if(emote.equalsIgnoreCase("U+2B05")) {
			if(c.currentPage > 0) c.currentPage--;
		}
		//right
		else if(emote.equalsIgnoreCase("U+25B6")) {
			if(c.currentPage < c.pages) c.currentPage++;
		}
		else {
			return;
		}
		
		String link = c.currentPage == 0 ?
				String.format(CoverImageLink, c.media_id, c.cover_file_type) :
				String.format(PageImageLink, c.media_id, c.currentPage, c.page_file_type[c.currentPage-1]);
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
		embed.setTitle(c.title);
		embed.setDescription(String.format("nHentai ID: `%s`", c.main_id));
		embed.setImage(link);
		embed.setFooter(String.format("Page %s/%s", c.currentPage, c.pages));
		
		c.message.editMessage(embed.build()).queue();
		
	}

	public String getInvoke() {
		return "nhentai";
	}

	public String getUsage() {
		return "nehntai <search query>";
	}

	public String getDescription() {
		return "Delivers your favotire nHentai doujinshi";
	}

	public Permission requiredPermission() {
		return null;
	}

}
