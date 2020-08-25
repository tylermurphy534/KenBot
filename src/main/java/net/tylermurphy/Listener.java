package net.tylermurphy;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.database.DatabaseManager;

class Listener extends ListenerAdapter {

    private final CommandManager commandManager;
    private final LevelManager levelManager;
    private final AutoModManager autoModManager;
    private final WebhookManager webhookManger;
    Listener() {
        this.commandManager = new CommandManager();
        this.levelManager = new LevelManager();
        this.autoModManager = new AutoModManager();
        this.webhookManger = new WebhookManager();
    }
    
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
    	event.getJDA().getGuildById(727925906244632576L).getSelfMember().modifyNickname("").queue();
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String rw = event.getMessage().getContentRaw();

        if (rw.equalsIgnoreCase(Config.PREFIX + "shutdown") &&
                event.getAuthor().getIdLong() == Config.OWNER) {
            shutdown(event.getJDA());
            return;
        }

        String prefix = Cache.PREFIXES.get(event.getGuild().getIdLong());
        if(prefix == null) {
        	DatabaseManager.GuildSettings.set(event.getGuild().getIdLong(), "prefix", Config.PREFIX);
        	prefix = Config.PREFIX;
        	Cache.PREFIXES.put(event.getGuild().getIdLong(), prefix);
        }

        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && rw.toLowerCase().startsWith(prefix.toLowerCase())) {
            commandManager.handleCommand(event);
        }
        if(!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage()) {
        	levelManager.handleMessage(event);
        	autoModManager.handleMessage(event);
        	webhookManger.handleMessage(event);
        }
    }

    private void shutdown(JDA jda) {
        jda.shutdown();
        System.exit(0);
    }
}