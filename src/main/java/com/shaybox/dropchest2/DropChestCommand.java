package com.shaybox.dropchest2;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class DropChestCommand implements CommandExecutor {
	private DropChest plugin;

	DropChestCommand(DropChest plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You are not a player");
			return true;
		}

		Player player = (Player) sender;
		DropChestPlayer dPlayer = DropChestPlayer.getPlayer(player.getUniqueId());

		//noinspection SwitchStatementWithTooFewBranches
		switch (cmd.getName().toLowerCase()) {
			case "dropchest": {
				if (!player.hasPermission("dropchest")) {
					player.sendMessage(GOLD + "You do not have permissions to use DropChest");
					return true;
				}

				if (args.length == 0) {
					String maxRange = String.valueOf(plugin.getConfig().getInt("max-radius"));
					String range = player.hasPermission("dropchest.setradius.big") ? "unlimited" : maxRange;
					player.sendMessage(GOLD + "<> Required | () Optional");
					player.sendMessage(GOLD + "Your max radius is " + range);
					if (player.hasPermission("dropchest.add"))
						player.sendMessage(YELLOW + "/dropchest add (radius)");
					if (player.hasPermission("dropchest.remove"))
						player.sendMessage(YELLOW + "/dropchest remove");
					if (player.hasPermission("dropchest.setradius"))
						player.sendMessage(YELLOW + "/dropchest radius <radius>");
					if (player.hasPermission("dropchest.setowner"))
						player.sendMessage(YELLOW + "/dropchest owner <player>");
					if (player.hasPermission("dropchest.info"))
						player.sendMessage(YELLOW + "/dropchest info");
					//                    if (player.hasPermission("dropchest.filter")) player.sendMessage("/dropchest filter {suck|push|pull} [{chest} {itemid|itemtype|clear|all}]");
					return true;
				}

				switch (args[0].toLowerCase()) {
					case "add": {
						if (!player.hasPermission("dropchest.add")) {
							player.sendMessage(GOLD + "You may not create containers");
							return true;
						}

						int radius = plugin.getConfig().getInt("default-radius");
						if (args.length == 2 && player.hasPermission("dropchest.setradius")) {
							radius = Integer.valueOf(args[1]);
							int maxRadius = plugin.getConfig().getInt("max-radius");
							if (radius > maxRadius && !player.hasPermission("dropchest.setradius.big"))
								radius = maxRadius;
						}
						dPlayer.setRadius(radius);
						dPlayer.setAction("ADD");
						player.sendMessage(GREEN + "Now rightclick on the container that you want to add");
						return true;
					}
					case "remove": {
						if (!player.hasPermission("dropchest.remove")) {
							player.sendMessage(GOLD + "You may not remove containers");
							return true;
						}

						dPlayer.setAction("REMOVE");
						player.sendMessage(GREEN + "Now rightclick on the container that you want to remove");
						return true;
					}
					case "radius": {
						if (!player.hasPermission("dropchest.setradius")) {
							player.sendMessage("You may not set the radius.");
							return true;
						}

						if (args.length < 2) {
							player.sendMessage(GOLD + "Please specify a radius");
							player.sendMessage(GOLD + "/dropchest setradius <radius>");
							return true;
						}

						int radius = Integer.valueOf(args[1]);
						int maxRadius = plugin.getConfig().getInt("max-radius");
						if (radius > maxRadius && !player.hasPermission("dropchest.setradius.big")) radius = maxRadius;
						dPlayer.setRadius(radius);
						dPlayer.setAction("RADIUS");
						player.sendMessage(GREEN + "Now rightclick on the container to change the radois");
						return true;
					}
					case "owner": {
						if (!player.hasPermission("dropchest.setowner")) {
							player.sendMessage("You may not set the owner.");
							return true;
						}

						if (args.length < 2) {
							player.sendMessage(GOLD + "Please specify a player");
							player.sendMessage(GOLD + "/dropchest setowner <player>");
							return true;
						}

						Player newOwner = plugin.getServer().getPlayer(args[1]);
						if (newOwner == null) {
							player.sendMessage(GOLD + "That player does not exist.");
							return true;
						}

						dPlayer.setOwner(newOwner);
						dPlayer.setAction("OWNER");
						player.sendMessage(GREEN + "Now rightclick on the container to change the owner");
						return true;
					}
					case "info": {
						if (!player.hasPermission("dropchest.which")) {
							player.sendMessage(GOLD + "You may not use this command.");
							return true;
						}

						dPlayer.setAction("INFO");
						player.sendMessage(GREEN + "Now rightclick on the container for info");
						return true;
					}
					//                        case "setdelay": {
					//                            /*****************
					//                             *   SETDELAY   *
					//                             *****************/
					//                            if (!hasPermission(player, "dropchest.delay")) {
					//                                player.sendMessage("You may not set the delay of a DropChest.");
					//                                return true;
					//                            }
					//                            if (args.length == 3) {
					//                                int delay = Integer.valueOf(args[2]);
					//                                delay = (delay > 0 ? delay : 0); //Derp derp, I'll set a negative delay! Nope.
					//                                DropChestItem dci = getChestByIdOrName(args[1]);
					//                                if (dci != null) {
					//                                    if (ownsChest(dci, player)) {
					//                                        dci.setDelay(delay);
					//                                        player.sendMessage(GREEN + "Delay of Chest '" + dci.getName() + "' set to " + dci.getDelay() + " milliseconds.");
					//                                        save();
					//                                    } else {
					//                                        player.sendMessage("That's not your chest.");
					//                                    }
					//                                } else {
					//                                    syntaxerror = true;
					//                                }
					//                            } else {
					//                                syntaxerror = true;
					//                            }
					//                            return true;
					//                        }
					//                        case "filter": {
					//                            /*****************
					//                             *	 FILTER	*
					//                             *****************/
					//                            if (!hasPermission(player, "dropchest.filter")) {
					//                                player.sendMessage("You may not use DropChest filters!");
					//                                return true;
					//                            }
					//                            //dropchest filter {suck|push|pull|finish} [{chestid} {itemid|itemtype|clear}]
					//                            if (args.length >= 2) {
					//                                String typestring = args[1];
					//                                FilterType type = null;
					//                                try {
					//                                    type = FilterType.valueOf(typestring.toUpperCase());
					//                                } catch (IllegalArgumentException e) {
					//                                    type = null;
					//                                }
					//                                if (type != null) {
					//                                    if (args.length == 2 && dplayer != null) {
					//                                        dplayer.setEditingFilter(true);
					//                                        dplayer.setEditingFilterType(type);
					//                                        dplayer.getOwner().sendMessage(GREEN + "You're now entering interactive mode for filtering " + type.toString().toLowerCase() + "ed items");
					//                                    } else if (dplayer == null && args.length == 2) {
					//                                        player.sendMessage("You can't use interactive mode from a console!");
					//                                    } else if (args.length == 4) {
					//                                        String itemstring = args[3];
					//                                        DropChestItem chest = getChestByIdOrName(args[2]);
					//                                        Material item = null;
					//                                        if (itemstring.equalsIgnoreCase("clear")) {
					//                                            if (ownsChest(chest, player)) {
					//                                                chest.getFilter(type).clear();
					//                                                player.sendMessage(GREEN + "Filter cleared.");
					//                                            } else {
					//                                                player.sendMessage("That's not your chest.");
					//                                            }
					//                                        } else if (itemstring.equalsIgnoreCase("all")) {
					//                                            if (chest != null && ownsChest(chest, player)) {
					//                                                List<Material> filter = chest.getFilter(type);
					//                                                for (Material m : Material.values()) {
					//                                                    if (!m.equals(Material.AIR)) {
					//                                                        if (!filter.contains(m)) {
					//                                                            filter.add(m);
					//                                                        }
					//                                                    }
					//                                                }
					//                                                player.sendMessage(GREEN + "All items set.");
					//                                            }
					//                                        } else {
					//                                            if (chest != null) {
					//                                                if (ownsChest(chest, player)) {
					//                                                    try {
					//                                                        item = Material.valueOf(itemstring.toUpperCase());
					//                                                    } catch (IllegalArgumentException e) {
					//                                                        item = null;
					//                                                    }
					//                                                    boolean materialNotFound = false;
					//                                                    if (item == null) {
					//                                                        Integer itemid = null;
					//                                                        try {
					//                                                            itemid = Integer.valueOf(itemstring);
					//                                                        } catch (NumberFormatException e) {
					//                                                            itemid = null;
					//                                                        }
					//                                                        if (itemid != null) {
					//                                                            item = Material.getMaterial(itemid);
					//                                                            if (item == null) {
					//                                                                materialNotFound = true;
					//                                                            }
					//                                                        } else {
					//                                                            materialNotFound = true;
					//                                                        }
					//                                                    }
					//                                                    if (!materialNotFound) {
					//                                                        List<Material> filter = chest.getFilter(type);
					//                                                        if (filter.contains(item)) {
					//                                                            filter.remove(item);
					//                                                            player.sendMessage(GREEN + item.toString() + " is no more being " + type.toString().toLowerCase() + "ed.");
					//                                                        } else {
					//                                                            filter.add(item);
					//                                                            player.sendMessage(GREEN + item.toString() + " is now being " + type.toString().toLowerCase() + "ed.");
					//                                                        }
					//                                                    } else {
					//                                                        player.sendMessage("Material " + itemstring + " not found.");
					//                                                    }
					//                                                    save();
					//                                                } else {
					//                                                    player.sendMessage("That's not your chest.");
					//                                                }
					//
					//
					//                                            } else {
					//                                                log.log(Level.INFO, "No such chest " + args[1] + ".");
					//                                                syntaxerror = true;
					//                                            }
					//                                        }
					//                                    } else {
					//                                        log.log(Level.INFO, "Too much arguments.");
					//                                        syntaxerror = true;
					//                                    }
					//                                } else if (typestring.equalsIgnoreCase("finish")) {
					//                                    if (dplayer != null) {
					//                                        dplayer.setEditingFilter(false);
					//                                        dplayer.getOwner().sendMessage(GREEN + "You're now leaving interactive mode!");
					//                                    } else {
					//                                        player.sendMessage("You can't use interactive mode from a console!");
					//                                    }
					//                                } else {
					//                                    log.log(Level.INFO, "Filter type not found.");
					//                                    syntaxerror = true;
					//                                }
					//                            }
					//                            return true;
					//                        }
					//                        case "protect": {
					//                            if (args.length == 3) {
					//                                String cheststring = args[1];
					//                                String mode = args[2];
					//                                DropChestItem item = getChestByIdOrName(cheststring);
					//                                if (item == null) {
					//                                    player.sendMessage("This chest does not exist.");
					//                                    return true;
					//                                }
					//                                if (!config.isLetUsersProtectChests()) {
					//                                    player.sendMessage("Chest protection is disabled on this server.");
					//                                    return true;
					//                                }
					//                                boolean mayProtect = true;
					//                                if (player instanceof Player) {
					//                                    mayProtect = hasPermission((Player) player, "dropchest.protect");
					//                                }
					//                                if (ownsChest(item, player) && mayProtect) {
					//                                    if (mode.equalsIgnoreCase("off")) {
					//                                        item.setProtect(false);
					//                                        player.sendMessage("Chest is not anymore protected.");
					//                                    } else if (mode.equalsIgnoreCase("on")) {
					//                                        item.setProtect(true);
					//                                        player.sendMessage("Chest is now protected.");
					//                                    } else {
					//                                        syntaxerror = true;
					//                                    }
					//                                    save();
					//                                } else {
					//                                    player.sendMessage("You may not set this attribute.");
					//                                }
					//                            }
					//                            return true;
					//                        }
				}
			}

			//            case "dcitem": {
			//                if (args.length == 1) {
			//                int id = 0;
			//                Material m = null;
			//                try {
			//                    id = Integer.valueOf(args[0]);
			//                } catch (Exception e) {
			//                    m = Material.matchMaterial(args[0].toUpperCase());
			//                }
			//                if (id != 0) {
			//                    m = Material.getMaterial(id);
			//                    if (m != null) {
			//                        player.sendMessage(YELLOW.toString() + id + WHITE + " is " + YELLOW.toString() + m.toString());
			//                    } else {
			//                        player.sendMessage(RED + "That item does not exist.");
			//                    }
			//                } else {
			//                    if (m != null) {
			//                        id = m.getId();
			//                        player.sendMessage(YELLOW + m.toString() + WHITE + " is " + YELLOW + id);
			//                    } else {
			//                        player.sendMessage(RED + "That item does not exist.");
			//                    }
			//                }
			//                return true;
			//            }
			//                break;
			//            }

			//            case "chestinfo": {
			//                if (!hasPermission(player, "dropchest.moderator")) {
			//                    player.sendMessage("You may not use this command");
			//                    return true;
			//                }
			//                if (args.length == 1) {
			//                    DropChestItem dci = getChestByIdOrName(args[0]);
			//                    if (dci == null) {
			//                        player.sendMessage("This chest doesn't exist.");
			//                        return true;
			//                    }
			//                    String info = chestInformation(dci.getInventory(), "of " + dci.getName());
			//                    if (dplayer != null) {
			//                        dplayer.sendMessage(info);
			//                    } else {
			//                        player.sendMessage(info);
			//                    }
			//                    return true;
			//                } else {
			//                    if (dplayer != null) {
			//                        dplayer.setChestRequestType(ChestRequestType.CHESTINFO);
			//                        dplayer.sendMessage("Rightclick on any chest to get information");
			//                    } else {
			//                        player.sendMessage("You can't use interactive commands from the console");
			//                        return true;
			//                    }
			//                }
			//                break;
			//            }
		}

		player.sendMessage(RED + "Incorrect argument for command");
		return true;
	}
}
