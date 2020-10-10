package net.tylermurphy.commands.moderation;

import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class TempMute implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		Member member = event.getMember();
		Member selfMember = event.getGuild().getSelfMember();
		List<Member> metionedMembers = event.getMessage().getMentionedMembers();
		
		if(metionedMembers.isEmpty() || args.size() < 3) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		if(metionedMembers.size() > 1) {
			channel.sendMessage(":x: Please only mention one user.").queue();
			return;
		}
		
		Member target = metionedMembers.get(0);
		String reason = String.join(" ", args.subList(2, args.size()));
		
		if (!member.hasPermission(Permission.MANAGE_ROLES) || !member.canInteract(target)) {
			channel.sendMessage(":x: You dont have permission to use this command.").queue();
			return;
		}
		
		if (!member.hasPermission(Permission.MANAGE_ROLES) || !selfMember.canInteract(target)) {
			channel.sendMessage(":x: I can't manage roles that user or I don't have permission to manage roles.").queue();
			return;
		}
		
		List<Role> roles = event.getGuild().getRolesByName("muted", true);
		
		Role mutedRole;
		try{
			mutedRole = roles.get(0);
		}catch(Exception e) {
			channel.sendMessage(":x: Unable to find role names `Muted`").queue();
			return;
		}
		
		int time;
		try {
			time = parseTimeAmount(args.get(1));
		} catch(NumberFormatException e) {
			channel.sendMessage(":x: Error formatting time: "+args.get(1)).queue();
			return;
		}
		
		tempmute(target,time,parseTimeUnit(args.get(1)),mutedRole);
		channel.sendMessage(String.format("%s tempmuted %s for %s, for reason: `%s`",event.getAuthor(),target,args.get(1),reason)).queue();
		
		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Infraction Notice")
				.setColor(Color.yellow)
				.setDescription(String.format("%s tempmuted you in %s for %s, for reason: `%s`",event.getAuthor(),event.getGuild(),args.get(1),reason));
		
		target.getUser().openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(builder.build()).queue();
		});
	}
	
	private int parseTimeAmount(String time) throws NumberFormatException {
		char[] t = time.toCharArray();
		int breakPoint = 0;
		String amount = ""; 
		int parsedAmount = 0;
		for(int i=0; i < t.length; i++) {
			if(t[i] == 's' || t[i] == 'S') {
				breakPoint = i;
				break;
			}else if(t[i] == 'm' || t[i] == 'M') {
				breakPoint = i;
				break;
			}else if(t[i] == 'h' || t[i] == 'H') {
				breakPoint = i;
				break;
			}else if(t[i] == 'd' || t[i] == 'D') {
				breakPoint = i;
				break;
			}
		}
		
		for(int i = 0;i < breakPoint; i++) {
			amount += t[i];
		}
		
		parsedAmount = Integer.parseInt(amount);
		
		return parsedAmount;
	}
	
	private TimeUnit parseTimeUnit(String time) {
		TimeUnit unit = TimeUnit.SECONDS;
		char[] t = time.toCharArray();
		for(int i=0; i < t.length; i++) {
			if(t[i] == 's' || t[i] == 'S') {
				unit = TimeUnit.SECONDS;
				break;
			}else if(t[i] == 'm' || t[i] == 'M') {
				unit = TimeUnit.MINUTES;
				break;
			}else if(t[i] == 'h' || t[i] == 'H') {
				unit = TimeUnit.HOURS;
				break;
			}else if(t[i] == 'd' || t[i] == 'D') {
				unit = TimeUnit.DAYS;
				break;
			}
		}
		
		return unit;
	}
	
	public void tempmute(Member target, int time, TimeUnit unit, Role mutedRole) {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			int counter = 0;
			public void run() {
				counter++;
				if(counter == 1) {
					target.getGuild().addRoleToMember(target, mutedRole).queue();
				}
				if(counter == 2) {
					target.getGuild().removeRoleFromMember(target, mutedRole).queue();
					this.cancel();
				}
			}
		};
		switch(unit) {
			case SECONDS:
				timer.schedule(task, 0, time * 1000);
				break;
			case MINUTES:
				timer.schedule(task, 0, time * 1000 * 60);
				break;
			case HOURS:
				timer.schedule(task, 0, time * 1000 * 60 * 60);
				break;
			case DAYS:
				timer.schedule(task, 0, time * 1000 & 60 * 60 * 24);
				break;
			default:
				timer.schedule(task, 0, time * 1000);
				break;
		}
	}

	public String getInvoke() {
		return "tempmute";
	}
	
	public String getUsage() {
		return "TempMute <@User> <time> <reason>";
	}
	
	public String getDescription() {
		return "Temporarily mute someone for some time";
	}
	
}
