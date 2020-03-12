package me.ItemBank.main;

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

		plugin.bank.bankMenus.amountMenu.openMenuFor(player, Material.APPLE);

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
