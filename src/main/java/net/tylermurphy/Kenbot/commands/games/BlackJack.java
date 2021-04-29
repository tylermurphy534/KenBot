package net.tylermurphy.Kenbot.commands.games;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Kenbot.Bot;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.commands.Timeout;

public class BlackJack extends ListenerAdapter implements ICommand {

	private static HashMap<String,BlackJackGame> games = new HashMap<String,BlackJackGame>();
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		String discriminator = event.getAuthor().getName()+event.getAuthor().getDiscriminator();
		System.out.println(discriminator);
		if(games.get(discriminator) != null && !games.get(discriminator).timeout.isTimedOut()) {
			channel.sendMessage("You had a previous game in progress. Deleting previous instance and starting a new one.").queue();
			games.remove(discriminator);
		}
		
		BlackJackGame game = new BlackJackGame(discriminator,channel.getIdLong());
		games.put(discriminator, game);
		game.timeout.startTimeout(30, channel, "The blackjack game has timedout.");
		game.hit(channel);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        MessageReaction reaction = event.getReaction();
        String emote;
		try {
			 emote = reaction.getReactionEmote().getAsCodepoints();
		} catch (Exception e) {
			return;
		}
        TextChannel channel = event.getChannel();
        String discriminator = event.getUser().getName()+event.getUser().getDiscriminator();
        BlackJackGame game = games.get(discriminator);
        if(game != null && game.timeout.isTimedOut()) {
        	games.remove(game);
        	return;
        }
        if(game == null || game.channelId != channel.getIdLong() || !game.discriminator.equals(discriminator)) return;
        if(emote.equalsIgnoreCase("U+1F6D1")) {
        	game.end(channel);
        }else if(emote.equalsIgnoreCase("U+27A1")) {
        	game.hit(channel);
        }else if(emote.equalsIgnoreCase("U+274C")) {
        	EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setColor(Color.magenta)
					.setDescription("You Canceled The Game");
			channel.sendMessage(builder.build()).queue();
        	games.remove(discriminator);
        }
        if(game.ended) games.remove(discriminator);
    }

	public String getInvoke() {
		return "blackjack";
	}
	
	private class BlackJackGame {
		
		public Timeout timeout = new Timeout();
		
		private List<Card> playedCards = new ArrayList<Card>();
		private String discriminator;
		public long channelId = 0;
		private int NumberOfAces = 0;
		private int CurrentTotalCardsValue = 0;
		public boolean ended = false;
		
		public BlackJackGame(String discriminator, long channelId) {
			this.discriminator = discriminator;
			this.channelId = channelId;
		}
		
		public void hit(TextChannel channel) {
			Card card = generateCard();
			if(card.value == 1) NumberOfAces++;
			playedCards.add(card);
			String hand = "";
			for(Card hasCard : playedCards) {
				hand = hand + " " + getCardAsString(hasCard);
			}
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setTitle("Hit!")
					.setColor(Color.magenta)
					.appendDescription("You drew a "+getCardAsString(card)+"!\n Your current score is "+getCurrentValue(NumberOfAces,playedCards))
					.appendDescription("\nCurrent Hand:"+hand);
			channel.sendMessage(builder.build()).queue();
			CurrentTotalCardsValue = getCurrentValue(NumberOfAces,playedCards);
			if(CurrentTotalCardsValue <= 21) next(channel);
			else end(channel);
		}
		
		public void next(TextChannel channel) {
			timeout.refreshTimeout();
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setTitle("Hit or Stay?")
					.setColor(Color.magenta)
					.appendDescription(":octagonal_sign: Stay\n")
					.appendDescription(":arrow_right: Hit\n")
					.appendDescription(":x: Cancel Game");
			Message sentMessage = channel.sendMessage(builder.build()).complete();
			sentMessage.addReaction("U+1F6D1").queue();
			sentMessage.addReaction("U+27A1").queue();
			sentMessage.addReaction("U+274C").queue();
		}
		
		private String getCardAsString(Card card) {
			String[] suits = {":spades:",":clubs:",":hearts:",":diamonds:"};
			String[] values = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
			return (values[card.value-1] + "" + suits[card.suit-1]);
		}
		
		private int getCurrentValue(int aces, List<Card> cards) {
			int value = 0;
			for(Card card : cards) {
				if(card.value != 1) {
					value += Math.min(card.value,10);
				}
			}
			for(int i=0; i<aces; i++) {
				if(value <= 10 && aces-i == 1) value += 11;
				else value += 1;
			}
			return value;
		}
		
		private Card generateCard() {
			int value = (int) (Math.random()*13+1);
			int suit = (int) (Math.random()*4+1);
			Card newCard = new Card(value,suit);
			for(Card card : playedCards) {
				if(card.suit == suit && card.value == value) {
					return generateCard();
				}
			}
			return newCard;
		}
		
		public void end(TextChannel channel) {
			timeout.stopTimeout();
			String title, desc;
			List<Card> dealersHand = new ArrayList<Card>();
			int aces = 0;
			while(getCurrentValue(aces,dealersHand) < 17) {
				Card nextCard = generateCard();
				if(nextCard.value == 1)aces++;
				dealersHand.add(nextCard);
			}
			String hand = "";
			for(Card hasCard : dealersHand) {
				hand = hand + " " + getCardAsString(hasCard);
			}
			int dealerEndingScore = getCurrentValue(aces,dealersHand);
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setTitle("Dealers Hand")
					.setColor(Color.magenta)
					.appendDescription("The dealer ended the game with a score of: "+dealerEndingScore)
					.appendDescription("\nThe dealers hand was:"+hand);
			channel.sendMessage(builder.build()).queue();
			if((CurrentTotalCardsValue <= 21 && CurrentTotalCardsValue > dealerEndingScore) || (CurrentTotalCardsValue <= 21 && dealerEndingScore > 21)) {
				desc = "You won the game with a score of "+CurrentTotalCardsValue;
				title = "You win!";
			} else if(CurrentTotalCardsValue > 21) {
				desc = "You lost the game with a score of "+CurrentTotalCardsValue;
				title = "You Bust!";
			} else {
				desc = "You lost with a score of "+CurrentTotalCardsValue+
						"\nThe dealer had a bigger final score of "+dealerEndingScore;
				title = "You Lost";
			}
			EmbedBuilder builder2 = EmbedUtils.getDefaultEmbed()
					.setTitle(title)
					.setColor(Color.magenta)
					.setDescription(desc);
			channel.sendMessage(builder2.build()).queue();
			ended = true;
		}
		
	}
	
	private class Card {
		
		public int value;
		public int suit;
		
		public Card(int value, int suit) {
			this.value = value;
			this.suit = suit;
		}
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Play blackjack";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}