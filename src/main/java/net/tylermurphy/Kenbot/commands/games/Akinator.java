package net.tylermurphy.Kenbot.commands.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.markozajc.akiwrapper.Akiwrapper;
import com.markozajc.akiwrapper.Akiwrapper.Answer;
import com.markozajc.akiwrapper.AkiwrapperBuilder;
import com.markozajc.akiwrapper.core.entities.Guess;
import com.markozajc.akiwrapper.core.entities.Identifiable;
import com.markozajc.akiwrapper.core.entities.Question;
import com.markozajc.akiwrapper.core.exceptions.ServerNotFoundException;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Kenbot.Bot;
import net.tylermurphy.Kenbot.Config;
import net.tylermurphy.Kenbot.commands.ICommand;

public class Akinator extends ListenerAdapter implements ICommand  {
	
	private final List<String> VALID = Arrays.asList("yes","y","no","n","probably","p","probably not","pn","idk","i","back","b");
	
	private HashMap<String,AkinatorGame> games = new HashMap<String,AkinatorGame>();
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		String id = event.getMember().getId() + event.getChannel().getId();
		AkinatorGame g = games.get(id);
		if(g != null) {
			if(!args.isEmpty() && args.get(0).equalsIgnoreCase("cancel")){
				event.getChannel().sendMessage("Canceled akinator game").queue();
				games.remove(id);
				return;
			}
			event.getChannel().sendMessage(":x: You currently have a akinator game in progress, type "+Config.DEFAULT_PREFIX+" akinator cancel to end the game.").queue();
			return;
		}
		try {
			event.getChannel().sendMessage("Loading akinator, one moment please ...").queue();
			Akiwrapper wrapper = new AkiwrapperBuilder().build();
			AkinatorGame game = new AkinatorGame();
			game.wrapper = wrapper;
			game.NAME = event.getMember().getEffectiveName();
			game.CHANNELID = event.getChannel().getIdLong();
			game.next();
			games.put(id, game);
			
		} catch (ServerNotFoundException e) {
			event.getChannel().sendMessage(":x: An unexpected error occoured, try again later.");
		}
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		String id = event.getMember().getId() + event.getChannel().getId();
		String raw = event.getMessage().getContentRaw();
		AkinatorGame game = games.get(id);
		if(game == null) return;
		if(!VALID.contains(raw)) return;
		Akiwrapper aki = game.wrapper;
		String status = game.STATUS;
		if(status.equals("QUESTION")) {
			if(raw.equalsIgnoreCase("back") || raw.equalsIgnoreCase("b")) {
				game.goBack();
				return;
			} else if(raw.equalsIgnoreCase("yes") || raw.equalsIgnoreCase("y")) {
				aki.answerCurrentQuestion(Answer.YES);
			} else if(raw.equalsIgnoreCase("no") || raw.equalsIgnoreCase("n")) {
				aki.answerCurrentQuestion(Answer.NO);
			} else if(raw.equalsIgnoreCase("probably") || raw.equalsIgnoreCase("p")) {
				aki.answerCurrentQuestion(Answer.PROBABLY);
			} else if(raw.equalsIgnoreCase("probably not") || raw.equalsIgnoreCase("pn")) {
				aki.answerCurrentQuestion(Answer.PROBABLY_NOT);
			} else if(raw.equalsIgnoreCase("idk") || raw.equalsIgnoreCase("i")) {
				aki.answerCurrentQuestion(Answer.DONT_KNOW);
			}
			
			if(aki.getCurrentQuestion() == null) {
				games.remove(id);
				game.akiLost();
				return;
			}
			
			game.next();
		} else if(status.equals("GUESS")) {
			if(raw.equalsIgnoreCase("back") || raw.equalsIgnoreCase("b")) {
				game.goBack();
			} else if(raw.equalsIgnoreCase("yes") || raw.equalsIgnoreCase("y")) {
				games.remove(id);
				game.akiWon();
			} else if(raw.equalsIgnoreCase("no") || raw.equalsIgnoreCase("n")) {
				game.akiWrong();
			}
		} else if(status.equals("CONTINUE")) {
			if(raw.equalsIgnoreCase("back") || raw.equalsIgnoreCase("b")) {
				Object last = game.Sequence.get(game.CURRENT_SEQUENCE_INDEX);
				if(last instanceof Guess ) {
					game.STATUS = "GUESS";
					game.sendGuessEmbed((Guess)last);
				} else {
					game.STATUS = "QUESTION";
					game.sendQuestionEmbed();
				}
			} else if(raw.equalsIgnoreCase("yes") || raw.equalsIgnoreCase("y")) {
				game.next();
			} else if(raw.equalsIgnoreCase("no") || raw.equalsIgnoreCase("n")) {
				event.getChannel().sendMessage("Game Ended, Thanks For Playing!").queue();
			}
		}
	}
	
	public String getInvoke() {
		return "Akinator";
	}

	public String getUsage() {
		return "";
	}

	public String getDescription() {
		return "Plays the Akinator game.";
	}

	public Permission requiredPermission() {
		return null;
	}

}

