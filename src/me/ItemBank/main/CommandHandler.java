package me.ItemBank.main;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandHandler {
	ItemBank plugin;

	public CommandHandler(ItemBank plugin) {
		this.plugin = plugin;
	}

	public boolean command(CommandSender sender, Command command, String label, String[] args) {

		switch (command.getName().toLowerCase()) {
		case "createbank":
			createBank(sender, args);
			break;
		case "openbank":
			openBankForPlayer(sender, args);
			break;
		case "help":
			if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
				testCommand(sender, args);
			} else {
				sender.sendMessage(plugin.helpMessage);
			}
			break;
		case "reloadconfig":
			plugin.reloadConfig();
			sender.sendMessage(ChatColor.GREEN + "Item Bank Config Reloaded");
			break;
		default:
			sender.sendMessage(ChatColor.RED + "Invalid arguements");
			break;
		}

		return true;
	}

	private void openBankForPlayer(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(mustBeAPlayerMessage());
			return;
		}

		if (!sender.hasPermission("itembank.openbank") && !sender.isOp()) {
			noPermission(sender);
			return;
		}

		if (!plugin.getConfig().getBoolean("allow-open-bank-command")) {
			sender.sendMessage(ChatColor.RED + "That command is disabled");
			return;
		}

		plugin.bank.openBank((Player) sender);
	}

	private void noPermission(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
	}

	private void createBank(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(mustBeAPlayerMessage());
			return;
		}

		if (!sender.hasPermission("itembank.createbank") && !sender.isOp()) {
			noPermission(sender);
			return;
		}

		if (args.length == 0) {
			plugin.bank.createBank((Player) sender, 0);
		} else if (args.length == 1) {
			switch (args[0].toLowerCase()) {
			case "front":
				plugin.bank.createBank((Player) sender, 0);
				break;
			case "back":
				plugin.bank.createBank((Player) sender, 1);
				break;
			case "both":
				plugin.bank.createBank((Player) sender, 2);
				break;
			default:
				sender.sendMessage(ChatColor.RED + "Invalid arguement");
				break;
			}
		}
	}

	private String mustBeAPlayerMessage() {
		return ChatColor.RED + "Must be a player to run this command";
	}

	private void testCommand(CommandSender sender, String[] args) {
		sender.sendMessage("TestMethod");
		Player player = (Player) sender;

		ItemStack item = new ItemStack(Material.DIAMOND);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Shit");
		lore.add("Fuck");
		meta.setLore(lore);
		item.setItemMeta(meta);

		player.getInventory().addItem(item);

		if (args.length > 1) {
			if (args[1].equalsIgnoreCase("add")) {
				Material itemType = player.getInventory().getItemInMainHand().getType();

				if (plugin.bank.bankItemsData.bankItemData.get(itemType) == null) {
					plugin.bank.bankItemsData.bankItemData.put(itemType, new BankItem(itemType, plugin));
					plugin.bank.bankItemsData.bankItemData.get(itemType).accountAmounts.put(player.getUniqueId(),
							player.getInventory().getItemInMainHand().getAmount());
				} else {
					plugin.bank.bankItemsData.bankItemData.get(itemType).accountAmounts.put(player.getUniqueId(),
							player.getInventory().getItemInMainHand().getAmount());
				}

				plugin.bank.bankItemsData.saveBankData();
				// ---------------------------------------------------------------
			} else if (args[1].equalsIgnoreCase("ga")) {
				for (BankItem BI : plugin.bank.bankItemsData.bankItemData.values()) {
					plugin.consoleMessage(BI.material + " : " + BI.accountAmounts.toString());
				}
			} else if (args[1].equalsIgnoreCase("hm")) {
				player.sendMessage(plugin.bank.bankItemsData.bankItemData
						.get(player.getInventory().getItemInMainHand().getType()).accountAmounts
						.get(player.getUniqueId()).toString());
			}
		}

	}
}
