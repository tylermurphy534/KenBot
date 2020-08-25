package net.tylermurphy.commands.fun;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Kill implements ICommand {

	String[] statements = {
			":knife: You stab %s and take them out",
			":gun: You shoot %s in the chest 20 times",
			":cat2: You attempt to kill %s with a fucking cat, wtf are you doing",
			":saxophone: You blunt %s to death with a saxophone",
			":bomb: You strap a bomb to %s they blow up",
			":syringe: You inject %s with poison",
			":chains: You choke %s to death with chains",
			":axe: You chop %s into peices",
			":snowflake: You freeze %s to death",
			":watermelon: %s turns into a watermelon, they just do, deal with it"
	};

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
		if(mentionedMembers.isEmpty()) {
			channel.sendMessage(":x: Please Mention Someone").queue();
			return;
		}
		if(event.getAuthor().getIdLong() == mentionedMembers.get(0).getIdLong()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("Good job you kill yourself, now what, oh right, nothing, becase your dead");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		String message = String.format(statements[(int) (Math.random()*statements.length)], mentionedMembers.get(0).getUser());
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setDescription(message);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "kill";
	}
	
	
	
}
