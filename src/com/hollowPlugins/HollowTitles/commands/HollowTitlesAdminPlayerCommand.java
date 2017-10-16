package com.hollowPlugins.HollowTitles.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hollowPlugins.HollowPluginBase.HollowPluginBase;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseAbstractCommandExecutor;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseDoCommandData;
import com.hollowPlugins.HollowTitles.GroupData;
import com.hollowPlugins.HollowTitles.HollowTitles;
import com.hollowPlugins.utils.TextFormatHelper;

import net.md_5.bungee.api.ChatColor;

public class HollowTitlesAdminPlayerCommand extends HollowPluginBaseAbstractCommandExecutor {

	private HollowTitles _plugin;

	public HollowTitlesAdminPlayerCommand(HollowPluginBase plugin) {
		super(plugin);
		_plugin = (HollowTitles)plugin;
		//require(new HollowPluginBaseCommandRequires(HollowPluginBaseCommandRequires.TYPE_PERMISSION, "hollowTitles.admin.customTitles.*"));
	}

	@Override
	public boolean execute(CommandSender sender, HollowPluginBaseDoCommandData data, String commandName, String label, String[] args) {
		ArrayList<String> parsedArgs = _removeModifiers(args, "-");
		Player player = null;
		if (sender instanceof Player) {
            player = (Player) sender;
        }
		
		if (parsedArgs.size() < 2) {
    		messageSender(ChatColor.RED + "Player Custom title command issued without valid argument!", sender);
    		messageSender(ChatColor.RED + "Usage: " + ChatColor.GOLD + "/title player" + ChatColor.ITALIC + " [add|remove|list]", sender);
    		return true;
    	}
		
		String targetName = parsedArgs.get(1);
    	Player target = _plugin.getServer().getPlayer(targetName);
    	if (target != null) {
    		targetName = target.getName();
    	}
    	
    	String customTitle = TextFormatHelper.concatArgs(parsedArgs, 2, " ");
    	
    	String subCommand = parsedArgs.get(0);
    	
    	// Add custom title
    	if (subCommand.equalsIgnoreCase("add")) {
    		if (player != null && !player.hasPermission("hollowTitles.admin.customTitles.basic")) {
    			messageSender(ChatColor.RED + "You don't have permission for that command!", sender);
                return true;
            }
    		
    		if (parsedArgs.size() < 3) {
    			messageSender(ChatColor.RED + "Arguments count mismatch in command /title player add", sender);
                return false;
            }
    		
    		if (_plugin.getTool().getCustomTitlesMaxLength() > 0 && customTitle.length() > _plugin.getTool().getCustomTitlesMaxLength()) {
    			messageSender(plugin.conf.getSentence("customTitleTooLong"), sender);
    			return true;
    		}
    		
    		boolean result = _plugin.getTool().addCustomTitle(targetName, customTitle);
    		if (result) {
    			String text = plugin.conf.getSentence("customTitleAdd");
        		text = TextFormatHelper.format(text, "[player]", targetName);
        		text = TextFormatHelper.format(text, "[title]", customTitle);
        		messageSender(text, sender);
    			if (target != null && target.isOnline()) {
        			messageSender(TextFormatHelper.format(plugin.conf.getSentence("playerCustomTitleAdd"), "[title]", customTitle), target);
        		}
    		} else {
    			messageSender(TextFormatHelper.format(plugin.conf.getSentence("customTitleFail"), "[player]", targetName), sender);
    		}
    		return result;
    		
    	// Remove custom title
    	} else if (subCommand.equalsIgnoreCase("remove")) {
    		if (player != null && !player.hasPermission("hollowTitles.admin.customTitles.basic")) {
    			messageSender(ChatColor.RED + "You don't have permission for that command!", sender);
                return true;
            }
    		
    		if (parsedArgs.size() < 3) {
    			messageSender(ChatColor.RED + "Arguments count mismatch in command /title player remove", sender);
                return false;
            }
    		
    		 int nr = -1;
             try {
                 nr = Integer.parseInt(parsedArgs.get(2));
             } catch (NumberFormatException nfe) {
            	 nr = -1;
             }
            boolean result = false;
    		if (nr != -1) {
    			customTitle = _getCustomTitleFromNumber(targetName, nr);
    		}
    		if (customTitle != null) {
    			result = _plugin.getTool().removeCustomTitle(targetName, customTitle);
    		}
    		
    		if (result) {
    			String text = plugin.conf.getSentence("customTitleRemove");
        		text = TextFormatHelper.format(text, "[player]", targetName);
        		text = TextFormatHelper.format(text, "[title]", customTitle);
        		messageSender(text, player);
        		if (target != null && target.isOnline()) {
        			String playerText = plugin.conf.getSentence("playerCustomTitleRemove");
        			playerText = TextFormatHelper.format(playerText, "[title]", customTitle);
        			messageSender(playerText, target);
        		}
    		} else {
    			messageSender(TextFormatHelper.format(plugin.conf.getSentence("customTitleFail"), "[player]", targetName), sender);
    		}
    		return result;
    	
    	// List player custom titles
    	} else if (subCommand.equalsIgnoreCase("list")) {
    		if (player != null && !player.hasPermission("hollowTitles.admin.customTitles.basic")) {
    			messageSender(ChatColor.RED + "You don't have permission for that command!", sender);
                return true;
            }
    		
    		if (parsedArgs.size() < 2) {
    			messageSender(ChatColor.RED + "Arguments count mismatch in command /title player list", sender);
                return false;
            }
    		
    		 List<String> titleList = _plugin.getTool().getCustomTitles(targetName);
             if (titleList.isEmpty()) {
    			messageSender(TextFormatHelper.format(plugin.conf.getSentence("playerCustomTitleListEmpty"), "[player]", targetName), sender);
                return true;
             }
             
             int normalTitlesCount = _plugin.getTool().getAvailableTitles(player).size();
             int width = _plugin.getTool().getListWidth();
             
             messageSender(ChatColor.WHITE + "Custom titles for " + ChatColor.GREEN + targetName + ChatColor.WHITE + ":", sender);
             int count = 1;
             String text = "";

             for (int i = 0; i < titleList.size(); i++) {
            	 text += "" + ChatColor.WHITE + (normalTitlesCount + i) + ": " + ChatColor.GOLD + titleList.get(i) + " ";

                 if (count == width) {
                     if (!"".equals(text)) {
                    	 messageSender(text, player);
                     }
                     if (i >= titleList.size()) {
                         break;
                     }
                     text = "";
                     count = 1;
                 } else {
                     count++;
                 }
             }
             if (text != "") {
            	 messageSender(text, player);
             }
             
             return true;
    		
    	} else if (subCommand.equalsIgnoreCase("change")) {
    		if (player != null && !player.hasPermission("hollowTitles.admin.customTitles.changePlayer")) {
    			messageSender(ChatColor.RED + "You don't have permission for that command!", sender);
                return true;
            }
    		
    		int nr = -1;
            try {
                nr = Integer.parseInt(parsedArgs.get(1));
            } catch (NumberFormatException nfe) {
            }
            
            List<String> titleList = _plugin.getTool().getAvailableTitles(player);
            titleList.addAll(_plugin.getTool().getCustomTitles(player.getName()));
            
            if (nr == -1) {
            	String titleText = TextFormatHelper.concatArgs(parsedArgs, 2, " ");
            	titleText = titleText.toLowerCase();
            	
            	nr = _plugin.getTool().findNumberfromText(titleText, titleList);
            	
            	// No exact match found
            	if (nr == -1) {
            		messageSender(plugin.conf.getSentence("invalidTitle"), sender);
                    return true;
            	}
            }
            
            if (nr >= titleList.size()) {
            	messageSender(plugin.conf.getSentence("invalidTitle"), sender);
                return true;
            }

            String format = _plugin.getTool().getFormat(target);
            
            String newTitle = titleList.get(nr);
            GroupData playerGroup = _plugin.getTool().getGroupFor(newTitle);
            
            _plugin.getTool().setTitle(targetName, playerGroup.getName(), format, newTitle, true);
            
            if (target != null) {
        		messageSender(TextFormatHelper.format(plugin.conf.getSentence("playerTitleChange"), "[title]", titleList.get(nr)), target);
            }
            return true;
    		
    	} else if (subCommand.equalsIgnoreCase("lock")) {
    		if (player != null && !player.hasPermission("hollowTitles.admin.customTitles.toggleLock")) {
    			messageSender(ChatColor.RED + "You don't have permission for that command!", sender);
                return true;
            }
    		
    		_plugin.getTool().setPlayerTitleLockState(targetName, true);
    		messageSender(ChatColor.GREEN + targetName + ChatColor.WHITE + "'s title have been " + ChatColor.GOLD + "locked" , sender);
    		return true;
    		
    	} else if (subCommand.equalsIgnoreCase("unlock")) {
    		if (player != null && !player.hasPermission("hollowTitles.admin.customTitles.toggleLock")) {
    			messageSender(ChatColor.RED + "You don't have permission for that command!", sender);
                return true;
            }
    		
    		_plugin.getTool().setPlayerTitleLockState(targetName, false);
    		messageSender(ChatColor.GREEN + targetName + ChatColor.WHITE + "'s title have been " + ChatColor.GOLD + "unlocked" , sender);
    		return true;
    		
    	} else {
    		messageSender(ChatColor.RED + "Player Custom title command issued without valid argument!", sender);
    		messageSender(ChatColor.RED + "Usage: " + ChatColor.GOLD + "/title player" + ChatColor.ITALIC + " [add|remove|list]", sender);
    		return true;
    	}
		
	}
	
	private String _getCustomTitleFromNumber(String targetName, int index) {
		Player target = _plugin.getServer().getPlayer(targetName);
		if (target == null) {
			return null;
		}
		List<String> titleList = _plugin.getTool().getAvailableTitles(target);
        titleList.addAll(_plugin.getTool().getCustomTitles(targetName));
        return titleList.get(index);
	}

}
