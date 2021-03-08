package net.tylermurphy.managers;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Config;
import net.tylermurphy.database.Database;
import net.tylermurphy.image.ImageFactory;

public class WelcomeManager extends ListenerAdapter {

	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		String value = Database.GuildSettings.get(event.getGuild().getIdLong(), "WelcomeMessage");
		if(value != null && value.equals("false")) return;
		
		TextChannel channel = event.getGuild().getSystemChannel();
		if(channel == null) return;
		byte[] img = ImageFactory.WelcomeImage(event.getMember().getUser(), event.getGuild());
		 
		try {
			channel
				.sendMessageFormat(":tada:**| Weclome %s#%s** to %s!", event.getMember().getEffectiveName(), event.getMember().getUser().getDiscriminator(), event.getGuild().getName())
				.addFile(img, "welcome.png")
				.queue();
		} catch(Exception ignored) {}
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
	
		if(Config.DEBUG == true && event.getAuthor().getIdLong() == Config.OWNER && event.getMessage().getContentRaw().equalsIgnoreCase(Config.PREFIX+"welcometest")) {
			TextChannel channel = event.getChannel();
			byte[] img = ImageFactory.WelcomeImage(event.getMember().getUser(), event.getGuild());
			
			channel
				.sendMessageFormat(":tada:**| Weclome %s#%s** to %s!", event.getMember().getEffectiveName(), event.getMember().getUser().getDiscriminator(), event.getGuild().getName())
				.addFile(img, "welcome.png")
				.queue();
		}
		
	}
	
}
