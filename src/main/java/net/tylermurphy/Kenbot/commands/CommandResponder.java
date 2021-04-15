package net.tylermurphy.Kenbot.commands;

import java.util.Arrays;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Kenbot.Bot;
import net.tylermurphy.Kenbot.Config;
import net.tylermurphy.Kenbot.managers.AutoModManager;
import net.tylermurphy.Kenbot.managers.LevelManager;
import net.tylermurphy.Kenbot.managers.WebhookManager;

public class CommandResponder extends ListenerAdapter {

    private final LevelManager levelManager;
    private final AutoModManager autoModManager;
    private final WebhookManager webhookManger;
	private final ExecutorService executerService;
	
	public CommandResponder() {
        this.levelManager = new LevelManager();
        this.autoModManager = new AutoModManager();
        this.webhookManger = new WebhookManager();
        Bot.JDA.addEventListener(this);
        executerService = Executors.newCachedThreadPool(); 
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		Thread executerThread = new Thread(() -> {
			handleMessage(event);
		});
		
		executerService.execute(executerThread);
		
    }
	
	private void handleMessage(GuildMessageReceivedEvent event) {
		if(!event.getChannel().canTalk(event.getGuild().getSelfMember())) {
			return;
		}
		
        String rw = event.getMessage().getContentRaw();

        if (rw.equalsIgnoreCase(Config.DEFAULT_PREFIX + "shutdown") &&
                event.getAuthor().getId().equals(Config.OWNER_USER_ID)) {
        	event.getJDA().shutdown();
            System.exit(0);
            return;
        }
        
        String prefix = Config.DEFAULT_PREFIX;
        
        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && rw.toLowerCase().startsWith(prefix.toLowerCase())) {

            final String[] split = event.getMessage().getContentRaw().replaceFirst(
                    "(?i)" + Pattern.quote(prefix), "").split("\\s+");
            final String invoke = split[0].toLowerCase();
            if(CommandRegister.REGISTER.containsKey(invoke)) {
            	
            	ICommand command = CommandRegister.REGISTER.get(invoke);
            	
            	if(event.getMember().hasPermission(command.requiredPermission())) {
            		 
            		final List<String> args = Arrays.asList(split).subList(1, split.length);
            		command.invoke(args, event);
            	
            	} else {
            		
            		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
            				.appendDescription(":x: **Invalid Permissions**\n")
            				.appendDescription("You require the "+command.requiredPermission().toString()+" permission to use this command.");
            		event.getChannel().sendMessage(builder.build()).queue();
            		
            	}
               
            }
        	
        }
        
        if(!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage()) {
        	
        	levelManager.handleMessage(event);
        	autoModManager.handleMessage(event);
        	webhookManger.handleMessage(event);
        	
        }
	}
	
}