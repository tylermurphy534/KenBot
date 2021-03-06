package net.tylermurphy.Kenbot.commands.games;

import java.util.ArrayList;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.commands.Timeout;

public class Battle extends ListenerAdapter implements ICommand {
	
	private static List<Match> matches = new ArrayList<Match>();
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		List<Member> members = event.getMessage().getMentionedMembers();
		for(Match match : matches) {
			if(match.timeout.isTimedOut()) {
				matches.remove(match);
				continue;
			}
			if(match.user1.user.getIdLong() == event.getAuthor().getIdLong() && match.status.equals("Challanged")) {
				EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
						.setDescription("Canceled Match Request");
				channel.sendMessage(embed.build()).queue();
				matches.remove(match);
				match.timeout.stopTimeout();
				return;
			}
			if(match.channel.getIdLong() == event.getChannel().getIdLong()) {
				channel.sendMessage(":x: There is already a battle in this channel").queue();
				return;
			}
		}
		for(Match match : matches) {
			if(event.getAuthor().getIdLong() == match.user1.user.getIdLong() || event.getAuthor().getIdLong() == match.user2.user.getIdLong()) {
				channel.sendMessage(":x: You are already in a battle in this channel").queue();
				return;
			}
		}
		if(members.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		Member opponent = members.get(0);
		if(event.getAuthor().getIdLong() == opponent.getUser().getIdLong()) {
			channel.sendMessage(":x: You cant battle yourself").queue();
			return;
		}
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setTitle(String.format("%s has challanged %s to a battle.", event.getAuthor().getName(), opponent.getUser().getName()))
				.appendDescription("React with :white_check_mark: to accept\n")
				.appendDescription("React with :x: to deny\n")
				.appendDescription("Run the Battle command to cancel");
		Message message = channel.sendMessage(embed.build()).complete();
		message.addReaction("U+2714").queue();
		message.addReaction("U+274C").queue();
		Match match = new Match();
		match.status = "Challanged";
		match.user1.user = event.getAuthor();
		match.user2.user = opponent.getUser();
		match.channel = channel;
		matches.add(match);
		match.timeout.startTimeout(30, channel, "The battle has timed out!");
		
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		Match match = null;
		for(Match temp : matches) {
			if(temp.timeout.isTimedOut()) {
				matches.remove(match);
				continue;
			}
			if(event.getAuthor().getIdLong() == temp.user1.user.getIdLong() || event.getAuthor().getIdLong() == temp.user2.user.getIdLong()) {
				match = temp;
				break;
			}
		}
		if(match == null) return;
		if(!match.status.equals("Battle")) return;
		if(match.going == 1 && match.user1.user.getIdLong() != event.getAuthor().getIdLong()) return;
		if(match.going == 2 && match.user2.user.getIdLong() != event.getAuthor().getIdLong()) return;
		int input;
		try {
			input = Integer.parseInt(event.getMessage().getContentRaw());
		}catch(Exception e) {return;}
		if(input < 1 || input > 3) return;
		match.action(input);
		if(match.going==1) match.going=2; else match.going=1;
		match.next();
	}
	
