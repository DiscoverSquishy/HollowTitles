package com.hollowPlugins.HollowTitles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.hollowPlugins.utils.TextFormatHelper;

public class HollowTitlesTool {

	private static final String EMPTY = null;
	public final String CUSTOM_GROUP = "CustomTitle";
	private HollowTitles _plugin;
	private HashMap<String, GroupData> _groups;

	public HollowTitlesTool(HollowTitles plugin) {
		this._plugin = plugin;
	
		
		init();
	}

	private void init() {
		loadTitles();
	}
	
	public void reload() {
		loadTitles();
	}

	public void loadTitles() {
		Set<String> groupsConf = _plugin.conf.getConfigurationSection("Groups").getKeys(false);
		
		_groups = new HashMap<String, GroupData>();
		
		GroupData newGroup;
		for (String groupName : groupsConf) {
			newGroup = new GroupData(groupName, _plugin.conf.getInt("Groups." + groupName + ".Rank", 0), 
					_plugin.conf.getString("Groups." + groupName + ".Format", null), _plugin.conf.getStringList("Groups." + groupName + ".Titles"));
			_groups.put(groupName, newGroup);
		}
		
		checkOnlinePlayers();
	}
	
	/**
	 * PLAYER DATA
	 */

	private void checkOnlinePlayers() {
		Collection<? extends Player> onlinePlayers = _plugin.getServer().getOnlinePlayers();
		for (Player tempPlayer : onlinePlayers) {
			checkPlayer(tempPlayer);
		}
	}
	
	// TODO > Do we really need that?
	public void checkPlayer(Player player) {
        ArrayList<GroupData> allowedGroups = _getPlayerGroups(player);

        boolean ok = false;

        String playerGroup = _plugin.conf.getString(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".Group", EMPTY);
        String currentTitle = _plugin.conf.getString(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".Title", EMPTY);

        // Check for normal titles
        if (playerGroup != null && currentTitle != null) {
        	if (_isPlayerAllowedInGroup(playerGroup, allowedGroups)) {
        		if (_isValidTitleForGroups(currentTitle, allowedGroups)) {
        			ok = true;
        		}
        	}
        	
        }
        
        // Check for custom titles
        List<String> playerTitles = getCustomTitles(player.getName());
		for (String tempTitle : playerTitles) {
		    if (tempTitle.equalsIgnoreCase(currentTitle)) {
		        ok = true;
		        break;
		    }
		}

        if (!ok) {
        	_resetPlayerData(player);
        }

        String format = getFormat(player);
        String confFormat = _plugin.conf.getString(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".Format", null);
        if (confFormat != null && !confFormat.equalsIgnoreCase(format)) {
        	_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".Format", null);
        }
        
