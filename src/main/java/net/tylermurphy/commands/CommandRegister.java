package net.tylermurphy.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.tylermurphy.commands.autoModeration.Caps;
import net.tylermurphy.commands.autoModeration.Duplicate;
import net.tylermurphy.commands.autoModeration.Emoji;
import net.tylermurphy.commands.autoModeration.Mentions;
import net.tylermurphy.commands.autoModeration.Spam;
import net.tylermurphy.commands.essential.Description;
import net.tylermurphy.commands.essential.Help;
import net.tylermurphy.commands.essential.Usage;
import net.tylermurphy.commands.fun.Joke;
import net.tylermurphy.commands.fun.Kill;
import net.tylermurphy.commands.fun.Meme;
import net.tylermurphy.commands.fun.Phone;
import net.tylermurphy.commands.fun.Ping;
import net.tylermurphy.commands.fun.Precent;
import net.tylermurphy.commands.fun.WhoAreYou;
import net.tylermurphy.commands.games.Battle;
import net.tylermurphy.commands.games.BlackJack;
import net.tylermurphy.commands.games.Coinflip;
import net.tylermurphy.commands.games.Dice;
import net.tylermurphy.commands.games.EightBall;
import net.tylermurphy.commands.games.MultiDice;
import net.tylermurphy.commands.games.Nunchi;
import net.tylermurphy.commands.moderation.Ban;
import net.tylermurphy.commands.moderation.ClearWarns;
import net.tylermurphy.commands.moderation.DelSelfRole;
import net.tylermurphy.commands.moderation.Kick;
import net.tylermurphy.commands.moderation.LogChannel;
import net.tylermurphy.commands.moderation.Mute;
import net.tylermurphy.commands.moderation.Purge;
import net.tylermurphy.commands.moderation.SelfRole;
import net.tylermurphy.commands.moderation.ServerStats;
import net.tylermurphy.commands.moderation.SetPrefix;
import net.tylermurphy.commands.moderation.SetPrefixWithSpace;
import net.tylermurphy.commands.moderation.SetWarnAction;
import net.tylermurphy.commands.moderation.SoftBan;
import net.tylermurphy.commands.moderation.TempMute;
import net.tylermurphy.commands.moderation.Unban;
import net.tylermurphy.commands.moderation.Unmute;
import net.tylermurphy.commands.moderation.Warn;
import net.tylermurphy.commands.moderation.Warns;
import net.tylermurphy.commands.music.ForceSkip;
import net.tylermurphy.commands.music.Join;
import net.tylermurphy.commands.music.Leave;
import net.tylermurphy.commands.music.Loop;
import net.tylermurphy.commands.music.LoopQueue;
import net.tylermurphy.commands.music.NowPlaying;
import net.tylermurphy.commands.music.Pause;
import net.tylermurphy.commands.music.Play;
import net.tylermurphy.commands.music.Queue;
import net.tylermurphy.commands.music.Remove;
import net.tylermurphy.commands.music.Resume;
import net.tylermurphy.commands.music.Skip;
import net.tylermurphy.commands.music.Stop;
import net.tylermurphy.commands.nsfw.E621;
import net.tylermurphy.commands.nsfw.Hentai;
import net.tylermurphy.commands.nsfw.NHentai;
import net.tylermurphy.commands.nsfw.Rule34;
import net.tylermurphy.commands.social.Boop;
import net.tylermurphy.commands.social.Cookie;
import net.tylermurphy.commands.social.GetShip;
import net.tylermurphy.commands.social.Gift;
import net.tylermurphy.commands.social.HighFive;
import net.tylermurphy.commands.social.Hug;
import net.tylermurphy.commands.social.Kiss;
import net.tylermurphy.commands.social.Lick;
import net.tylermurphy.commands.social.Pet;
import net.tylermurphy.commands.social.Punch;
import net.tylermurphy.commands.social.Ship;
import net.tylermurphy.commands.social.Slap;
import net.tylermurphy.commands.social.Tickle;
import net.tylermurphy.commands.webhooks.CreateWebhook;
import net.tylermurphy.commands.webhooks.DeleteWebhook;
import net.tylermurphy.commands.webhooks.SetWebhookName;
import net.tylermurphy.commands.webhooks.SetWebhookURL;
import net.tylermurphy.commands.xp.Level;
import net.tylermurphy.commands.xp.Top;
import net.tylermurphy.commands.xp.XP;

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
		register(new Ship());
		register(new Tickle());
		register(new HighFive());
		register(new Lick());
		register(new Kill());
		register(new Punch());
		register(new GetShip());
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
		register(new SetWebhookURL());
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
