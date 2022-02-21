package me.ItemBank.main;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import SpigotTools.ItemStackEditor;
import me.ItemBank.main.Session.ACCOUNT;

public class DepositOrWithdrawMenu {
	ItemBank plugin;

	Inventory inventory;
	String menuName;
	ItemStack[] menuButtons;
	ItemStack depositButtonIcon;
	ItemStack withdrawButtonIcon;
	ItemStack backButtonIcon;
	ItemStack exitButtonIcon;
	ItemStack backgroundIcon;

	public DepositOrWithdrawMenu(ItemBank plugin, String menuName) {
		this.plugin = plugin;
		this.menuName = ChatColor.BLUE + menuName;
	}

	public void setup() {
		BankMenus bankMenus = plugin.bank.bankMenus;
		ArrayList<String> lore = new ArrayList<String>();

		lore.add("Click to deposit items");
		depositButtonIcon = bankMenus.makeButton(Material.HOPPER, ChatColor.GOLD + "Deposit", lore);

		lore.set(0, "Click to withdraw items");
		withdrawButtonIcon = bankMenus.makeButton(Material.HOPPER, ChatColor.GOLD + "Withdraw", lore);
		backButtonIcon = bankMenus.makeButton(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back");
		exitButtonIcon = bankMenus.makeButton(Material.BARRIER, ChatColor.RED + "Exit");
		backgroundIcon = bankMenus.makeButton(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ");

		menuButtons = new ItemStack[18];
		for (int i = 0; i < 18; i++) {
			switch (i) {
			case 2:
				menuButtons[i] = depositButtonIcon.clone();
				break;
			case 6:
				menuButtons[i] = withdrawButtonIcon.clone();
				break;
			case 9:
				menuButtons[i] = backButtonIcon.clone();
				break;
			case 13:
				menuButtons[i] = exitButtonIcon.clone();
				break;
			default:
				menuButtons[i] = backgroundIcon.clone();
				break;
			}
		}

	}

	public void openMenuFor(Player player) {
		Session session = plugin.bank.sessions.get(player);
		UUID id;
		if (session.getAccount() == ACCOUNT.GLOBAL) {
			id = plugin.bank.globalUUID;
		} else {
			id = player.getUniqueId();
		}

		long totalNumberOfItemsInAccount = 0;

		for (BankItem bankItem : plugin.bank.bankItemsData.bankItemData.values()) {
			if (bankItem.accountAmounts.containsKey(id)) {
				totalNumberOfItemsInAccount += bankItem.accountAmounts.get(id);
			}
		}

		String withrawButtonOriginalName = menuButtons[6].getItemMeta().getDisplayName();
		ItemStackEditor.setDisplayName(menuButtons[6], menuButtons[6].getItemMeta().getDisplayName() + " ("
				+ plugin.decimalFormat.format(totalNumberOfItemsInAccount) + " Items)");

		inventory = Bukkit.createInventory(player, 18, menuName);
		inventory.setContents(menuButtons);

		player.openInventory(inventory);

		ItemStackEditor.setDisplayName(menuButtons[6], withrawButtonOriginalName);
	}
}