class AkinatorGame {
	
	Akiwrapper wrapper;
	String STATUS = "QUESTION";
	long CHANNELID;
	int QUESTION_NUMBER = 0;
	ArrayList<Identifiable> Sequence = new ArrayList<Identifiable>();
	ArrayList<String> Guesses = new ArrayList<String>();
	int CURRENT_SEQUENCE_INDEX = -1;
	String NAME;
	
	public Object getPrevious() {
		return Sequence.get(CURRENT_SEQUENCE_INDEX-1);
	}
	
	public Object getCurrent() {
		return Sequence.get(CURRENT_SEQUENCE_INDEX);
	}
	
	public void goBack() {
		try {
			if(CURRENT_SEQUENCE_INDEX < 1) return;
			Object previous = getPrevious();
			if(previous instanceof Guess) {
				STATUS = "GUESS";
				CURRENT_SEQUENCE_INDEX--;
				Sequence.remove(previous);
				Guesses.remove(Guesses.size()-1);
				sendGuessEmbed((Guess)previous);
			} else if(previous instanceof Question) {
				wrapper.undoAnswer();
				STATUS = "QUESTION";
				CURRENT_SEQUENCE_INDEX--;
				QUESTION_NUMBER--;
				Sequence.remove(previous);
				sendQuestionEmbed();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendQuestionEmbed() {
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
		builder.setTitle(NAME + ", Question " + QUESTION_NUMBER);
		builder.appendDescription("**"+wrapper.getCurrentQuestion().getQuestion()+"**");
		builder.appendDescription("\n[yes (**y**) / no (**n**) / idk (**i**) / probably (**p**) / probably not (**pn**)]\n[back (**b**)]");
		TextChannel channel = (TextChannel)Bot.JDA.getGuildChannelById(CHANNELID);
		channel.sendMessage(builder.build()).queue();
	}
	
	public void akiLost() {
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
		builder.setTitle(NAME + " you beat akinator!");
		builder.setDescription("Akinator ran out of question to ask and coudn't\nguess who you were think of!\nThanks for playing!");
		TextChannel channel = (TextChannel)Bot.JDA.getGuildChannelById(CHANNELID);
		channel.sendMessage(builder.build()).queue();
	}
	
	public void akiWon() {
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
		builder.setTitle("Akinator has guessd your character!");
		builder.setDescription("Thanks for playing! Akinator");
		TextChannel channel = (TextChannel)Bot.JDA.getGuildChannelById(CHANNELID);
		channel.sendMessage(builder.build()).queue();
	}
	
	public void akiWrong() {
		STATUS = "CONTINUE";
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
		builder.setTitle("Continue?");
		builder.setDescription("Akinator guessed incorrectly, would you like to continue?");
		builder.appendDescription("\n\n[yes (**y**) / no (**n**)]\n[back (**b**)]");
		TextChannel channel = (TextChannel)Bot.JDA.getGuildChannelById(CHANNELID);
		channel.sendMessage(builder.build()).queue();
	}
	
	public Guess attemptAGuess() {
		Guess bestGuess = null;
		for(Guess guess : wrapper.getGuessesAboveProbability(.85)) {
			if(guess.getProbability() > .85) {
				if(bestGuess == null || bestGuess.getProbability() < guess.getProbability()) {
					if(Guesses.contains(guess.getName()+":"+guess.getDescription())) continue;
					bestGuess = guess;
				}
			}
		}
		if(bestGuess != null) {
			Guesses.add(bestGuess.getName()+":"+bestGuess.getDescription());
			return bestGuess;
		}
		return null;
	}
	
	public void next() {
		CURRENT_SEQUENCE_INDEX++;
		Guess guess = attemptAGuess();
		if(guess == null) {
			STATUS = "QUESTION";
			QUESTION_NUMBER++;
			Question question = wrapper.getCurrentQuestion();
			Sequence.add(question);
			sendQuestionEmbed();
		} else {
			STATUS = "GUESS";
			Sequence.add(guess);
			sendGuessEmbed(guess);
		}
	}
	
	public void sendGuessEmbed(Guess g) {
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
		builder.setTitle("Is this your character?");
		builder.appendDescription("**"+g.getName()+"**\n"+g.getDescription());
		builder.appendDescription("\n\n[yes (**y**) / no (**n**)]\n[back (**b**)]");
		builder.setImage(g.getImage().toExternalForm());
		TextChannel channel = (TextChannel)Bot.JDA.getGuildChannelById(CHANNELID);
		channel.sendMessage(builder.build()).queue();
	}
}
