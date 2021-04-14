package net.tylermurphy.Kenbot.commands;

import java.util.Collection;


import java.util.HashMap;
import java.util.Map;

import net.tylermurphy.Kenbot.Config;
import net.tylermurphy.Kenbot.commands.admin.*;
import net.tylermurphy.Kenbot.commands.autoModeration.*;
import net.tylermurphy.Kenbot.commands.essential.*;
import net.tylermurphy.Kenbot.commands.fun.*;
import net.tylermurphy.Kenbot.commands.games.*;
import net.tylermurphy.Kenbot.commands.moderation.*;
import net.tylermurphy.Kenbot.commands.music.*;
import net.tylermurphy.Kenbot.commands.nsfw.*;
import net.tylermurphy.Kenbot.commands.relationship.*;
import net.tylermurphy.Kenbot.commands.social.*;
import net.tylermurphy.Kenbot.commands.twitch.*;
import net.tylermurphy.Kenbot.commands.webhooks.*;
import net.tylermurphy.Kenbot.commands.xp.*;

public class CommandRegister {
	
	public final static Map<String, ICommand> REGISTER = new HashMap<>();

	public static void registerCommands() {
		
		// MUSIC
		
		register(new Leave());
		register(new Play());
		register(new Stop());
		register(new Queue());
		register(new Skip());
		register(new NowPlaying());
		register(new Pause());
		register(new Resume());
		register(new Loop());
		register(new Join());
		register(new LoopQueue());
		register(new Remove());
		register(new ForceSkip());
		
		//ADMIN
		
		register(new Purge());
		register(new SelfRole());
		register(new DelSelfRole());
		register(new LogChannel());
		register(new ServerStats());
		register(new ToggleWelcomeMessages());
		
		// AUTO MODERATION
		
		register(new Caps());
		register(new Duplicate());
		register(new Mentions());
		register(new Emoji());
		register(new Spam());
		
		// ESSENTIAL
		
		register(new Help());
		register(new Usage());
		register(new Description());
		
		// FUN
		
		register(new Phone());
		register(new Meme());
		register(new Joke());
		register(new Kill());
		register(new Eject());
		register(new Precent());
		register(new Ping());
		register(new WhoAreYou());
		
		// GAMES
		
		register(new Dice());
		register(new BlackJack());
		register(new Nunchi());
		register(new Coinflip());
		register(new Battle());
		register(new MultiDice());
		register(new EightBall());
		register(new Akinator());
		
		// MODERATION
		
		register(new Kick());
		register(new Ban());
		register(new Unban());
		register(new Mute());
		register(new Unmute());
		register(new TempMute());
		register(new SoftBan());
		register(new Warn());
		register(new Warns());
		register(new SetWarnAction());
		register(new ClearWarns());
		
		// NSFW
		
		if(Config.NSFW_ENABLED) {
		
			register(new Hentai());
			register(new E621());
			register(new Rule34());
			register(new NHentai());
		
		}
		
		// RELATIONSHIP
		
		register(new Propose());
		register(new Status());
		register(new Divorce());
		register(new RejectProposal());
		register(new AcceptProposal());
		
		// SOCIAL
		
		register(new Hug());
		register(new Kiss());
		register(new Gift());
		register(new Boop());
		register(new Cookie());
		register(new Pet());
		register(new Slap());
		register(new Tickle());
		register(new HighFive());
		register(new Lick());
		register(new Punch());
		register(new Cry());
		register(new Pout());
		register(new Dance());
		register(new Stats());
		
		// XP
		
		register(new XP());
		register(new Level());
		register(new Top());
		register(new ToggleLeveling());
		
		// WEBHOOKS
		
		register(new DeleteWebhook());
		register(new CreateWebhook());
		register(new SetWebhookName());
		register(new SetWebhookAvatar());
		
		// TWITCH
		
		if(Config.TWITCH_ENABLED) {
		
			register(new BroadcastWhenLive());
			register(new RemoveTwitchBroadcast());
			register(new SetTwitchChannel());
			register(new SetTwitchRole());
			
		}
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
