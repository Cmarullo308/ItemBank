package me.ItemBank.main;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class CommandHandler {
	ItemBank plugin;

	public CommandHandler(ItemBank plugin) {
		this.plugin = plugin;
	}

	public boolean command(CommandSender sender, Command command, String label, String[] args) {

		if (args.length == 0) {
			return false;
		}

		switch (args[0].toLowerCase()) {
		case "test":
			testCommand(sender, args);
			break;
		default:
			sender.sendMessage(ChatColor.RED + "Invalid arguements");
			break;
		}

		return true;
	}

	private void testCommand(CommandSender sender, String[] args) {
		sender.sendMessage("TestMethod");
		Player player = (Player) sender;

		if (args[1].equalsIgnoreCase("add")) {
			Material itemType = player.getInventory().getItemInMainHand().getType();

			if (plugin.bankItemsData.bankItemData.get(itemType) == null) {
				plugin.bankItemsData.bankItemData.put(itemType, new BankItem(itemType, plugin));
				plugin.bankItemsData.bankItemData.get(itemType).accountAmounts.put(player.getUniqueId(),
						player.getInventory().getItemInMainHand().getAmount());
			} else {
				plugin.bankItemsData.bankItemData.get(itemType).accountAmounts.put(player.getUniqueId(),
						player.getInventory().getItemInMainHand().getAmount());
			}

			plugin.bankItemsData.saveBankData();
			// ---------------------------------------------------------------
		} else if (args[1].equalsIgnoreCase("ga")) {
			for (BankItem BI : plugin.bankItemsData.bankItemData.values()) {
				plugin.consoleMessage(BI.material + " : " + BI.accountAmounts.toString());
			}
		} else if (args[1].equalsIgnoreCase("hm")) {
			player.sendMessage(plugin.bankItemsData.bankItemData
					.get(player.getInventory().getItemInMainHand().getType()).accountAmounts.get(player.getUniqueId())
							.toString());
		}

	}
}
