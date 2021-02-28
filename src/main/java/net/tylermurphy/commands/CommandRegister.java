package net.tylermurphy.commands;

import java.util.Collection;

import java.util.HashMap;
import java.util.Map;

import net.tylermurphy.commands.admin.*;
import net.tylermurphy.commands.autoModeration.*;
import net.tylermurphy.commands.essential.*;
import net.tylermurphy.commands.fun.*;
import net.tylermurphy.commands.games.*;
import net.tylermurphy.commands.moderation.*;
import net.tylermurphy.commands.music.*;
import net.tylermurphy.commands.nsfw.*;
import net.tylermurphy.commands.relationship.*;
import net.tylermurphy.commands.social.*;
import net.tylermurphy.commands.webhooks.*;
import net.tylermurphy.commands.xp.*;

public class CommandRegister {
	
	public final static Map<String, ICommand> REGISTER = new HashMap<>();

	public static void registerCommands() {
		register(new Help());
		register(new Join());
		register(new Precent());
		register(new Ping());
		register(new WhoAreYou());
		register(new Leave());
		register(new Play());
		register(new Stop());
		register(new Queue());
		register(new Skip());
		register(new NowPlaying());
		register(new Pause());
		register(new Resume());
		register(new Kick());
		register(new Ban());
		register(new Unban());
		register(new Purge());
		register(new SetPrefix());
		register(new Loop());
		register(new Mute());
		register(new Unmute());
		register(new TempMute());
		register(new Dice());
		register(new BlackJack());
		register(new Nunchi());
		register(new Coinflip());
		register(new Phone());
		register(new Meme());
		register(new Joke());
		register(new Hug());
		register(new Kiss());
		register(new Gift());
		register(new Boop());
		register(new Cookie());
		register(new Pet());
		register(new Slap());
		register(new Battle());
		register(new Propose());
		register(new Tickle());
		register(new HighFive());
		register(new Lick());
		register(new Kill());
		register(new Punch());
		register(new Status());
		register(new XP());
		register(new Level());
		register(new Top());
		register(new SelfRole());
		register(new DelSelfRole());
		register(new SetPrefixWithSpace());
		register(new LogChannel());
		register(new ServerStats());
		register(new Caps());
		register(new Duplicate());
		register(new Mentions());
		register(new Emoji());
		register(new Spam());
		register(new SoftBan());
		register(new MultiDice());
		register(new DeleteWebhook());
		register(new CreateWebhook());
		register(new SetWebhookName());
		register(new SetWebhookAvatar());
		register(new Hentai());
		register(new LoopQueue());
		register(new Remove());
		register(new EightBall());
		register(new Warn());
		register(new Warns());
		register(new SetWarnAction());
		register(new ClearWarns());
		register(new E621());
		register(new Rule34());
		register(new Usage());
		register(new Description());
		register(new ForceSkip());
		register(new NHentai());
		register(new ToggleLeveling());
		register(new Cry());
		register(new Pout());
		register(new Dance());
		register(new ToggleWelcomeMessages());
		register(new Eject());
		register(new Divorce());
		register(new RejectProposal());
		register(new AcceptProposal());
		register(new Stats());
	}
	
	private static void register(ICommand command) {
        if (!REGISTER.containsKey(command.getInvoke())) {
        	REGISTER.put(command.getInvoke().toLowerCase(), command);
        }
    }
	
	public Collection<ICommand> getCommands() {
		return REGISTER.values();
	}
	
	public ICommand getCommand(String name) {
        return REGISTER.get(name);
    }
	
}
