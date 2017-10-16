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
import com.hollowPlugins.HollowTitles.GroupData;
import com.hollowPlugins.HollowTitles.HollowTitles;
import com.hollowPlugins.utils.TextFormatHelper;

import mkremins.fanciful.FancyMessage;

public class HollowTitlesUseCommand extends HollowPluginBaseAbstractCommandExecutor {

	private static final String LOCK_OVERRIDE = "o";
	
	private HollowTitles _plugin;

	public HollowTitlesUseCommand(HollowPluginBase plugin) {
		super(plugin);
		_plugin = (HollowTitles)plugin;
		require(new HollowPluginBaseCommandRequires(HollowPluginBaseCommandRequires.TYPE_PLAYER));
	}

	@Override
	public boolean execute(CommandSender sender, HollowPluginBaseDoCommandData data, String commandName, String label, String[] args) {
		ArrayList<String> parsedArgs = _removeModifiers(args, "-");
		Player player = (Player) sender;
		
		
		
        int nr = -1;
        if (parsedArgs.size() > 0) {
        	try {
                nr = Integer.parseInt(parsedArgs.get(0));
            } catch (NumberFormatException nfe) {
            }
        } else {
        	player.sendMessage(plugin.formatCommandResponse() + ChatColor.YELLOW + "You need to specifiy a title # or text to change your current title!");
        	return true;
        }
        
        List<String> titleList = _plugin.getTool().getAvailableTitles(player);
        int normalTitlesCount = titleList.size();
        titleList.addAll(_plugin.getTool().getCustomTitles(player.getName()));
        
        if (nr == -1) {
        	String titleToFind = TextFormatHelper.concatArgs(parsedArgs, 0, " ").toLowerCase();
        	
        	nr = _plugin.getTool().findNumberfromText(titleToFind, titleList);
        	
        	// No exact match found, we're gonna suggest titles
        	if (nr == -1) {
        		ArrayList<String> result = new ArrayList<>();
            	if (parsedArgs.size() > 2) {
                	for (int x = 2; x < parsedArgs.size(); x++) {
                		if (parsedArgs.get(x).length() < 4) {
                			continue;
                		}
                		result.addAll(_plugin.getTool().findTitles(args[x].toLowerCase(), titleList, normalTitlesCount));
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
            		
            		int width = _plugin.getTool().getListWidth();
                    int height = _plugin.getTool().getListHeight();
                    int count = 1;
                    FancyMessage titleListMessage = new FancyMessage("");
                    FancyMessage useTitleTooltip;
            		FancyMessage useTitleTooltip2 = new FancyMessage("as your new title.").color(ChatColor.GREEN);
            		boolean isEmpty = true;
                    
            		if (result.size() <= width * height) {
            			player.sendMessage(plugin.formatCommandResponse() + TextFormatHelper.format(plugin.conf.getSentence("noResult"), "[title]", titleToFind));
            			player.sendMessage(plugin.conf.getSentence("suggestion"));
            			count = 0;
            			for (int x = 0; x < result.size(); x++) {
            				isEmpty = false;
            				useTitleTooltip = new FancyMessage("Click to set").color(ChatColor.GREEN).then(ChatColor.stripColor(TextFormatHelper.parseColors(result.get(x).split(":")[1]))).color(ChatColor.GOLD);
                        	titleListMessage.then(TextFormatHelper.parseColors(result.get(x)) + " ")
                        		.formattedTooltip(useTitleTooltip, useTitleTooltip2).command("/title use " + ChatColor.stripColor(result.get(x).split(":")[1]));
            				
            				count++;
                            if (count == width) {
                            	titleListMessage.send(player);
                            	isEmpty = true;
                            	titleListMessage = new FancyMessage("");
                                count = 0;
                            }
            			}
            		}
            		
            		if (!isEmpty) {
	            		titleListMessage.send(player);
	                	titleListMessage = new FancyMessage("");
            		}
            	} else {
            		player.sendMessage(plugin.formatCommandResponse() + plugin.conf.getSentence("invalidTitle"));
            		
            		new FancyMessage("To use a title type ").suggest("/title use ")
            		.formattedTooltip(new FancyMessage("Click for the command suggestion.").color(ChatColor.GREEN))
            		.then("/title use [#/title]").color(ChatColor.AQUA).suggest("/title use ")
            		.formattedTooltip(new FancyMessage("Click for the command suggestion.").color(ChatColor.GREEN))
            		.send(player);
            		
            		new FancyMessage("You can list your titles by typing ").command("/title list")
            		.formattedTooltip(new FancyMessage("Click to see your available titles.").color(ChatColor.GREEN))
            		.then("/title list [page]").color(ChatColor.AQUA).command("/title list")
            		.formattedTooltip(new FancyMessage("Click to see your available titles.").color(ChatColor.GREEN))
            		.send(player);
            	}
        		
                return true;
        	}
        }
        
        if (nr >= titleList.size()) {
        	player.sendMessage(plugin.conf.getSentence("invalidTitle"));
        	
        	new FancyMessage("You can list your titles by typing ").command("/title list")
    		.formattedTooltip(new FancyMessage("Click to see your available titles.").color(ChatColor.GREEN))
    		.then("/title list [page]").color(ChatColor.AQUA).command("/title list")
    		.formattedTooltip(new FancyMessage("Click to see your available titles.").color(ChatColor.GREEN))
    		.send(player);
            return true;
        }

        String format = _plugin.getTool().getFormat(player);

        if (_plugin.getTool().getPlayerTitleLockState(player.getName()) && !data.has(LOCK_OVERRIDE)) {
        	player.sendMessage(plugin.conf.getSentence("lockedTitle"));
        	return true;
        }
        
        String newTitle = titleList.get(nr);
        GroupData playerGroup = _plugin.getTool().getGroupFor(newTitle);
        String groupName;
        if (playerGroup == null) {
        	groupName = _plugin.getTool().CUSTOM_GROUP;
        } else {
        	groupName = playerGroup.getName();
        }
        
        _plugin.getTool().setTitle(player.getName(), groupName, format, newTitle, true);
        
        player.sendMessage(TextFormatHelper.format(_plugin.conf.getSentence("titleChange"), "[title]", newTitle));
		
        return true;
		
	}

}
