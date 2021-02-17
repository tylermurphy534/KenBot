package net.tylermurphy.managers;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.database.DatabaseManager;
import net.tylermurphy.image.ImageGenerator;

public class WelcomeManager extends ListenerAdapter {

	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		String value = DatabaseManager.GuildSettings.get(event.getGuild().getIdLong(), "WelcomeMessage");
		if(value != null && value.equals("false")) return;
		
		TextChannel channel = event.getGuild().getSystemChannel();
		if(channel == null) return;
		byte[] img = ImageGenerator.WelcomeImage(event.getMember().getUser(), event.getGuild());
		 
		channel
			.sendMessageFormat(":tada:**| Weclome %s#%s** to %s!", event.getMember().getEffectiveName(), event.getMember().getUser().getDiscriminator(), event.getGuild().getName())
			.addFile(img, "welcome.png")
			.queue();
	}
	
}
