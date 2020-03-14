package me.ItemBank.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class BankMainMenu {
	ItemBank plugin;

	Inventory inventory;
	String menuName;
	ItemStack[] menuButtons;
	ItemStack publicAccountButtonIcon;
	ItemStack privateAccountButtonIcon;
	ItemStack backgroundIcon;
	ItemStack exitIcon;

	public BankMainMenu(ItemBank plugin, String menuName) {
		this.plugin = plugin;

		this.menuName = ChatColor.BLUE + menuName;
	}

	public void setup() {
		BankMenus bankMenus = plugin.bank.bankMenus;

		ArrayList<String> lore = new ArrayList<String>();

		lore.add("Items shared with everyone");
		publicAccountButtonIcon = bankMenus.makeButton(Material.GOLD_INGOT, ChatColor.GOLD + "Public account", lore);
		lore.clear();

		lore.add("Your private account");
		privateAccountButtonIcon = bankMenus.makeButton(Material.IRON_INGOT, ChatColor.GOLD + "Private account", lore);
		lore.clear();

		lore.add(" ");
		backgroundIcon = bankMenus.makeButton(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ", lore);
		lore.clear();

		exitIcon = bankMenus.makeButton(Material.BARRIER, ChatColor.RED + "Exit");

		menuButtons = new ItemStack[18];
		for (int i = 0; i < 18; i++) {
			switch (i) {
			case 2:
				menuButtons[i] = publicAccountButtonIcon.clone();
				break;
			case 6:
				menuButtons[i] = privateAccountButtonIcon.clone();
				break;
			case 13:
				menuButtons[i] = exitIcon.clone();
				break;
			default:
				menuButtons[i] = backgroundIcon.clone();
				break;
			}
		}
	}

	public void openMenuFor(Player player) {
		inventory = Bukkit.createInventory(player, 18, menuName);
		inventory.setContents(menuButtons);

		player.openInventory(inventory);
	}
}