	public void onMessageReceived(MessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		Match match = null;
		int p = 0;
		Stats stats = null;
	    for(Match temp : matches) {
	    	if(temp.timeout.isTimedOut()) {
				matches.remove(match);
				continue;
			}
			if(event.getAuthor().getIdLong() == temp.user1.user.getIdLong()) {
				match = temp;
				p = 1;
				stats = temp.user1;
				break;
			}else if(event.getAuthor().getIdLong() == temp.user2.user.getIdLong()) {
				match = temp;
				p = 2;
				stats = temp.user2;
				break;
			}
		}
	    if(match == null) return;
	    int response;
	    try {
	    	response = Integer.parseInt(event.getMessage().getContentRaw());
	    }catch(Exception e) {return;}
	    int max = Math.min(18, stats.point-(4-stats.spot));
	    if(response < 1 || response > max) {return;}
	    if(!match.status.equals("Stats")) return;
	    switch(stats.spot) {
	    	case 1:
	    		stats.point -= response;
	    		stats.health = response;
	    		stats.maxHealth = response;
	    		stats.spot = 2;
	    		max = Math.min(18, stats.point-(4-stats.spot));
	    		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
	    				.appendDescription("Select your defense stat, min 1, max "+max+"\n")
	    				.appendDescription("You have "+stats.point+" skill points left.");
	    		channel.sendMessage(embed.build()).queue();
	    		match.timeout.refreshTimeout();
	    		break;
	    	case 2:
	    		stats.point -= response;
	    		stats.defense = response;
	    		stats.spot = 3;
	    		max = Math.min(18, stats.point-(4-stats.spot));
	    		EmbedBuilder embed2 = EmbedUtils.getDefaultEmbed()
	    				.appendDescription("Select your speed stat, min 1, max "+max+"\n")
	    				.appendDescription("You have "+stats.point+" skill points left.");
	    		channel.sendMessage(embed2.build()).queue();
	    		match.timeout.refreshTimeout();
	    		break;
	    	case 3:
	    		stats.point -= response;
	    		stats.speed = response;
	    		stats.spot = 4;
	    		max = Math.min(18, stats.point-(4-stats.spot));
	    		EmbedBuilder embed3 = EmbedUtils.getDefaultEmbed()
	    				.appendDescription("Select your attack stat, min 1, max "+max+"\n")
	    				.appendDescription("You have "+stats.point+" skill points left.");
	    		channel.sendMessage(embed3.build()).queue();
	    		match.timeout.refreshTimeout();
	    		break;
	    	case 4:
	    		stats.point -= response;
	    		stats.attack = response;
	    		stats.spot = 4;
	    		max = Math.min(18, stats.point-(4-stats.spot));
	    		stats.done = true;
	    		EmbedBuilder embed4 = EmbedUtils.getDefaultEmbed()
	    				.setDescription(stats.user.getName()+" has inputed their stats.");
	    		match.channel.sendMessage(embed4.build()).queue();
	    		match.timeout.refreshTimeout();
	    		switch(p) {
	    			case 1:
	    				match.user1 = stats;
	    				break;
	    			case 2:
	    				match.user2 = stats;
	    				break;
	    		}
	    		if(match.user1.done && match.user2.done) {
	    			match.status = "Battle";
	    			match.init();
	    			match.next();
	    		}
	    		break;
	    	default:
	    		break;
	    }
	}
	
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if(event.getMember().getUser().isBot()) return;
		MessageReaction reaction = event.getReaction();
		String emote;
		try {
			 emote = reaction.getReactionEmote().getAsCodepoints();
		} catch (Exception e) {
			return;
		}
	    TextChannel channel = event.getChannel();
	    Match match = null;
	    for(Match temp : matches) {
	    	if(temp.timeout.isTimedOut()) {
				matches.remove(match);
				continue;
			}
			if(event.getMember().getUser().getIdLong() == temp.user2.user.getIdLong()) {
				match = temp;
				break;
			}
		}
	    if(match == null) return;
	    if(!match.status.equals("Challanged")) return;
	    if(emote.equals("U+2714")) {
	    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
	    			.appendDescription("The opponet has `accepted` the match\n")
	    			.appendDescription("Respond to the messages in your dms to set your stats\n")
	    			.appendDescription("\nHealth stat is how much health you get in the battle.\n")
	    			.appendDescription("Defense stat is the chance (stat/20)% the opponet will miss.\n")
	    			.appendDescription("Speed stat is the highest value will get the first turn.\n")
	    			.appendDescription("Attack stat is that amount of damage you do > 3+stat/4.");
	    	channel.sendMessage(embed.build()).queue();
	    	match.status = "Stats";
	    	final Match finalMatch = match;
	    	match.user1.user.openPrivateChannel().queue(privateChannel -> {
	    		EmbedBuilder health = EmbedUtils.getDefaultEmbed()
	    				.appendDescription("Select your health stat, min 1, max 18\n")
	    				.appendDescription("You have "+finalMatch.user1.point+" skill points left.");
	    		privateChannel.sendMessage(health.build()).queue();
	    	});
	    	match.user2.user.openPrivateChannel().queue(privateChannel -> {
	    		EmbedBuilder health = EmbedUtils.getDefaultEmbed()
	    				.appendDescription("Select your health stat, min 1, max 18\n")
	    				.appendDescription("You have "+finalMatch.user2.point+" skill points left.");
	    		privateChannel.sendMessage(health.build()).queue();
	    	});
	    	match.timeout.refreshTimeout();
	    }else if(emote.equals("U+274C")) {
	    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
	    			.setDescription("The opponet has `denied` the match!");
	    	channel.sendMessage(embed.build()).queue();
	    	matches.remove(match);
	    }
	}

	public String getInvoke() {
		return "battle";
	}
	
	private class Match {
		TextChannel channel;
		Stats user1 = new Stats();
		Stats user2 = new Stats();
		String status;
		int going = 0;
		
		public Timeout timeout = new Timeout();
		
		public void init() {
			if(user1.speed > user2.speed)
				going = 1;
			else 
				going = 2;
		}

		public void next() {
			timeout.refreshTimeout();
			if(user1.health < 1 || user2.health < 1) { end(); return;}
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setTitle("Battle Status")
					.appendDescription(String.format("`%s:` Health: %s\n", user1.user.getName(), user1.health))
					.appendDescription(String.format("`%s:` Health: %s\n", user2.user.getName(), user2.health))
					.appendDescription(String.format("Its %s's turn.\n\n", going == 1 ? user1.user.getName() : user2.user.getName()))
					.appendDescription("Type 1 to attack\n")
					.appendDescription("Type 2 to heal\n")
					.appendDescription("Type 3 to increace attack");
				
			channel.sendMessage(embed.build()).queue();
		}
		
		public int rand(int amount) {
			return (int) Math.round(Math.random()*amount);
		}
		
		public void end() {
			User winningUser = user1.health < 1 ? user2.user : user1.user;
			User loosingUser = user2.health < 1 ? user2.user : user1.user;
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setTitle("The Battle Has Finished")
					.appendDescription(String.format("**%s has defeated %s in battle!**\n",winningUser,loosingUser))
					.appendDescription("`Ending Stats:`\n")
					.appendDescription(String.format("`%s:` Health: %s\n", user1.user.getName(), user1.health))
					.appendDescription(String.format("`%s:` Health: %s\n", user2.user.getName(), user2.health));
			channel.sendMessage(embed.build()).queue();
			timeout.stopTimeout();
			matches.remove(this);
		}
		
		public void action(int choice) {
			switch(choice) {
				case 1:
					if(going==1) {
						int attack = rand(10) + 3+user1.attack/4;
						attack = Math.max(0, attack);
						int successRoll = rand(20);
						if(successRoll >= user2.defense) {
							EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setDescription(String.format("%s attacked and hit, they did %s damage.", user1.user.getName(), attack));
							channel.sendMessage(embed.build()).queue();
							user2.health -= attack;
						}else {
							EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setDescription(String.format("%s attacked but missed.", user1.user.getName()));
							channel.sendMessage(embed.build()).queue();
						}
					}else {
						int attack = rand(10) + 3+user2.attack/4;
						attack = Math.max(0, attack);
						int successRoll = rand(20);
						if(successRoll >= user1.defense) {
							EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setDescription(String.format("%s attacked and hit, they did %s damage.", user2.user.getName(), attack));
							channel.sendMessage(embed.build()).queue();
							user1.health -= attack;
						}else {
							EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setDescription(String.format("%s attacked but missed.", user2.user.getName()));
							channel.sendMessage(embed.build()).queue();
						}
					}
					break;
				case 2:
					if(going==1) {
						user1.health = Math.min(user1.maxHealth,(int)(user1.maxHealth * .2 + user1.health));
						EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setDescription(String.format("%s healed.", user1.user.getName()));
						channel.sendMessage(embed.build()).queue();
					}else {
						user2.health = Math.min(user2.maxHealth,(int)(user2.maxHealth * .2 + user2.health));
						EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setDescription(String.format("%s healed.", user2.user.getName()));
						channel.sendMessage(embed.build()).queue();
					}
					break;
				case 3:
					if(going==1) {
						user1.attack++;
						EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setDescription(String.format("%s increaced their attack.", user1.user.getName()));
						channel.sendMessage(embed.build()).queue();
					}else {
						user2.attack++;
						EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setDescription(String.format("%s increaced their attack.", user2.user.getName()));
						channel.sendMessage(embed.build()).queue();
					}
					break;
				default:
					break;
			}
		}
	}
	
	private class Stats {
		public User user;
		public int maxHealth,health,defense,speed,attack;
		public int point = 40;
		public int spot = 1;
		public boolean done = false;
	}
	
	public String getUsage() {
		return "Battle <@User>";
	}
	
	public String getDescription() {
		return "Battle someone to the death! Run the command again to cancel battle request.";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}