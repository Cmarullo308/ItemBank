package me.ItemBank.main;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CommandHandler {
	ItemBank plugin;

	public CommandHandler(ItemBank plugin) {
		this.plugin = plugin;
	}

	public boolean command(CommandSender sender, Command command, String label, String[] args) {

		switch (command.getLabel().toLowerCase()) {
		case "createbank":
			createBank(sender, args);
			break;
		case "help":
			if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
				testCommand(sender, args);
			} else {
				sender.sendMessage(plugin.helpMessage);
			}
			break;
		default:
			sender.sendMessage(ChatColor.RED + "Invalid arguements");
			break;
		}

		return true;
	}

	private void createBank(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(mustBeAPlayerMessage());
			return;
		}

		plugin.bank.createBank((Player) sender);
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
