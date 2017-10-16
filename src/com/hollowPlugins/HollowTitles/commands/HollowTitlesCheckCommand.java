package com.hollowPlugins.HollowTitles.commands;

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

public class HollowTitlesCheckCommand extends HollowPluginBaseAbstractCommandExecutor {

	private HollowTitles _plugin;

	public HollowTitlesCheckCommand(HollowPluginBase plugin) {
		super(plugin);
		_plugin = (HollowTitles)plugin;
		require(new HollowPluginBaseCommandRequires(HollowPluginBaseCommandRequires.TYPE_PLAYER));
	}

	@Override
	public boolean execute(CommandSender sender, HollowPluginBaseDoCommandData data, String commandName, String label, String[] args) {
		Player player = (Player) sender;
		
		String currentTitle = _plugin.getTool().getCurrentTitle(player.getName());
		if (currentTitle == null) {
			player.sendMessage(plugin.formatCommandResponse() + ChatColor.YELLOW + "You don't currently have a title!");
		} else {
			player.sendMessage(plugin.formatCommandResponse() + "Your current title is: " + ChatColor.GOLD + TextFormatHelper.parseColors(currentTitle));
		}
		
		new FancyMessage("Change it?").color(ChatColor.GREEN).style(ChatColor.ITALIC)
		.formattedTooltip(new FancyMessage("Click to see your available titles.").color(ChatColor.GREEN))
		.command("/title list").send(player); 
		
		return true;
	}

}
