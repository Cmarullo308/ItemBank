package me.ItemBank.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class AmountMenu {
	ItemBank plugin;

	Inventory inventory;
	String menuName;
	ItemStack[] menuButtons;
	ItemStack minus1ButtonIcon;
	ItemStack minus10ButtonIcon;
	ItemStack resetTo0ButtonIcon;
	ItemStack add1ButtonIcon;
	ItemStack add10ButtonIcon;
	ItemStack backButtonIcon;
	ItemStack exitButtonIcon;
	ItemStack withdrawButtonIcon;
	ItemStack backgroundButtonIcon;

	public AmountMenu(ItemBank plugin, String menuName) {
		this.plugin = plugin;
		this.menuName = menuName;
	}

	public void setup() {
		BankMenus bankMenus = plugin.bank.bankMenus;
		ArrayList<String> lore = new ArrayList<String>();

		lore.add(ChatColor.BLUE + "~ItemBank~");
		minus1ButtonIcon = bankMenus.makeButton(Material.GLOWSTONE_DUST, ChatColor.RED + "-1", lore);
		minus10ButtonIcon = bankMenus.makeButton(Material.GLOWSTONE_DUST, ChatColor.RED + "-10", lore);
		resetTo0ButtonIcon = bankMenus.makeButton(Material.WHITE_STAINED_GLASS_PANE, "Reset to 0", lore);
		add1ButtonIcon = bankMenus.makeButton(Material.REDSTONE, ChatColor.RED + "+1", lore);
		add10ButtonIcon = bankMenus.makeButton(Material.REDSTONE, ChatColor.RED + "+10", lore);
		backButtonIcon = bankMenus.makeButton(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back", lore);
		exitButtonIcon = bankMenus.makeButton(Material.BARRIER, ChatColor.RED + "Exit", lore);
		withdrawButtonIcon = bankMenus.makeButton(Material.LIME_STAINED_GLASS_PANE, "Withdraw", lore);
		backgroundButtonIcon = bankMenus.makeButton(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ", lore);

		menuButtons = new ItemStack[27];
		for (int slotNum = 0; slotNum < 27; slotNum++) {
			switch (slotNum) {
			case 4:
				break;
			case 11:
				menuButtons[slotNum] = minus10ButtonIcon.clone();
				break;
			case 12:
				menuButtons[slotNum] = minus1ButtonIcon.clone();
				break;
			case 13:
				menuButtons[slotNum] = resetTo0ButtonIcon.clone();
				break;
			case 14:
				menuButtons[slotNum] = add1ButtonIcon.clone();
				break;
			case 15:
				menuButtons[slotNum] = add10ButtonIcon.clone();
				break;
			case 18:
				menuButtons[slotNum] = backButtonIcon.clone();
				break;
			case 22:
				menuButtons[slotNum] = exitButtonIcon.clone();
				break;
			case 35:
				menuButtons[slotNum] = withdrawButtonIcon.clone();
				break;
			default:
				menuButtons[slotNum] = backgroundButtonIcon.clone();
				break;
			}
		}
	}

	public void openMenuFor(Player player, Material material) {
		inventory = Bukkit.createInventory(player, 27, menuName);
		menuButtons[4] = new ItemStack(material);

		inventory.setContents(menuButtons);

		player.openInventory(inventory);
	}
}
