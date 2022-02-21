package me.ItemBank.main;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ItemBank.main.Session.ACCOUNT;

public class AmountMenu {
	ItemBank plugin;

	Inventory inventory;
	String menuName;
	ItemStack[] menuButtons;
	ItemStack minus1ButtonIcon;
	ItemStack minus10ButtonIcon;
	ItemStack minus64ButtonIcon;
	ItemStack resetTo0ButtonIcon;
	ItemStack add1ButtonIcon;
	ItemStack add10ButtonIcon;
	ItemStack add64ButtonIcon;
	ItemStack backButtonIcon;
	ItemStack exitButtonIcon;
	ItemStack withdrawButtonIcon;
	ItemStack backgroundButtonIcon;

	public AmountMenu(ItemBank plugin, String menuName) {
		this.plugin = plugin;
		this.menuName = ChatColor.BLUE + menuName;
	}

	public void setup() {
		BankMenus bankMenus = plugin.bank.bankMenus;

		minus1ButtonIcon = bankMenus.makeButton(Material.GLOWSTONE_DUST, ChatColor.RED + "-1");
		minus10ButtonIcon = bankMenus.makeButton(Material.GLOWSTONE_DUST, ChatColor.RED + "-10");
		minus64ButtonIcon = bankMenus.makeButton(Material.GLOWSTONE_DUST, ChatColor.RED + "-64");
		resetTo0ButtonIcon = bankMenus.makeButton(Material.WHITE_STAINED_GLASS_PANE, "Reset to 0");
		add1ButtonIcon = bankMenus.makeButton(Material.REDSTONE, ChatColor.RED + "+1");
		add10ButtonIcon = bankMenus.makeButton(Material.REDSTONE, ChatColor.RED + "+10");
		add64ButtonIcon = bankMenus.makeButton(Material.REDSTONE, ChatColor.RED + "+64");
		backButtonIcon = bankMenus.makeButton(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back");
		exitButtonIcon = bankMenus.makeButton(Material.BARRIER, ChatColor.RED + "Exit");
		withdrawButtonIcon = bankMenus.makeButton(Material.LIME_STAINED_GLASS_PANE, "Withdraw");
		backgroundButtonIcon = bankMenus.makeButton(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ");

		menuButtons = new ItemStack[27];
		for (int slotNum = 0; slotNum < 27; slotNum++) {
			switch (slotNum) {
			case 4:
				break;
			case 10:
				menuButtons[slotNum] = minus64ButtonIcon.clone();
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
			case 16:
				menuButtons[slotNum] = add64ButtonIcon.clone();
				break;
			case 18:
				menuButtons[slotNum] = backButtonIcon.clone();
				break;
			case 22:
				menuButtons[slotNum] = exitButtonIcon.clone();
				break;
			case 26:
				menuButtons[slotNum] = withdrawButtonIcon.clone();
				break;
			default:
				menuButtons[slotNum] = backgroundButtonIcon.clone();
				break;
			}
		}
	}

	public void openMenuFor(Player player, Material material) {
		ConcurrentHashMap<Material, BankItem> bankItemData = plugin.bank.bankItemsData.bankItemData;
		
        DecimalFormat decimalFormat = plugin.decimalFormat;

		inventory = Bukkit.createInventory(player, 27, menuName);
		menuButtons[4] = new ItemStack(material);

		Session session = plugin.bank.sessions.get(player);
		BankItem bankItem = bankItemData.get(material);
		if (session.getAccount() == ACCOUNT.GLOBAL) {
			if (bankItem.accountAmounts.get(plugin.bank.globalUUID) != null) {
				session.setMaxAmount(bankItem.accountAmounts.get(plugin.bank.globalUUID));
			} else {
				session.setMaxAmount(0);
			}
		} else if (session.getAccount() == ACCOUNT.PRIVATE) {
			if (bankItem.accountAmounts.get(player.getUniqueId()) != null) {
				session.setMaxAmount(bankItem.accountAmounts.get(player.getUniqueId()));
			} else {
				session.setMaxAmount(0);
			}
		}

		if (session.getAmountSelected() > session.getMaxAmount()) {
			session.setAmountSelected(session.getMaxAmount());
		}

		ItemMeta meta;

		// Minus 1
		menuButtons[12] = new ItemStack(Material.GLOWSTONE_DUST);
		meta = menuButtons[12].getItemMeta();
		meta.setDisplayName(ChatColor.RED + "-1 " + ChatColor.WHITE + "(" + session.getAmountSelected() + " / "
				+ decimalFormat.format(session.getMaxAmount()) + ")");
		menuButtons[12].setItemMeta(meta);

		// Minus 10
		menuButtons[11] = new ItemStack(Material.GLOWSTONE_DUST);
		meta = menuButtons[11].getItemMeta();
		meta.setDisplayName(ChatColor.RED + "-10 " + ChatColor.WHITE + "(" + session.getAmountSelected() + " / "
				+ decimalFormat.format(session.getMaxAmount()) + ")");
		menuButtons[11].setItemMeta(meta);

		// Minus 64
		menuButtons[10] = new ItemStack(Material.GLOWSTONE_DUST);
		meta = menuButtons[10].getItemMeta();
		meta.setDisplayName(ChatColor.RED + "-64 " + ChatColor.WHITE + "(" + session.getAmountSelected() + " / "
				+ decimalFormat.format(session.getMaxAmount()) + ")");
		menuButtons[10].setItemMeta(meta);

		// Reset to 0
		menuButtons[13] = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		meta = menuButtons[13].getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Reset to 0 " + ChatColor.WHITE + "(" + session.getAmountSelected()
				+ " / " + decimalFormat.format(session.getMaxAmount()) + ")");
		menuButtons[13].setItemMeta(meta);

		// Add 1
		menuButtons[14] = new ItemStack(Material.REDSTONE);
		meta = menuButtons[14].getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "+1 " + ChatColor.WHITE + "(" + session.getAmountSelected() + " / "
				+ decimalFormat.format(session.getMaxAmount()) + ")");
		menuButtons[14].setItemMeta(meta);

		// Add 10
		menuButtons[15] = new ItemStack(Material.REDSTONE);
		meta = menuButtons[15].getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "+10 " + ChatColor.WHITE + "(" + session.getAmountSelected() + " / "
				+ decimalFormat.format(session.getMaxAmount()) + ")");
		menuButtons[15].setItemMeta(meta);

		// Add 64
		menuButtons[16] = new ItemStack(Material.REDSTONE);
		meta = menuButtons[16].getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "+64 " + ChatColor.WHITE + "(" + session.getAmountSelected() + " / "
				+ decimalFormat.format(session.getMaxAmount()) + ")");
		menuButtons[16].setItemMeta(meta);

		menuButtons[26] = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		meta = menuButtons[26].getItemMeta();
		meta.setDisplayName(
				ChatColor.GREEN + "Withdraw " + ChatColor.GOLD + session.getAmountSelected() + " " + ChatColor.GREEN
						+ capitalizeWord(session.getMaterialSelected().name().replace("_", " ").toLowerCase()));
		menuButtons[26].setItemMeta(meta);

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
