package me.ItemBank.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class ListOfItemsMenu {
	ItemBank plugin;

	Inventory inventory;
	String menuName;
	ItemStack[] menuButtons;
	ItemStack previousPageButtonIcon;
	ItemStack nextPageButtonIcon;
	ItemStack exitButtonIcon;
	ItemStack backButtonIcon;
	ItemStack backgroundIcon;

	public ListOfItemsMenu(ItemBank plugin, String menuName) {
		this.plugin = plugin;
		this.menuName = ChatColor.BLUE + menuName;
	}

	public void setup() {
		BankMenus bankMenus = plugin.bank.bankMenus;
		ArrayList<String> lore = new ArrayList<String>();

		lore.add(ChatColor.BLUE + "~ItemBank~");
		backButtonIcon = bankMenus.makeButton(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back", lore);
		exitButtonIcon = bankMenus.makeButton(Material.BARRIER, ChatColor.RED + "Exit", lore);
		previousPageButtonIcon = bankMenus.makeButton(Material.LIME_STAINED_GLASS_PANE,
				ChatColor.GOLD + "Previous Page", lore);
		nextPageButtonIcon = bankMenus.makeButton(Material.LIME_STAINED_GLASS_PANE, ChatColor.GOLD + "Next Page", lore);
		backgroundIcon = bankMenus.makeButton(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ", lore);

		menuButtons = new ItemStack[54];
		for (int slotNum = 45; slotNum < 54; slotNum++) {
			switch (slotNum) {
			case 45:
				menuButtons[slotNum] = backButtonIcon.clone();
				break;
			case 47:
				menuButtons[slotNum] = previousPageButtonIcon.clone();
				break;
			case 49:
				menuButtons[slotNum] = exitButtonIcon.clone();
				break;
			case 51:
				menuButtons[slotNum] = nextPageButtonIcon.clone();
				break;
			default:
				menuButtons[slotNum] = backgroundIcon.clone();
				break;
			}
		}
	}

	public void openMenuFor(Player player, ItemStack[] items) {
		inventory = Bukkit.createInventory(player, 54, menuName);
		for (int i = 0; i < 45; i++) {
			if (items[i] != null) {
				menuButtons[i] = items[i].clone();
			}
		}

		inventory.setContents(menuButtons);

		player.openInventory(inventory);
	}
}
