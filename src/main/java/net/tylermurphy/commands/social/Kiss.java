package net.tylermurphy.commands.social;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class Kiss implements ICommand {
	
	private String[] gifs = {
			"https://media.tenor.com/images/503bb007a3c84b569153dcfaaf9df46a/tenor.gif",
			"https://media.tenor.com/images/78095c007974aceb72b91aeb7ee54a71/tenor.gif",
			"https://media.tenor.com/images/ea9a07318bd8400fbfbd658e9f5ecd5d/tenor.gif",
			"https://media.tenor.com/images/f102a57842e7325873dd980327d39b39/tenor.gif",
			"https://media.tenor.com/images/bc5e143ab33084961904240f431ca0b1/tenor.gif",
			"https://media.tenor.com/images/7fd98defeb5fd901afe6ace0dffce96e/tenor.gif",
			"https://media.tenor.com/images/3d56f6ef81e5c01241ff17c364b72529/tenor.gif",
			"https://media.tenor.com/images/f5167c56b1cca2814f9eca99c4f4fab8/tenor.gif",
			"https://media.tenor.com/images/e76e640bbbd4161345f551bb42e6eb13/tenor.gif",
			"https://media.tenor.com/images/02d9cae34993e48ab5bb27763d5ca2fa/tenor.gif",
			"https://media.tenor.com/images/1306732d3351afe642c9a7f6d46f548e/tenor.gif",
			"https://media.tenor.com/images/6f455ef36a0eb011a60fad110a44ce68/tenor.gif",
			"https://media.tenor.com/images/a1f7d43752168b3c1dbdfb925bda8a33/tenor.gif",
			"https://media.tenor.com/images/621ceac89636fc46ecaf81824f9fee0e/tenor.gif",
			"https://media.tenor.com/images/b8d0152fbe9ecc061f9ad7ff74533396/tenor.gif",
			"https://media.tenor.com/images/d0cd64030f383d56e7edc54a484d4b8d/tenor.gif",
			"https://media.tenor.com/images/a390476cc2773898ae75090429fb1d3b/tenor.gif",
			"https://media.tenor.com/images/626cb1e13142bce7f228ab8e30e2519c/tenor.gif",
			"https://media.tenor.com/images/4b56464510f4c9cfbec9eda5a25c3a72/tenor.gif"
	};

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
		if(mentionedMembers.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		int hugs1 = Database.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "kiss");
		int hugs2 = Database.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "kiss");
		hugs1++;
		int hugs = hugs1 + hugs2;
		Database.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "kiss", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s kisses %s!", name1, name2);
		String footer = String.format("Thats %s kisses now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "kiss";
	}
	
	public String getUsage() {
		return "Kiss <@User>";
	}
	
	public String getDescription() {
		return "Kiss someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
