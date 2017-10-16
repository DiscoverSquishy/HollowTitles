package com.hollowPlugins.HollowTitles.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hollowPlugins.HollowPluginBase.HollowPluginBase;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseAbstractCommandExecutor;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseCommandRequires;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseDoCommandData;

import mkremins.fanciful.FancyMessage;

public class HollowTitlesHelpCommand extends HollowPluginBaseAbstractCommandExecutor {

	public HollowTitlesHelpCommand(HollowPluginBase plugin) {
		super(plugin);
		require(new HollowPluginBaseCommandRequires(HollowPluginBaseCommandRequires.TYPE_PLAYER));
	}

	@Override
	public boolean execute(CommandSender sender, HollowPluginBaseDoCommandData data, String commandName, String label, String[] args) {
		Player player = (Player) sender;
		
		player.sendMessage(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.GOLD + "[" + ChatColor.YELLOW + "HollowTitles Commands" + ChatColor.GOLD + "]");
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "(Hover over a " + ChatColor.GREEN + "" + ChatColor.ITALIC + "command " + ChatColor.GRAY + "" + ChatColor.ITALIC + "for info, click to run it.)");
		
		new FancyMessage("/title help").color(ChatColor.AQUA).style(ChatColor.ITALIC).command("/title help")
		.formattedTooltip(new FancyMessage("Display this help.").color(ChatColor.GREEN))
		.send(player);
		
		new FancyMessage("/title list [page]").color(ChatColor.AQUA).style(ChatColor.ITALIC).command("/title list")
		.formattedTooltip(new FancyMessage("List all your available titles.").color(ChatColor.GREEN))
		.send(player);
		
		new FancyMessage("/title find [title]").color(ChatColor.AQUA).style(ChatColor.ITALIC).suggest("/title find ")
		.formattedTooltip(new FancyMessage("Find a specific title.").color(ChatColor.GREEN))
		.send(player);
		
		new FancyMessage("/title use [# / title]").color(ChatColor.AQUA).style(ChatColor.ITALIC).suggest("/title use ")
		.formattedTooltip(new FancyMessage("Change your title.").color(ChatColor.GREEN))
		.send(player);
		
		new FancyMessage("/title clear").color(ChatColor.AQUA).style(ChatColor.ITALIC).suggest("/title clear ")
		.formattedTooltip(new FancyMessage("Clear your current title.").color(ChatColor.GREEN))
		.send(player);
		
		return true;
	}

}
