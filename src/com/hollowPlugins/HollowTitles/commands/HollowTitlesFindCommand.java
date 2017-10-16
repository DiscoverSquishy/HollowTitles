package com.hollowPlugins.HollowTitles.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hollowPlugins.HollowPluginBase.HollowPluginBase;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseAbstractCommandExecutor;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseCommandRequires;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseDoCommandData;
import com.hollowPlugins.HollowTitles.HollowTitles;
import com.hollowPlugins.utils.TextFormatHelper;

import mkremins.fanciful.FancyMessage;

public class HollowTitlesFindCommand extends HollowPluginBaseAbstractCommandExecutor {

	private HollowTitles _plugin;

	public HollowTitlesFindCommand(HollowPluginBase plugin) {
		super(plugin);
		_plugin = (HollowTitles)plugin;
		require(new HollowPluginBaseCommandRequires(HollowPluginBaseCommandRequires.TYPE_PLAYER));
	}

	@Override
	public boolean execute(CommandSender sender, HollowPluginBaseDoCommandData data, String commandName, String label, String[] args) {
		ArrayList<String> parsedArgs = _removeModifiers(args, "-");
		Player player = (Player) sender;
		
		List<String> titleList = _plugin.getTool().getAvailableTitles(player);
        int normalTitlesCount = titleList.size();
        titleList.addAll(_plugin.getTool().getCustomTitles(player.getName()));
        
        if (titleList.isEmpty()) {
        	player.sendMessage(plugin.formatCommandResponse() + _plugin.conf.getSentence("noTitles"));
            return true;
        }

        if (parsedArgs.size() < 1) {
        	new FancyMessage(plugin.formatCommandResponse() + plugin.formatCommandResponse()).then("To find a title type ")
        	.then("/title find [title]").color(ChatColor.AQUA)
    		.formattedTooltip(new FancyMessage("Click to get the command suggestion.").color(ChatColor.GREEN))
    		.command("/title find ").send(player); 
            return true;
        }
        
        String titleToFind = TextFormatHelper.concatArgs(parsedArgs, 0, " ").toLowerCase();
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "[" + ChatColor.GREEN + "Find: " + titleToFind + ChatColor.GOLD + "]");
        
        int count = 0;
        int width = _plugin.getTool().getListWidth();
        int height = _plugin.getTool().getListHeight();
        
        FancyMessage useTitleTooltip;
		FancyMessage useTitleTooltip2 = new FancyMessage("as your new title.").color(ChatColor.GREEN);
        
        FancyMessage titleListMessage = new FancyMessage("");
        int maxSize = width * height;
        boolean isEmpty = true;
        int i;
        for (i = 0; i < titleList.size(); i++) {
            if (titleList.get(i).toLowerCase().contains(titleToFind)) {
            	if (i < normalTitlesCount) {
            		isEmpty = false;
            		useTitleTooltip = new FancyMessage("Click to set ").color(ChatColor.GREEN).then(TextFormatHelper.parseColors(titleList.get(i))).color(ChatColor.GOLD);
                	titleListMessage.then(" " + i + ": ").color(ChatColor.WHITE)
                		.then(TextFormatHelper.parseColors(titleList.get(i))).color(ChatColor.YELLOW)
                		.formattedTooltip(useTitleTooltip, useTitleTooltip2).command("/title use " + i);
            	} else if (i < titleList.size()) {
            		isEmpty = false;
            		useTitleTooltip = new FancyMessage("Click to set ").color(ChatColor.GREEN).then(TextFormatHelper.parseColors(titleList.get(i))).color(ChatColor.GOLD);
                	titleListMessage.then(" " + i + ": ").color(ChatColor.WHITE)
            		.then(TextFormatHelper.parseColors(titleList.get(i))).color(ChatColor.GOLD)
            		.formattedTooltip(useTitleTooltip, useTitleTooltip2).command("/title use " + i);
            	}

                count++;
                if (count == width) {
                	titleListMessage.send(player);
                	titleListMessage = new FancyMessage("");
                    count = 0;
                }

                maxSize--;
                if (maxSize == 0) {
                    player.sendMessage(plugin.conf.getSentence("tooMany"));
                    break;
                }
            }

        }
        
        if (!isEmpty) {
        	titleListMessage.send(player);
        	titleListMessage = new FancyMessage("");
        }

        // No results found, we're going to suggest something to the player
        if (maxSize == width * height) {
        	ArrayList<String> result = new ArrayList<String>();
        	if (parsedArgs.size() > 1) {
            	for (int x = 1; x < parsedArgs.size(); x++) {
            		if (parsedArgs.get(x).length() < 4) {
            			continue;
            		}
            		result.addAll(_plugin.getTool().findTitles(parsedArgs.get(x).toLowerCase(), titleList, normalTitlesCount));
            	}
        	} else {
        		for (int x = 0; x < parsedArgs.get(0).length() - 4; x++) {
            		result.addAll(_plugin.getTool().findTitles(parsedArgs.get(0).toLowerCase().substring(x, x + 3), titleList, normalTitlesCount));
            	}
        	}
        	if (result.size() != 0) {
        		// Removes duplicates
        		for (int x = 0; x < result.size(); x++) {
        			for (int y = x + 1; y < result.size(); y++) {
        				if (result.get(x).equals(result.get(y))) {
        					result.remove(y);
        				}
        			}
        		}
        	}
        	
        	if (result.size() != 0) {
        		
        		if (result.size() > width * height) {
        			player.sendMessage(plugin.conf.getSentence("tooMany"));
        		} else {
        			player.sendMessage(TextFormatHelper.format(plugin.conf.getSentence("noResult"), "[title]", titleToFind));
        			player.sendMessage(plugin.conf.getSentence("suggestion"));
        			count = 0;
        			titleListMessage = new FancyMessage("");
        			for (int x = 0; x < result.size(); x++) {
        				useTitleTooltip = new FancyMessage("Click to set").color(ChatColor.GREEN).then(ChatColor.stripColor(TextFormatHelper.parseColors(result.get(x).split(":")[1]))).color(ChatColor.GOLD);
                    	titleListMessage.then(TextFormatHelper.parseColors(result.get(x)) + " ")
                    		.formattedTooltip(useTitleTooltip, useTitleTooltip2).command("/title use " + ChatColor.stripColor(result.get(x).split(":")[1]));
        				
        				count++;
                        if (count == width) {
                        	titleListMessage.send(player);
                        	titleListMessage = new FancyMessage("");
                            count = 0;
                        }
        			}
        		}
        	} else {
        		player.sendMessage(TextFormatHelper.format(plugin.conf.getSentence("noResult"), "[title]", titleToFind));
        	}
        }
        return true;
		
	}

}
