package com.hollowPlugins.HollowTitles.commands;

import org.bukkit.command.CommandSender;

import com.hollowPlugins.HollowPluginBase.HollowPluginBase;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseAbstractCommandExecutor;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseCommandRequires;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseDoCommandData;
import com.hollowPlugins.HollowTitles.HollowTitles;

public class HollowTitlesAdminReloadCommand extends HollowPluginBaseAbstractCommandExecutor {

	private HollowTitles _plugin;

	public HollowTitlesAdminReloadCommand(HollowPluginBase plugin) {
		super(plugin);
		_plugin = (HollowTitles)plugin;
		require(new HollowPluginBaseCommandRequires(HollowPluginBaseCommandRequires.TYPE_PERMISSION, "hollowTitles.admin.reload"));
	}

	@Override
	public boolean execute(CommandSender sender, HollowPluginBaseDoCommandData data, String commandName, String label, String[] args) {
		_plugin.getTool().reload();
        messageSender("Reloading Titles configs... Done!", sender);
		
		return true;
	}

}
