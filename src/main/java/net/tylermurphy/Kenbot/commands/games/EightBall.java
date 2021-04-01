package net.tylermurphy.Kenbot.commands.games;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;

public class EightBall implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			event.getChannel().sendMessage(embed.build()).queue();
			return;
		}
		String url = "https://magic-8ball.com/assets/images/Our_magic_8_ball.png";
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
		String[] answers = {
				"The 8 ball decrees that your question is utterly bad",
				"No, just no, never, not ever, never going to happen, why did you even ask",
				"Maybe, the 8 ball is not sure",
				"Oh yes, totally, that is a big yep",
				"Yes.",
				"Nuh Uh",
				"No you idiot",
				"It is likily",
				"Probably not",
				"Most likily",
				"It is possible",
				"idk, idc",
				"i dont care, your mother doesn't care, god doesn't care",
				"Oh man, thats a dilemma",
				"Yes uwu",
				"No owo",
				"Sure i guess",
				"Nah bruh",
				"uwu maybe if uwu make iwt happen owo",
				"i dont answer to people as smart as :bricks:",
				":face_with_monocle:  :thinking:   :flushed:   :thumbsup:"
		};
		int rand = (int) (Math.random()*answers.length);
		embed.setAuthor("8 Ball", null, url);
		embed.setDescription(String.format(
				"**Q:**\n%s\n**A:**\n%s",
				String.join(" ", args),
				answers[rand]
		));
		event.getChannel().sendMessage(embed.build()).queue();
	}

	
	
	public String getInvoke() {
		return "8ball";
	}
	
	public String getUsage() {
		return "8ball <question>";
	}
	
	public String getDescription() {
		return "Rolls the 8ball to answer your question";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
