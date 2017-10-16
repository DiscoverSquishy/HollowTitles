package com.hollowPlugins.HollowTitles;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.hollowPlugins.HollowChat.ChannelPlayerChatEvent;
import com.hollowPlugins.HollowPluginBase.HollowPluginBase;
import com.hollowPlugins.HollowPluginBase.HollowPluginBaseListener;

public class HollowTitlesListener extends HollowPluginBaseListener {

	private HollowTitles _plugin;

	public HollowTitlesListener(HollowPluginBase plugin) {
		super(plugin);
		_plugin = (HollowTitles)plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGH)
    public void onPlayerChat(ChannelPlayerChatEvent event) {
		if (event.isCancelled()) {
            return;
        }
		if (!(event instanceof ChannelPlayerChatEvent)) {
			return;
		}
		
        Player player = event.getPlayer();

        String format = event.getFormat();
        //if (_plugin.getTool().getCurrentTitle(player.getName()) != null) {
        	 String newFormat = _plugin.getTool().formatMessage(player);
             format = format.replace("%1$s", newFormat + "%1$s");
        /*} else {
        	format = format.replace("<%1$s>", "%1$s");
        }*/
        event.setFormat(format);
    }
	
	 @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        _plugin.getTool().checkPlayer(event.getPlayer());
    }

}
