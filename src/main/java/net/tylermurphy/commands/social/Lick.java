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

public class Lick implements ICommand {
	
	private String[] gifs = {
		"https://media.tenor.com/images/5f73f2a7b302a3800b3613095f8a5c40/tenor.gif",
		"https://media.tenor.com/images/5c5828e51733c8ffe1c368f1395a03d0/tenor.gif",
		"https://media.tenor.com/images/c4e7f19ec6bc342c2e7d69caec637783/tenor.gif",
		"https://media.tenor.com/images/6b701503b0e5ea725b0b3fdf6824d390/tenor.gif",
		"https://media.tenor.com/images/62bb855ef9b5b01aacdc9c69e2101afb/tenor.gif",
		"https://media.tenor.com/images/9643577662a9946de17bd8c3fd124c72/tenor.gif",
		"https://media.tenor.com/images/4626d4bbe60ef15212e3181f11d6704a/tenor.gif",
		"https://media.tenor.com/images/21c8ff8307eb88bf8bf8438e4c78382b/tenor.gif",
		"https://media.tenor.com/images/fbd29dc835db996903fbba8a3542ae7f/tenor.gif",
		"https://media.tenor.com/images/b08b6d61bcf16bee7d56408f61855f35/tenor.gif",
		"https://media.tenor.com/images/4b953e5dea381acf6ef8bcad52982f4a/tenor.gif",
		"https://media.tenor.com/images/dbc120cf518319ffe2aedf635ad2df93/tenor.gif",
		"https://media.tenor.com/images/feeef4685f9307b76c78a22ba0a69f48/tenor.gif",
		"https://media.tenor.com/images/1a2d051f28155db0e4cf175d987cdac2/tenor.gif"
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
		int hugs1 = Database.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "lick");
		int hugs2 = Database.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "lick");
		hugs1++;
		int hugs = hugs1 + hugs2;
		Database.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "lick", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s licks %s!", name1, name2);
		String footer = String.format("Thats %s licks now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "lick";
	}
	
	public String getUsage() {
		return "Lick <@User>";
	}
	
	public String getDescription() {
		return "Lick someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
