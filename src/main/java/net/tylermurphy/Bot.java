package net.tylermurphy;

import java.awt.Color;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.tylermurphy.commands.CommandRegister;
import net.tylermurphy.commands.CommandResponder;
import net.tylermurphy.commands.admin.SelfRole;
import net.tylermurphy.commands.admin.ServerStats;
import net.tylermurphy.commands.fun.Phone;
import net.tylermurphy.commands.games.Battle;
import net.tylermurphy.commands.games.BlackJack;
import net.tylermurphy.commands.games.Nunchi;
import net.tylermurphy.commands.nsfw.NHentai;
import net.tylermurphy.commands.webhooks.SetWebhookAvatar;
import net.tylermurphy.database.MariaDBConnection;
import net.tylermurphy.managers.LogManager;
import net.tylermurphy.managers.WelcomeManager;
import net.tylermurphy.music.BotLeaveListener;

public class Bot {

	private Bot() throws LoginException, InterruptedException, SQLException, SecurityException {
        
		Config.loadConstants();
		MariaDBConnection.getConnection();
		CommandRegister.registerCommands();
		
		EmbedUtils.setEmbedBuilder(
				() -> new EmbedBuilder()
                	.setColor(Color.MAGENTA)
        );
			
		JDABuilder.createDefault(Config.TOKEN)
			.setChunkingFilter(ChunkingFilter.ALL)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.enableIntents((GatewayIntent.GUILD_MEMBERS))
			.setActivity(Activity.playing("Use "+Config.PREFIX+"help"))
		    .addEventListeners(
		    		new CommandResponder(), 
		    		new BlackJack(), 
		    		new Nunchi(), 
		    		new Phone(), 
		    		new Battle(), 
		    		new SelfRole(), 
		    		new LogManager(), 
		    		new ServerStats(), 
		    		new BotLeaveListener(), 
		    		new NHentai(),
		    		new SetWebhookAvatar(),
		    		new WelcomeManager())
		    .build()
		   	.awaitReady();
		
		SpringBoot.init();
		
	}
	
	public static void main(String[] args) throws LoginException, InterruptedException, SQLException, SecurityException {
		new Bot();
    }
}

@SpringBootApplication
class SpringBoot {
	
	public static void init() {
		SpringApplication.run(SpringBoot.class, new String[0]);
	}
	
}
