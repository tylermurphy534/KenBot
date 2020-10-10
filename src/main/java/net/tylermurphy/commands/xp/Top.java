package net.tylermurphy.commands.xp;

import java.util.ArrayList;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.LevelManager;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class Top implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		List<String> data = DatabaseManager.UserSettings.getAll(event.getGuild().getIdLong(), "XP");
		List<String> ids = new ArrayList<String>();
		List<Integer> xps = new ArrayList<Integer>();
		for(String s : data) {
			String[] strs = s.split(":");
			ids.add(strs[0]);
			xps.add(Integer.parseInt(strs[1]));
		}
		quickSort(ids,xps,0,ids.size()-1);
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setTitle("Guild Rankings");
		int max = 10;
		int offset = 0;
		boolean check = false;
		for(int i=0; i < Math.min(max, ids.size()); i++) {
			User user = event.getJDA().getUserById(ids.get(i));
			if(user == null) { max++; offset--; continue; }
			else check = true;
			int xp = xps.get(i);
			embed.appendDescription(String.format("**Rank %s**\n%s\t`Level: %s, XP: %s`\n", i+1+offset, user, LevelManager.getLevel(xp), xp));
		}
		if(!check) {
			channel.sendMessage(":x: An error has occured, please try again later.").queue();
			return;
		}
		channel.sendMessage(embed.build()).queue();
	}
	
	private void quickSort(List<String> ids, List<Integer> arr, int low, int high)
	{
	    if (low < high){
	        int pi = partition(ids, arr, low, high);
	        quickSort(ids, arr, low, pi - 1);
	        quickSort(ids, arr, pi + 1, high);
	    }
	}
	
	private int partition (List<String> ids, List<Integer> arr, int low, int high){
	    int pivot = arr.get(high);
	 
	    int i = (low - 1);

	    for (int j = low; j <= high - 1; j++){
	        if (arr.get(j) > pivot)
	        {
	            i++;
	            int temp = arr.get(i);
	            arr.set(i, arr.get(j));
	            arr.set(j, temp);
	            String temp2 = ids.get(i);
	            ids.set(i, ids.get(j));
	            ids.set(j, temp2);
	        }
	    }
	    int temp = arr.get(i+1);
	    arr.set(i+1, arr.get(high));
        arr.set(high, temp);
        String temp2 = ids.get(i+1);
        ids.set(i+1, ids.get(high));
        ids.set(high, temp2);
	    return (i + 1);
	}

	public String getInvoke() {
		return "top";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Get Top Users by Level in server";
	}

}
