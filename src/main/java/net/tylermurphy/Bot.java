package net.tylermurphy;

import java.awt.Color;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.tylermurphy.commands.fun.Phone;
import net.tylermurphy.commands.games.Battle;
import net.tylermurphy.commands.games.BlackJack;
import net.tylermurphy.commands.games.Nunchi;
import net.tylermurphy.commands.moderation.SelfRole;
import net.tylermurphy.commands.moderation.ServerStats;
import net.tylermurphy.database.MariaDBConnection;

public class Bot {

	private Bot() throws LoginException, InterruptedException, SQLException, SecurityException {
        
		Config.loadConstants();
		MariaDBConnection.getConnection();
		
		EmbedUtils.setEmbedBuilder(
				() -> new EmbedBuilder()
                	.setColor(Color.MAGENTA)
        );
			
		JDABuilder.createDefault(Config.TOKEN)
			.setChunkingFilter(ChunkingFilter.ALL)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.enableIntents((GatewayIntent.GUILD_MEMBERS))
			.setActivity(Activity.playing("Use "+Config.PREFIX+"help"))
		    .addEventListeners(new Listener(), new BlackJack(), new Nunchi(), new Phone(), new Battle(), new SelfRole(), new LogListener(), new ServerStats())
		    .build()
		   	.awaitReady();
		
		
		
		
	}
	
	public static void main(String[] args) throws LoginException, InterruptedException, SQLException, SecurityException {
		new Bot();
    }
}
