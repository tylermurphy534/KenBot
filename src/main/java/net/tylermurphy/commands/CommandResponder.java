package net.tylermurphy.commands;

import java.util.Arrays;

import java.util.List;
import java.util.regex.Pattern;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Config;
import net.tylermurphy.database.Database;
import net.tylermurphy.managers.AutoModManager;
import net.tylermurphy.managers.LevelManager;
import net.tylermurphy.managers.WebhookManager;

public class CommandResponder extends ListenerAdapter {

    private final LevelManager levelManager;
    private final AutoModManager autoModManager;
    private final WebhookManager webhookManger;
	
	public CommandResponder() {
        this.levelManager = new LevelManager();
        this.autoModManager = new AutoModManager();
        this.webhookManger = new WebhookManager();
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
        String rw = event.getMessage().getContentRaw();

        if (rw.equalsIgnoreCase(Config.PREFIX + "shutdown") &&
                event.getAuthor().getIdLong() == Config.OWNER) {
        	event.getJDA().shutdown();
            System.exit(0);
            return;
        }

        String prefix;
        try {
        	prefix = Database.GuildSettings.get(event.getGuild().getIdLong(), "prefix");
        	if(Config.DEBUG) prefix = Config.PREFIX;
        	if(prefix == null) prefix = Config.PREFIX;
        } catch(Exception e) {
        	prefix = Config.PREFIX;
        }
        
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