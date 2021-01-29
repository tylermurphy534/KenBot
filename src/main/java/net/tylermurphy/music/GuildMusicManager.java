package net.tylermurphy.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class GuildMusicManager {

    public final AudioPlayer player;

    public final TrackScheduler scheduler;
    
    public TextChannel boundChannel;
    
    public final AutoLeaveManager autoLeaveManager;
    
    public final Guild guild;
    
    public GuildMusicManager(Guild guild, AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player, guild);
        player.addListener(scheduler);
        this.guild = guild;
        autoLeaveManager = new AutoLeaveManager();
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}
