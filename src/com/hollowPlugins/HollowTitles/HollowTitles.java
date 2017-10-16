package com.hollowPlugins.HollowTitles;

import com.hollowPlugins.HollowPluginBase.HollowPluginBase;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesAdminAddTitleCommand;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesAdminDeleteTitleCommand;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesAdminListGroupsCommand;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesAdminPlayerCommand;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesAdminReloadCommand;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesCheckCommand;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesClearCommand;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesFindCommand;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesHelpCommand;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesListCommand;
import com.hollowPlugins.HollowTitles.commands.HollowTitlesUseCommand;

public class HollowTitles extends HollowPluginBase {

	public final String PLAYERS_CONFIG = "players";
	private HollowTitlesTool tool;
	
	// TODO > Clean up players data by removing specific format and using just group?
	public HollowTitles() {
		this.hasConfig = true;
		
		addCustomConfig(PLAYERS_CONFIG);
	}

	@Override
	protected void _onPluginReady() {
		tool = new HollowTitlesTool(this);

		registerListener(new HollowTitlesListener(this));
		
	}
	
	public HollowTitlesTool getTool() {
		return tool;
	}

	@Override
	protected void _addCommands() {
		commander.addCommand("title", new HollowTitlesCheckCommand(this));
		commander.addSubCommand("title", "help", new HollowTitlesHelpCommand(this));
		commander.addSubCommand("title", "clear", new HollowTitlesClearCommand(this));
		commander.addSubCommand("title", "find", new HollowTitlesFindCommand(this));
		commander.addSubCommand("title", "list", new HollowTitlesListCommand(this));
		commander.addSubCommand("title", "use", new HollowTitlesUseCommand(this));
		
		commander.addSubCommand("title", "reload", new HollowTitlesAdminReloadCommand(this));
		commander.addSubCommand("title", "group", new HollowTitlesAdminListGroupsCommand(this));
		commander.addSubCommand("title", "addtitle", new HollowTitlesAdminAddTitleCommand(this));
		commander.addSubCommand("title", "deletetitle", new HollowTitlesAdminDeleteTitleCommand(this));
		commander.addSubCommand("title", "player", new HollowTitlesAdminPlayerCommand(this));
		
		
	}

}
