package com.hollowPlugins.HollowTitles.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hollowPlugins.HollowPluginBase.HollowPluginBase;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseAbstractCommandExecutor;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseCommandRequires;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseDoCommandData;
import com.hollowPlugins.HollowTitles.GroupData;
import com.hollowPlugins.HollowTitles.HollowTitles;

public class HollowTitlesAdminListGroupsCommand extends HollowPluginBaseAbstractCommandExecutor {

	private HollowTitles _plugin;

	public HollowTitlesAdminListGroupsCommand(HollowPluginBase plugin) {
		super(plugin);
		_plugin = (HollowTitles)plugin;
		require(new HollowPluginBaseCommandRequires(HollowPluginBaseCommandRequires.TYPE_PERMISSION, "hollowTitles.admin.listgroups"));
	}

	@Override
	public boolean execute(CommandSender sender, HollowPluginBaseDoCommandData data, String commandName, String label, String[] args) {
		
		HashMap<String, GroupData> groups = _plugin.getTool().getGroups();
		
		for (GroupData group : groups.values()) {
			messageSender("Group: " + ChatColor.GRAY + group.getName(), sender);
			messageSender("Titles:", sender);
			String text = "";
            for (String t : group.getTitles()) {
            	text += ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + t;
            }
            messageSender(text, sender);
		}
		
		return true;
	}

}
