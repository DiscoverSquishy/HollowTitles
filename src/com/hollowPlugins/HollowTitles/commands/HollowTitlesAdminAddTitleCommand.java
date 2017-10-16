package com.hollowPlugins.HollowTitles.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hollowPlugins.HollowPluginBase.HollowPluginBase;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseAbstractCommandExecutor;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseCommandRequires;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseDoCommandData;
import com.hollowPlugins.HollowTitles.HollowTitles;
import com.hollowPlugins.utils.TextFormatHelper;

public class HollowTitlesAdminAddTitleCommand extends HollowPluginBaseAbstractCommandExecutor {

	private HollowTitles _plugin;

	public HollowTitlesAdminAddTitleCommand(HollowPluginBase plugin) {
		super(plugin);
		_plugin = (HollowTitles)plugin;
		require(new HollowPluginBaseCommandRequires(HollowPluginBaseCommandRequires.TYPE_PERMISSION, "hollowTitles.admin.addtitle"));
	}

	@Override
	public boolean execute(CommandSender sender, HollowPluginBaseDoCommandData data, String commandName, String label, String[] args) {
		ArrayList<String> parsedArgs = _removeModifiers(args, "-");
		
		 if (parsedArgs.size() < 2) {
             messageSender(ChatColor.RED + "Missing arguments. Use /title addtitle [GROUPNAME] [TitleName]", sender);
             return true;
         }
		
		 String newTitle = TextFormatHelper.concatArgs(parsedArgs, 1, " ");

         if (_plugin.getTool().addTitle(parsedArgs.get(0), newTitle)) {
        	 messageSender("Added '" + newTitle + "' to group '" + parsedArgs.get(0) + "'!", sender);
         } else {
        	 messageSender("Failed to add '" + newTitle + "' to group '" + parsedArgs.get(0) + "'!", sender);
         }
         return true;
	}

}
