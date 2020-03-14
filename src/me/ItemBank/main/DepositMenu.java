package me.ItemBank.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class DepositMenu {
	ItemBank plugin;

	Inventory inventory;
	String menuName;
	ItemStack[] menuButtons;
	ItemStack backButtonIcon;
	ItemStack exitButtonIcon;
	ItemStack moveAllItemsUpButtonIcon;
	ItemStack moveAllItemsDownButtonIcon;
	ItemStack depositButtonIcon;
	ItemStack backgroundIcon;

	public DepositMenu(ItemBank plugin, String menuName) {
		this.plugin = plugin;
		this.menuName = ChatColor.BLUE + menuName;
	}

	public void setup() {
		BankMenus bankMenus = plugin.bank.bankMenus;
		ArrayList<String> lore = new ArrayList<String>();

		lore.add(ChatColor.BLUE + "~ItemBank~");
		backButtonIcon = bankMenus.makeButton(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back", lore);
		exitButtonIcon = bankMenus.makeButton(Material.BARRIER, ChatColor.RED + "Exit", lore);
		moveAllItemsUpButtonIcon = bankMenus.makeButton(Material.PURPLE_STAINED_GLASS_PANE,
				ChatColor.DARK_PURPLE + "Move all items up");
		moveAllItemsDownButtonIcon = bankMenus.makeButton(Material.PURPLE_STAINED_GLASS_PANE,
				ChatColor.DARK_PURPLE + "Move all items down");
		depositButtonIcon = bankMenus.makeButton(Material.LIME_STAINED_GLASS_PANE, ChatColor.GOLD + "Deposit Items",
				lore);
		backgroundIcon = bankMenus.makeButton(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ", lore);

		menuButtons = new ItemStack[54];
		for (int slotNum = 44; slotNum < 54; slotNum++) {
			switch (slotNum) {
			case 45:
				menuButtons[slotNum] = backButtonIcon.clone();
				break;
			case 47:
				menuButtons[slotNum] = moveAllItemsDownButtonIcon.clone();
				break;
			case 51:
				menuButtons[slotNum] = moveAllItemsUpButtonIcon.clone();
				break;
			case 46:
			case 48:
			case 50:
			case 52:
				menuButtons[slotNum] = backgroundIcon.clone();
				break;
			case 49:
				menuButtons[slotNum] = exitButtonIcon.clone();
				break;
			case 53:
				menuButtons[slotNum] = depositButtonIcon.clone();
				break;
			default:
				break;
			}

		}
	}

	public void openMenuFor(Player player) {
		inventory = Bukkit.createInventory(player, 54, menuName);
		inventory.setContents(menuButtons);

		player.openInventory(inventory);
	}
}
