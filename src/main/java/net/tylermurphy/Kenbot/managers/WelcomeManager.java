package net.tylermurphy.Kenbot.managers;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Kenbot.Bot;
import net.tylermurphy.Kenbot.Config;
import net.tylermurphy.Kenbot.database.Database;
import net.tylermurphy.Kenbot.image.ImageFactory;

public class WelcomeManager extends ListenerAdapter {
	
	public WelcomeManager() {
		Bot.JDA.addEventListener(this);
	}

	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if(!Config.WELCOME_MESSAGING) return;
		String value = Database.GuildSettings.get(event.getGuild().getIdLong(), "WelcomeMessage");
		if(value != null && value.equals("false")) return;
		
		TextChannel channel = event.getGuild().getSystemChannel();
		if(channel == null) return;
		
		if(channel.canTalk(event.getGuild().getSelfMember())) {
			return;
		}
		
		byte[] img = ImageFactory.WelcomeImage(event.getMember().getUser(), event.getGuild());
		 
		try {
			channel
				.sendMessageFormat(":tada:**| Weclome %s#%s** to %s!", event.getMember().getEffectiveName(), event.getMember().getUser().getDiscriminator(), event.getGuild().getName())
				.addFile(img, "welcome.png")
				.queue();
		} catch(Exception ignored) {}
	}
	
}
