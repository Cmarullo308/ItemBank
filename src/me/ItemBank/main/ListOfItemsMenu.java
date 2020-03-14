package me.ItemBank.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

		backButtonIcon = bankMenus.makeButton(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back");
		exitButtonIcon = bankMenus.makeButton(Material.BARRIER, ChatColor.RED + "Exit");
		previousPageButtonIcon = bankMenus.makeButton(Material.LIME_STAINED_GLASS_PANE,
				ChatColor.GOLD + "Previous Page");
		nextPageButtonIcon = bankMenus.makeButton(Material.LIME_STAINED_GLASS_PANE, ChatColor.GOLD + "Next Page");
		backgroundIcon = bankMenus.makeButton(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ");

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

	public void openMenuFor(Player player, int page) {
		inventory = Bukkit.createInventory(player, 54, menuName);
		int firstSlot = (page - 1) * 45;
		int lastSlot = firstSlot + 44;

		Session session = plugin.bank.sessions.get(player);
		int amountOfItems = session.items.size();
		int count = firstSlot;
		int slotToSet = 0;

		for (int i = firstSlot; i <= lastSlot; i++) {

			if (count < amountOfItems && session.items.get(count) != null) {
				ItemStack item = new ItemStack(session.items.get(i));
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(capitalizeWord(item.getType().name().replace("_", " ").toLowerCase()) + " ("
						+ session.amounts.get(i) + ")");
				item.setItemMeta(meta);

				menuButtons[slotToSet] = item;
			} else {
				menuButtons[slotToSet] = null;
			}

			slotToSet++;
			count++;
		}

		if (page == 1) {
			menuButtons[47] = backgroundIcon.clone();
			menuButtons[51] = nextPageButtonIcon.clone();
		} else if (page == session.numOfPages) {
			menuButtons[47] = previousPageButtonIcon.clone();
		} else {
			menuButtons[47] = previousPageButtonIcon.clone();
			menuButtons[51] = nextPageButtonIcon.clone();
		}

		if (lastSlot >= session.items.size()) {
			menuButtons[51] = backgroundIcon.clone();
		}

		inventory.setContents(menuButtons);

		player.openInventory(inventory);
	}

	public static String capitalizeWord(String str) {
		String words[] = str.split("\\s");
		String capitalizeWord = "";
		for (String w : words) {
			String first = w.substring(0, 1);
			String afterfirst = w.substring(1);
			capitalizeWord += first.toUpperCase() + afterfirst + " ";
		}
		return capitalizeWord.trim();
	}
}
