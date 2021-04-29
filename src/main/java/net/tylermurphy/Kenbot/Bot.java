package net.tylermurphy.Kenbot;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.tylermurphy.Kenbot.commands.CommandRegister;
import net.tylermurphy.Kenbot.commands.EventResponder;
import net.tylermurphy.Kenbot.database.MariaDBConnection;
import net.tylermurphy.Kenbot.managers.LogManager;
import net.tylermurphy.Kenbot.managers.WelcomeManager;
import net.tylermurphy.Kenbot.music.BotLeaveListener;

public class Bot {
	
	public static JDA JDA;
	
	private static Logger LOG = Logger.getLogger(Bot.class.getName());
			
	private Bot() throws LoginException, InterruptedException, SQLException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		
		LOG.info("Loading bot_config.json");
		
		try {
			Config.loadConstants();
		} catch(Exception e) {
			LOG.error("Error parsing bot_config.json");
			e.printStackTrace();
			System.exit(0);
		}
		
		LOG.info("Connecting to database");
		
		try {
			MariaDBConnection.Init();
			MariaDBConnection.getConnection();
		} catch(Exception e) {
			LOG.error(e.getMessage());
			System.exit(0);
		}
		
		EmbedUtils.setEmbedBuilder(
				() -> new EmbedBuilder()
                	.setColor(
                		new Color(
                			Config.ACCENT_COLOR_RED / 255f,
                			Config.ACCENT_COLOR_GREEN / 255f,
                			Config.ACCENT_COLOR_RED / 255f
                	))
        );
		
		LOG.info("Starting JDA");
		
		try {
			JDA = JDABuilder.createDefault(Config.BOT_TOKEN)
				.setChunkingFilter(ChunkingFilter.ALL)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.enableIntents((GatewayIntent.GUILD_MEMBERS))
				.setActivity(Activity.playing("Use "+Config.DEFAULT_PREFIX+"help"))
			    .build()
			   	.awaitReady();
		} catch (Exception e) {
			LOG.error(e.getMessage());
			System.exit(0);
		}
		
		LOG.info("Loading Commands");
		CommandRegister.registerCommands();
		
		LOG.info("Starting Listeners");
		
		JDA.addEventListener(new EventResponder());
		JDA.addEventListener(new LogManager());
		if(Config.YOUTUBE_ENABLED) JDA.addEventListener(new BotLeaveListener());
		if(Config.WELCOME_MESSAGING) JDA.addEventListener(new WelcomeManager());
		
		if(Config.TWITCH_ENABLED) {
			LOG.info("Starting Springboot");
			SpringBoot.init();
		} else {
			LOG.info("Skipping Springboot initilization, Twitch disabled");
		}
		
		LOG.info("Finished Loading Kenbot");
		
		LOG.info("██╗░░██╗███████╗███╗░░██╗██████╗░░█████╗░████████╗");
		LOG.info("██║░██╔╝██╔════╝████╗░██║██╔══██╗██╔══██╗╚══██╔══╝");
		LOG.info("█████═╝░█████╗░░██╔██╗██║██████╦╝██║░░██║░░░██║░░░");
		LOG.info("██╔═██╗░██╔══╝░░██║╚████║██╔══██╗██║░░██║░░░██║░░░");
		LOG.info("██║░╚██╗███████╗██║░╚███║██████╦╝╚█████╔╝░░░██║░░░");
		LOG.info("╚═╝░░╚═╝╚══════╝╚═╝░░╚══╝╚═════╝░░╚════╝░░░░╚═╝░░░");
		
	}
	
	public static void main(String[] args) throws LoginException, InterruptedException, SQLException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		new Bot();
    }
}

@SpringBootApplication
class SpringBoot {
	
	public static void init() {
		SpringApplication.run(SpringBoot.class, new String[0]);
	}
	
}
