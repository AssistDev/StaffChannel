package main.java.me.avastprods.staffchannel;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public String prefix = ChatColor.BLUE + "[" + ChatColor.RESET + "StaffChannel" + ChatColor.BLUE + "] " + ChatColor.RESET;
	
	ArrayList<String> list = new ArrayList<String>();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		} else {
			Player s = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("sc")) {
				if (args.length == 0) {
					if (s.hasPermission("sc.toggle")) {
						if (!list.contains(s.getName())) {
							list.add(s.getName());
							s.sendMessage(prefix + "StaffChat enabled.");

						} else {
							list.remove(s.getName());
							s.sendMessage(prefix + "StaffChat disabled.");
						}
					} else {
						s.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Insufficient permissions.");
					}
				}

				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("add")) {
						if (s.hasPermission("sc.add")) {
							s.sendMessage(ChatColor.GRAY + "/sc add <player>");
						} else {
							s.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Insufficient permissions.");
						}
					}

					if (args[0].equalsIgnoreCase("kick")) {
						if (s.hasPermission("sc.kick")) {
							s.sendMessage(ChatColor.GRAY + "/sc kick <player>");
						} else {
							s.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Insufficient permissions.");
						}
					}
					
					if(args[0].equalsIgnoreCase("list")) {
						if(s.hasPermission("sc.list")) {
							Object[] theList = list.toArray();
							s.sendMessage(prefix + StringUtils.join(theList, ' ', 0, theList.length));
						} else {
							s.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Insufficient permissions.");
						}
					}
				}

				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("add")) {
						if (s.hasPermission("sc.add")) {
							Player target = Bukkit.getServer().getPlayer(args[1]);
							if (target != null) {
								if (!list.contains(target.getName())) {
									list.add(target.getName());
									s.sendMessage(prefix + "Added player '" + ChatColor.GRAY + target.getDisplayName() + ChatColor.WHITE + "' to the stafflist.");

								} else {
									s.sendMessage(prefix + ChatColor.RED + "This player is already in the staff channel.");
								}
							} else {
								s.sendMessage(prefix + ChatColor.RED + "This player is either not online, or does not exist.");
							}
						} else {
							s.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Insufficient permissions.");
						}
					}

					if (args[0].equalsIgnoreCase("kick")) {
						if (s.hasPermission("sc.kick")) {
							Player target = Bukkit.getServer().getPlayer(args[1]);
							if (target != null) {
								if (list.contains(target.getName())) {
									list.remove(target.getName());
									s.sendMessage(prefix + "Kicked player '" + ChatColor.GRAY + target.getDisplayName() + ChatColor.WHITE + "' from the stafflist.");

								} else {
									s.sendMessage(prefix + ChatColor.RED + "This player is not in the staff channel.");
								}
							} else {
								s.sendMessage(prefix + ChatColor.RED + "This player is either not online, or does not exist.");
							}
						} else {
							s.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Insufficient permissions.");
						}
					}
				}
			}
		}
		return false;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (list.contains(p.getName())) {
			for (Player all : Bukkit.getServer().getOnlinePlayers()) {
				if (list.contains(all.getName())) {
					e.setCancelled(true);
					all.sendMessage(prefix + p.getDisplayName() + ChatColor.GRAY + "> " + ChatColor.RESET + e.getMessage());
				}
			}
		}
	}
}
