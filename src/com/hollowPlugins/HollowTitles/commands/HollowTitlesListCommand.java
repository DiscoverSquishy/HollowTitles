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

public class HollowTitlesListCommand extends HollowPluginBaseAbstractCommandExecutor {

	private HollowTitles _plugin;

	public HollowTitlesListCommand(HollowPluginBase plugin) {
		super(plugin);
		_plugin = (HollowTitles)plugin;
		require(new HollowPluginBaseCommandRequires(HollowPluginBaseCommandRequires.TYPE_PLAYER));
	}

	@Override
	public boolean execute(CommandSender sender, HollowPluginBaseDoCommandData data, String commandName, String label, String[] args) {
		ArrayList<String> parsedArgs = _removeModifiers(args, "-");
		Player player = (Player) sender;

		FancyMessage useTitleTooltip;
		FancyMessage useTitleTooltip2 = new FancyMessage("as your new title.").color(ChatColor.GREEN);
		
		List<String> titleList = _plugin.getTool().getAvailableTitles(player);
        int normalTitlesCount = titleList.size();
        titleList.addAll(_plugin.getTool().getCustomTitles(player.getName()));
        
        if (titleList.isEmpty()) {
        	player.sendMessage(plugin.formatCommandResponse() + _plugin.conf.getSentence("noTitles"));
            return true;
        }

        int page = 0;
        if (parsedArgs.size() > 0) {
            try {
                page = Integer.parseInt(parsedArgs.get(0)) - 1;
            } catch (NumberFormatException nfe) {
            }
        }

        if (page < 0) {
            page = 0;
        }
        int width = _plugin.getTool().getListWidth();
        int height = _plugin.getTool().getListHeight();

        int maxPages = titleList.size() / (width * height);
        if (page > maxPages) {
            page = maxPages;
        }
        int paginatedPage = page;
        
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "[" + ChatColor.GREEN + "Your titles" + 
        ChatColor.GOLD + " | " + ChatColor.RESET + ChatColor.ITALIC + " Page " + (page + 1) + "/" + (maxPages + 1) + ChatColor.GOLD + "]");
        
        page = page * (width * height); // The number is width * height
        int count = 1;
        FancyMessage titleListMessage = new FancyMessage("");
        
        for (int i = page; i < page + (width * height); i++) {
            if (i < normalTitlesCount) {
            	useTitleTooltip = new FancyMessage("Click to set ").color(ChatColor.GREEN).then(TextFormatHelper.parseColors(titleList.get(i))).color(ChatColor.GOLD);
            	titleListMessage.then(" " + i + ": ").color(ChatColor.WHITE)
            		.then(TextFormatHelper.parseColors(titleList.get(i))).color(ChatColor.YELLOW)
            		.formattedTooltip(useTitleTooltip, useTitleTooltip2).command("/title use " + i);
            } else if (i < titleList.size()) {
            	useTitleTooltip = new FancyMessage("Click to set ").color(ChatColor.GREEN).then(TextFormatHelper.parseColors(titleList.get(i))).color(ChatColor.GOLD);
            	titleListMessage.then(" " + i + ": ").color(ChatColor.WHITE)
        		.then(TextFormatHelper.parseColors(titleList.get(i))).color(ChatColor.GOLD)
        		.formattedTooltip(useTitleTooltip, useTitleTooltip2).command("/title use " + i);
            }

            if (count == width) {
            	titleListMessage.send(player);
            	titleListMessage = new FancyMessage("");
                if (i >= titleList.size()) {
                    break;
                }
                count = 1;
            } else {
                count++;
            }
        }

        FancyMessage pageNavigation = new FancyMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "[");
        if (paginatedPage > 0) {
        	pageNavigation.then("Previous").color(ChatColor.AQUA).style(ChatColor.ITALIC).formattedTooltip(new FancyMessage("Click for previous page.").color(ChatColor.GREEN))
        	.command("/title list " + (paginatedPage));
        }
        if (paginatedPage > 0 && paginatedPage < maxPages) {
        	pageNavigation.then(" | ").color(ChatColor.GOLD);
        }
        if (paginatedPage < maxPages) {
        	pageNavigation.then("Next").color(ChatColor.AQUA).style(ChatColor.ITALIC).formattedTooltip(new FancyMessage("Click for next page.").color(ChatColor.GREEN))
        	.command("/title list " + (paginatedPage + 2));
        }
        pageNavigation.then("]").color(ChatColor.GOLD);
        pageNavigation.send(player);
        
        return true;
        
	}

}
