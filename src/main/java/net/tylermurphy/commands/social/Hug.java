package net.tylermurphy.commands.social;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class Hug implements ICommand {

	private String[] gifs = {
		"https://media.tenor.com/images/8f44c083c55620c02f59c6bea378dca4/tenor.gif",
		"https://media.tenor.com/images/bb67bef5f54d0191b7e2d3c1fd6e4bd3/tenor.gif",
		"https://media.tenor.com/images/7265a624272e13d0950518a9654ce976/tenor.gif",
		"https://media.tenor.com/images/6083ba11631dd577bcc271268d010832/tenor.gif",
		"https://media.tenor.com/images/ca88f916b116711c60bb23b8eb608694/tenor.gif",
		"https://media.tenor.com/images/a9bb4d55724484be94d13dd94721a8d9/tenor.gif",
		"https://media.tenor.com/images/d7f6849b07da0532c7dc3aab538d42d4/tenor.gif",
		"https://media.tenor.com/images/aab83bd3725feeaccb9929f8ca964db9/tenor.gif",
		"https://media.tenor.com/images/9fe95432f2d10d7de2e279d5c10b9b51/tenor.gif",
		"https://media.tenor.com/images/778282e02d511fbc061e1439a5105c6f/tenor.gif",
		"https://media.tenor.com/images/ca682cecd6bff521e400f984502f370c/tenor.gif",
		"https://media.tenor.com/images/ec5f44a6f93adfa22e36a5c78ae44cdf/tenor.gif",
		"https://media.tenor.com/images/61ea96bce16c53a913336a3dbc1a6100/tenor.gif",
		"https://media.tenor.com/images/f192f4d5171bef20fdb0e61c60ea7a23/tenor.gif"
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
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "hug");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "hug");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "hug", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s hugs %s!", name1, name2);
		String footer = String.format("Thats %s hugs now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "hug";
	}
	
	public String getUsage() {
		return "Hug <@User>";
	}
	
	public String getDescription() {
		return "Hug someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
