package net.tylermurphy.Kenbot.commands.games;

import java.util.ArrayList;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Kenbot.Bot;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.commands.Timeout;

public class Nunchi extends ListenerAdapter implements ICommand {

	private static List<NunchiGame> games = new ArrayList<NunchiGame>();
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		String pointer = event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator();
		for(NunchiGame game : games) {
			if(game.timeout.isTimedOut()) {
				games.remove(game);
				continue;
			}
			if(game.channelId == channel.getIdLong()) {
				channel.sendMessage(":x: There is already a nunchi game in progress in this channel.").queue();
				return;
			}
		}
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setTitle("Nunchi")
				.appendDescription(
						"`How To Play:` \n"+
						"Users have to count up by 1 from the starting\n"+
						"number shown by the bot. If someone makes a\n"+
						"mistake (types an incorrect number, or repeats\n"+
						"the same number) they are out of the game\n"+
						"and a new round starts without them.\n"+
						"Minimum 2 users required.\n\n"
						)
				.appendDescription(
						"`How To Start:` \n"+
						"React with :white_check_mark:   to join\n"+
						"React with :arrow_forward:  to start (Game Mater Only)\n"+
						"React with :x:  to cancel (Game Master Only)\n\n"
						)
				.appendDescription("`Game Master` "+event.getAuthor().getName());
		Message message = channel.sendMessage(embed.build()).complete();
		message.addReaction("U+2705").queue();
		message.addReaction("U+25B6").queue();
		message.addReaction("U+274C").queue();
		NunchiGame game = new NunchiGame(channel.getIdLong(),event.getGuild().getIdLong(),pointer);
		games.add(game);
		game.timeout.startTimeout(30, channel, "The Nunchi game has timed out.");
	}
	
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        MessageReaction reaction = event.getReaction();
        String emote;
		try {
			 emote = reaction.getReactionEmote().getAsCodepoints();
		} catch (Exception e) {
			return;
		}
        TextChannel channel = event.getChannel();
        String pointer = event.getUser().getName() + "#" + event.getUser().getDiscriminator();
        if(event.getMember().getUser().isBot()) return;
        NunchiGame game = null;
        for(NunchiGame temp : games) {
        	if(temp.timeout.isTimedOut()) {
				games.remove(temp);
				continue;
			}
        	if(temp.channelId == channel.getIdLong()) {
        		game = temp;
        		break;
        	}
        }
        if(game == null || !game.status.equals("Joining Phase")) return;
        if(emote.equalsIgnoreCase("U+2705")) {
        	if(game.users.contains(pointer)) return;
        	game.users.add(pointer);
        	channel.sendMessage(pointer + " has joined the game queue.").queue();
        	game.timeout.refreshTimeout();
        } else if(emote.equalsIgnoreCase("U+25B6") && game.gameMaster.equals(pointer) && event.getGuild().getIdLong() == game.guildId) {
        	if(game.users.size() < 2) return;
        	game.status = "Game In Progress";
        	game.next(channel);
        } else if(emote.equalsIgnoreCase("U+274C") && game.gameMaster.equals(pointer) && event.getGuild().getIdLong() == game.guildId) {
        	games.remove(game);
        	game.timeout.stopTimeout();
        	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
        			.setDescription("The Game Was Canceled.");
        	channel.sendMessage(embed.build()).queue();
        }
    }
	
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		MessageReaction reaction = event.getReaction();
        String emote;
		try {
			 emote = reaction.getReactionEmote().getAsCodepoints();
		} catch (Exception e) {
			return;
		}
        TextChannel channel = event.getChannel();
        String pointer = event.getUser().getName() + "#" + event.getUser().getDiscriminator();
        NunchiGame game = null;
        for(NunchiGame temp : games) {
        	if(temp.timeout.isTimedOut()) {
				games.remove(temp);
				continue;
			}
        	if(temp.channelId == channel.getIdLong()) {
        		game = temp;
        		break;
        	}
        }
        if(game == null || !game.status.equals("Joining Phase")) return;
        if(emote.equals("U+2705")) {
        	game.users.remove(pointer);
        	game.timeout.refreshTimeout();
        	channel.sendMessage(pointer + " has left the game queue.").queue();
        }
    }
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		NunchiGame game = null;
		for(NunchiGame temp : games) {
			if(temp.timeout.isTimedOut()) {
				games.remove(temp);
				continue;
			}
        	if(temp.channelId == channel.getIdLong()) {
        		game = temp;
        		break;
        	}
        }
		if(game == null || !game.status.equals("Game In Progress")) return;
		String pointer = event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator();
		if(!game.users.contains(pointer)) return;
		int check = game.check(event.getMessage().getContentRaw());
		if(check == 1) {
			game.timeout.refreshTimeout();
			game.nextNumber++;
		}else if(check == 2) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription(String.format("%s has made a mistake and will be removed from the game!", pointer));
			channel.sendMessage(embed.build()).queue();
			game.users.remove(pointer);
			game.next(channel);
		}
    }

	public String getInvoke() {
		return "nunchi";
	}
	
	private class NunchiGame {
		
		public Timeout timeout = new Timeout();
		
		long channelId;
		long guildId;
		String gameMaster;
		String status;
		List<String> users;
		int round, randomNumber,nextNumber;
		
		public NunchiGame(long channelId, long guildId, String gameMaster) {
			this.channelId = channelId;
			this.guildId = guildId;
			this.gameMaster = gameMaster;
			this.status = "Joining Phase";
			this.users = new ArrayList<String>();
			this.round = 0;
		}
		
		public void next(TextChannel channel) {
			timeout.refreshTimeout();
			if(users.size() == 1) {
				timeout.stopTimeout();
				end(channel);
				return;
			}
			round++;
			randomNumber = (int) (Math.random()*100)+1;
			nextNumber = randomNumber+1;
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setTitle("Round "+this.round)
					.appendDescription(String.format("Random Number For This Round Is: `%s`\n",randomNumber))
					.appendDescription(String.format("Players left: `%s`", users.size()));
			channel.sendMessage(embed.build()).queue();
		}
		
		public int check(String message) {
			try {
				int msg = Integer.parseInt(message);
				return msg == nextNumber ? 1 : 2;
			} catch (Exception e) {
				return 0;
			}
		}
			
		private void end(TextChannel channel) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setTitle("Game Over")
					.setDescription(String.format("%s was the last user standing and is crowned the winner.",users.get(0)));
			channel.sendMessage(embed.build()).queue();
			games.remove(this);
		}
			
		
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Plays a game of nunchi";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