        if (_isDataEmpty(player)) {
        	_clearPlayerData(player);
        }
    }
	
	private void _clearPlayerData(Player player) {
		_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase(), null);
	}

	
	/*Title
    Group
    Format
    PreviousTitle
    CustomTitles:*/
	
	private boolean _isDataEmpty(Player player) {
		if (_plugin.conf.getString(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".Group", null) != null) {
			return false;
		}
		if (_plugin.conf.getString(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".Title", null) != null) {
			return false;
		}
		if (_plugin.conf.getString(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".PreviousTitle", null) != null) {
			return false;
		}
		if (_plugin.conf.getString(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".Format", null) != null) {
			return false;
		}
		if (!_plugin.conf.getStringList(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".CustomTitles").isEmpty()) {
			return false;
		}
		return true;
	}

	private void _resetPlayerData(Player player) {
		_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".Group", null);
		_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + player.getName().toLowerCase() + ".Title", null);
	}
	
	public void setTitle(String playerName, String group, String format, String title, boolean setPrevious) {
		String currentTitle = getCurrentTitle(playerName);
    	if (setPrevious) {
    		if (currentTitle != null) {
    			if (!title.equalsIgnoreCase(currentTitle)) {
	    			_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".PreviousTitle", currentTitle);
	    		}
    		}
    	}
    	
    	_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".Group", group);
    	_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".Title", title);
    	_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".Format", format);
    	
    }
	
	public void clearTitle(String playerName) {
		_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".Group", null);
    	_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".Title", null);
    }
	
	public List<String> getCustomTitles(String playerName) {
		return _plugin.conf.getStringList(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".CustomTitles");
	}
	
	public String getCurrentTitle(String playerName) {
        return _plugin.conf.getString(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".Title", null);
    }
	
	public String getPreviousTitle(String playerName) {
        return _plugin.conf.getString(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".PreviousTitle", null);
    }
	
	public String getCurrentGroup(String playerName) {
        return _plugin.conf.getString(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".Group", null);
    }
	
	public boolean getPlayerTitleLockState(String playerName) {
		return _plugin.conf.getBoolean("Players." + playerName.toLowerCase() + ".Locked", false);
	}
	
	public void setPlayerTitleLockState(String playerName, boolean locked) {
		_plugin.conf.set("Players." + playerName.toLowerCase() + ".Locked", locked);
	}
	

	/**
	 * FORMAT MANAGEMENT
	 */
	
	 public String getFormat(Player player) {
        if (player == null) return null;
        
        String template = "";
        
        int rank = -1;
        for (GroupData group : _groups.values()) {
        	if (player.hasPermission("hollowTitles.group." + group.getName())) {
                if (group.getRank() != -1) {
                    if (group.getRank() > rank) {
                    	template = group.getFormat();
                    	rank = group.getRank();
                    }
                }
                
        	}
		}
        
        if (template.equalsIgnoreCase("")) {
        	template = null;
        }
        return template;
    }
	 
	 public String formatMessage(Player player) {
		 
        String title = getCurrentTitle(player.getName());
        String format = getFormat(player);

        if (format == null) {
            checkPlayer(player);
            format = getFormat(player);
            if (format == null) {
                format = getDefaultFormat();
            }
        }
        if (title == null) {
            title = getDefaultTitle();
        }

        format = format.replace("@", title);
        format = format.replace("&", "\u00A7");
        return format;
    }
	
	
	/**
	 * TITLE & GROUPS CHECKING
	 */
	
	private boolean _isValidTitleForGroups(String playerTitle, ArrayList<GroupData> allowedGroups) {
		for (GroupData groupData : allowedGroups) {
			if (groupData.containsTitle(playerTitle)) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<GroupData> _getPlayerGroups(Player player) {
		ArrayList<GroupData> result = new ArrayList<GroupData>();
		
		 for (String groupName : _groups.keySet()) {
             if (player.hasPermission("hollowTitles.group." + groupName)) {
            	 result.add(_groups.get(groupName));
             }
		 }
		
		return result;
	}

	private boolean _isPlayerAllowedInGroup(String playerGroup, ArrayList<GroupData> allowedGroups) {
		for (GroupData groupData : allowedGroups) {
			if (groupData.getName().equalsIgnoreCase(playerGroup)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<String> getTitlesFromGroups(ArrayList<GroupData> groups) {
		ArrayList<String> result = new ArrayList<String>();
		
		for (GroupData group : groups) {
			result.addAll(group.getTitles());
		}
		
		return result;
	}
	
	public List<String> getAvailableTitles(Player player) {
		return getTitlesFromGroups(_getPlayerGroups(player));
	}
	
	/**
	 * TITLES MANAGEMENT
	 */
	
	public boolean addTitle(String groupName, String newTitle) {
        if (_groups.containsKey(groupName)) {
        	GroupData group = _groups.get(groupName);
        	if (!group.containsTitle(newTitle)) {
        		group.addTitle(newTitle);
        		_plugin.conf.set("Groups." + group.getName() + ".Titles", group.getTitles());
        		return true;
        	}
        }
        return false;
    }

	public boolean removeTitle(String groupName, String oldTitle) {
		if (_groups.containsKey(groupName)) {
			GroupData group = _groups.get(groupName);
			if (group.containsTitle(oldTitle)) {
				group.removeTitle(oldTitle);
				_plugin.conf.set("Groups." + group.getName() + ".Titles", group.getTitles());
        		return true;
			}
		} 
        return false;
    }
	
	
	/**
	 * CUSTOM TITLES MANAGEMENT
	 */
	
	public boolean addCustomTitle(String playerName, String newTitle) {
		List<String> playerTitles = _plugin.conf.getStringList(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".CustomTitles");
		if(!playerTitles.contains(newTitle)) {
			playerTitles.add(newTitle);
			_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".CustomTitles", playerTitles);
			return true;
		}
        return false;
	}
	
	public boolean removeCustomTitle(String playerName, String oldTitle) {
		List<String> playerTitles = _plugin.conf.getStringList(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".CustomTitles");
		if(playerTitles.contains(oldTitle)) {
			playerTitles.remove(oldTitle);
			_plugin.conf.set(_plugin.PLAYERS_CONFIG, "Players." + playerName.toLowerCase() + ".CustomTitles", playerTitles);
			
			String currentTitle = getCurrentTitle(playerName);
			if (currentTitle != null && currentTitle.equalsIgnoreCase(oldTitle)) {
				String previousTitle = getPreviousTitle(playerName);
				/*if (previousTitle == null) {
            		previousTitle = getDefaultTitle();
            	}*/
            	setTitle(playerName, getCurrentGroup(playerName), getDefaultFormat(), previousTitle, false);
            	
            	Player target = _plugin.getServer().getPlayer(playerName);
            	if (target != null && target.isOnline()) {
            		target.sendMessage(TextFormatHelper.format(_plugin.conf.getSentence("titleChange"), "[title]", previousTitle));
            	}
			}
			return true;
		}
		return false;
	}

	
	
	public String getDefaultTitle() {
        return _plugin.conf.getString("DefaultTitle", "---");
    }
	
	public String getDefaultFormat() {
        return _plugin.conf.getString("DefaultFormat", "<%1$s> %2$s");
    }
	
	public int getListWidth() {
        return _plugin.conf.getInt("ListWidth", 4) - 1;
    }

    public int getListHeight() {
        return _plugin.conf.getInt("ListHeight", 5) - 1;
    }
    
    public int getCustomTitlesMaxLength() {
		return _plugin.conf.getInt("CustomTitleMaxLength", 20);
	}

	public GroupData getGroupFor(String newTitle) {
		for (GroupData group : _groups.values()) {
			if (group.containsTitle(newTitle)) {
				return group;
			}
		}
		return null;
	}
	
	public HashMap<String, GroupData> getGroups() {
		return _groups;
	}
	
	/**
     * Finds and returns a list of titles containing the searched text.
     * @param searching
     * @param player
     * @return
     */
    public ArrayList<String> findTitles(String searching, List<String> titleList, int normalTitlesCount) {
        ArrayList<String> result = new ArrayList<>();
        
        for (int i = 0; i < titleList.size(); i++) {
            if (titleList.get(i).toLowerCase().contains(searching)) {
            	if (i < normalTitlesCount) {
            		result.add("" + ChatColor.WHITE + i + ": " + ChatColor.YELLOW + titleList.get(i));
            	} else {
            		result.add("" + ChatColor.WHITE + i + ": " + ChatColor.GOLD + titleList.get(i));
            	}
            }

        }
        
		return result;
	}
    
    public int findNumberfromText(String searching, List<String> titleList) {
		
    	for (int i = 0; i < titleList.size(); i++) {
        	if (titleList.get(i).toLowerCase().equals(searching)) {
        		return i;
        	}
        }
		return -1;
	}
	
}
