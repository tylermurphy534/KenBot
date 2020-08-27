package net.tylermurphy;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.commands.autoModeration.*;
import net.tylermurphy.commands.essential.*;
import net.tylermurphy.commands.fun.*;
import net.tylermurphy.commands.music.*;
import net.tylermurphy.commands.nsfw.*;
import net.tylermurphy.commands.games.*;
import net.tylermurphy.commands.moderation.*;
import net.tylermurphy.commands.social.*;
import net.tylermurphy.commands.webhooks.*;
import net.tylermurphy.commands.xp.*;

public class CommandManager {

	private final Map<String, ICommand> commands = new HashMap<>();
	
	CommandManager(){
		addCommand(new Help());
		addCommand(new Join());
		addCommand(new Precent());
		addCommand(new Ping());
		addCommand(new WhoAreYou());
		addCommand(new Leave());
		addCommand(new Play());
		addCommand(new Stop());
		addCommand(new Queue());
		addCommand(new Skip());
		addCommand(new NowPlaying());
		addCommand(new Pause());
		addCommand(new Resume());
		addCommand(new Kick());
		addCommand(new Ban());
		addCommand(new Unban());
		addCommand(new Purge());
		addCommand(new SetPrefix());
		addCommand(new Loop());
		addCommand(new Mute());
		addCommand(new Unmute());
		addCommand(new TempMute());
		addCommand(new Dice());
		addCommand(new BlackJack());
		addCommand(new Nunchi());
		addCommand(new Coinflip());
		addCommand(new Phone());
		addCommand(new Meme());
		addCommand(new Joke());
		addCommand(new Hug());
		addCommand(new Kiss());
		addCommand(new Gift());
		addCommand(new Boop());
		addCommand(new Cookie());
		addCommand(new Pet());
		addCommand(new Slap());
		addCommand(new Battle());
		addCommand(new Ship());
		addCommand(new Tickle());
		addCommand(new HighFive());
		addCommand(new Lick());
		addCommand(new Kill());
		addCommand(new Punch());
		addCommand(new GetShip());
		addCommand(new XP());
		addCommand(new Level());
		addCommand(new Top());
		addCommand(new SelfRole());
		addCommand(new DelSelfRole());
		addCommand(new SetPrefixWithSpace());
		addCommand(new LogChannel());
		addCommand(new ServerStats());
		addCommand(new Caps());
		addCommand(new Duplicate());
		addCommand(new Mentions());
		addCommand(new Emoji());
		addCommand(new Spam());
		addCommand(new SoftBan());
		addCommand(new MultiDice());
		addCommand(new DeleteWebhook());
		addCommand(new CreateWebhook());
		addCommand(new SetWebhookName());
		addCommand(new SetWebhookURL());
		addCommand(new Blowjob());
		addCommand(new Boobs());
		addCommand(new Cum());
		addCommand(new Hentai());
		addCommand(new HentaiGIF());
		addCommand(new Kuni());
		addCommand(new Lewd());
		addCommand(new LewdKemo());
		addCommand(new Pussy());
		addCommand(new SmallBoobs());
		addCommand(new Spank());
		addCommand(new Tits());
		addCommand(new LoopQueue());
		addCommand(new Remove());
		addCommand(new EightBall());
		addCommand(new TrumpQuote());
		addCommand(new Warn());
		addCommand(new Warns());
		addCommand(new SetWarnAction());
		addCommand(new ClearWarns());
	}
	
	private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke().toLowerCase(), command);
        }
    }
	
	public Collection<ICommand> getCommands() {
		return commands.values();
	}
	
	public ICommand getCommand(String name) {
        return commands.get(name);
    }
	
	void handleCommand(GuildMessageReceivedEvent event) {
        final String prefix = Cache.PREFIXES.get(event.getGuild().getIdLong());

        final String[] split = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(prefix), "").split("\\s+");
        final String invoke = split[0].toLowerCase();
        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            event.getChannel().sendTyping().queue();
            commands.get(invoke).handle(args, event);
        }
    }
	
}
