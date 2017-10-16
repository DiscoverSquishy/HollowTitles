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

public class HollowTitlesAdminDeleteTitleCommand extends HollowPluginBaseAbstractCommandExecutor {

	private HollowTitles _plugin;

	public HollowTitlesAdminDeleteTitleCommand(HollowPluginBase plugin) {
		super(plugin);
		_plugin = (HollowTitles)plugin;
		require(new HollowPluginBaseCommandRequires(HollowPluginBaseCommandRequires.TYPE_PERMISSION, "hollowTitles.admin.removetitle"));
	}

	@Override
	public boolean execute(CommandSender sender, HollowPluginBaseDoCommandData data, String commandName, String label, String[] args) {
		ArrayList<String> parsedArgs = _removeModifiers(args, "-");
		
		 if (parsedArgs.size() < 2) {
             messageSender(ChatColor.RED + "Missing arguments. Use /title deleteTitle [GROUPNAME] [TitleName]", sender);
             return true;
         }
        
        String oldTitle = TextFormatHelper.concatArgs(parsedArgs, 1, " ");

        if (_plugin.getTool().removeTitle(parsedArgs.get(0), oldTitle)) {
        	messageSender("Removed '" + oldTitle + "' from group '" + parsedArgs.get(0) + "'!", sender);
        } else {
        	messageSender("Failed to remove '" + oldTitle + "' from group '" + parsedArgs.get(0) + "'!", sender);
        }
        return true;
	}

}
