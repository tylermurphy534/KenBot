package net.tylermurphy.Kenbot.music;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotLeaveListener extends ListenerAdapter {
	
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		
		PlayerManager manager = PlayerManager.getInstance();
		GuildMusicManager musicManager = manager.getGuildMusicManager(event.getGuild());
		
		if(event.getMember().getIdLong() == event.getGuild().getSelfMember().getIdLong()) {
			musicManager.player.stopTrack();
			musicManager.player.setPaused(false);
			musicManager.boundChannel = null;
			musicManager.scheduler.unLoopQueue();
			musicManager.scheduler.setLooped(false);
			musicManager.scheduler.getQueue().clear();
			return;
		} else if(event.getGuild().getSelfMember().getVoiceState().getChannel() == null) {
			return;
		}
		
		int users = 0;
		for(Member m : event.getChannelLeft().getMembers()) {
			if(!m.getUser().isBot())
				users++;
		}
		if(users == 0 && musicManager.autoLeaveManager.task != null) {
			musicManager.autoLeaveManager.startTimeout(event.getGuild());
		}
		
	}
	
}
