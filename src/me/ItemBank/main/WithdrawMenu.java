package me.ItemBank.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WithdrawMenu {
	ItemBank plugin;

	Inventory inventory;
	String menuName;
	ItemStack[] menuButtons;
	ItemStack sortByCategoryButton;
	ItemStack allItemsButton;
	ItemStack aButtonIcon;
	ItemStack bButtonIcon;
	ItemStack cButtonIcon;
	ItemStack dButtonIcon;
	ItemStack eButtonIcon;
	ItemStack fButtonIcon;
	ItemStack gButtonIcon;
	ItemStack hButtonIcon;
	ItemStack iButtonIcon;
	ItemStack jButtonIcon;
	ItemStack kButtonIcon;
	ItemStack lButtonIcon;
	ItemStack mButtonIcon;
	ItemStack nButtonIcon;
	ItemStack oButtonIcon;
	ItemStack pButtonIcon;
	ItemStack qButtonIcon;
	ItemStack rButtonIcon;
	ItemStack sButtonIcon;
	ItemStack tButtonIcon;
	ItemStack uButtonIcon;
	ItemStack vButtonIcon;
	ItemStack wButtonIcon;
	ItemStack xButtonIcon;
	ItemStack yButtonIcon;
	ItemStack zButtonIcon;
	ItemStack backgroundIcon;
	ItemStack backButtonIcon;
	ItemStack exitIcon;

	public WithdrawMenu(ItemBank plugin, String menuName) {
		this.plugin = plugin;
		this.menuName = ChatColor.BLUE + menuName;
	}

	public void setup() {
		BankMenus bankMenus = plugin.bank.bankMenus;

		sortByCategoryButton = bankMenus.makeButton(Material.BOOKSHELF, ChatColor.GOLD + "Categories");
		allItemsButton = bankMenus.makeButton(Material.ENDER_CHEST, ChatColor.GOLD + "All Items");
		aButtonIcon = bankMenus.makeButton(Material.SHULKER_BOX, ChatColor.GOLD + "A");
		bButtonIcon = bankMenus.makeButton(Material.WHITE_SHULKER_BOX, ChatColor.GOLD + "B");
		cButtonIcon = bankMenus.makeButton(Material.ORANGE_SHULKER_BOX, ChatColor.GOLD + "C");
		dButtonIcon = bankMenus.makeButton(Material.MAGENTA_SHULKER_BOX, ChatColor.GOLD + "D");
		eButtonIcon = bankMenus.makeButton(Material.LIGHT_BLUE_SHULKER_BOX, ChatColor.GOLD + "E");
		fButtonIcon = bankMenus.makeButton(Material.YELLOW_SHULKER_BOX, ChatColor.GOLD + "F");
		gButtonIcon = bankMenus.makeButton(Material.LIME_SHULKER_BOX, ChatColor.GOLD + "G");
		hButtonIcon = bankMenus.makeButton(Material.PINK_SHULKER_BOX, ChatColor.GOLD + "H");
		iButtonIcon = bankMenus.makeButton(Material.GRAY_SHULKER_BOX, ChatColor.GOLD + "I");
		jButtonIcon = bankMenus.makeButton(Material.LIGHT_GRAY_SHULKER_BOX, ChatColor.GOLD + "J");
		kButtonIcon = bankMenus.makeButton(Material.CYAN_SHULKER_BOX, ChatColor.GOLD + "K");
		lButtonIcon = bankMenus.makeButton(Material.PURPLE_SHULKER_BOX, ChatColor.GOLD + "L");
		mButtonIcon = bankMenus.makeButton(Material.BLUE_SHULKER_BOX, ChatColor.GOLD + "M");
		nButtonIcon = bankMenus.makeButton(Material.BROWN_SHULKER_BOX, ChatColor.GOLD + "N");
		oButtonIcon = bankMenus.makeButton(Material.GREEN_SHULKER_BOX, ChatColor.GOLD + "O");
		pButtonIcon = bankMenus.makeButton(Material.RED_SHULKER_BOX, ChatColor.GOLD + "P");
		qButtonIcon = bankMenus.makeButton(Material.BLACK_SHULKER_BOX, ChatColor.GOLD + "Q");
		rButtonIcon = bankMenus.makeButton(Material.SHULKER_BOX, ChatColor.GOLD + "R");
		sButtonIcon = bankMenus.makeButton(Material.WHITE_SHULKER_BOX, ChatColor.GOLD + "S");
		tButtonIcon = bankMenus.makeButton(Material.ORANGE_SHULKER_BOX, ChatColor.GOLD + "T");
		uButtonIcon = bankMenus.makeButton(Material.MAGENTA_SHULKER_BOX, ChatColor.GOLD + "U");
		vButtonIcon = bankMenus.makeButton(Material.LIGHT_BLUE_SHULKER_BOX, ChatColor.GOLD + "V");
		wButtonIcon = bankMenus.makeButton(Material.YELLOW_SHULKER_BOX, ChatColor.GOLD + "W");
		xButtonIcon = bankMenus.makeButton(Material.LIME_SHULKER_BOX, ChatColor.GOLD + "X");
		yButtonIcon = bankMenus.makeButton(Material.PINK_SHULKER_BOX, ChatColor.GOLD + "Y");
		zButtonIcon = bankMenus.makeButton(Material.GRAY_SHULKER_BOX, ChatColor.GOLD + "Z");
		backgroundIcon = bankMenus.makeButton(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ");
		backButtonIcon = bankMenus.makeButton(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back");
		exitIcon = bankMenus.makeButton(Material.BARRIER, ChatColor.RED + "Exit");

		menuButtons = new ItemStack[45];
		for (int slotNum = 0; slotNum < 45; slotNum++) {
			switch (slotNum) {
			case 4:
				menuButtons[slotNum] = sortByCategoryButton.clone();
				break;
			case 9:
				menuButtons[slotNum] = allItemsButton.clone();
				break;
			case 10:
				menuButtons[slotNum] = aButtonIcon.clone();
				break;
			case 11:
				menuButtons[slotNum] = bButtonIcon.clone();
				break;
			case 12:
				menuButtons[slotNum] = cButtonIcon.clone();
				break;
			case 13:
				menuButtons[slotNum] = dButtonIcon.clone();
				break;
			case 14:
				menuButtons[slotNum] = eButtonIcon.clone();
				break;
			case 15:
				menuButtons[slotNum] = fButtonIcon.clone();
				break;
			case 16:
				menuButtons[slotNum] = gButtonIcon.clone();
				break;
			case 17:
				menuButtons[slotNum] = hButtonIcon.clone();
				break;
			case 18:
				menuButtons[slotNum] = iButtonIcon.clone();
				break;
			case 19:
				menuButtons[slotNum] = jButtonIcon.clone();
				break;
			case 20:
				menuButtons[slotNum] = kButtonIcon.clone();
				break;
			case 21:
				menuButtons[slotNum] = lButtonIcon.clone();
				break;
			case 22:
				menuButtons[slotNum] = mButtonIcon.clone();
				break;
			case 23:
				menuButtons[slotNum] = nButtonIcon.clone();
				break;
			case 24:
				menuButtons[slotNum] = oButtonIcon.clone();
				break;
			case 25:
				menuButtons[slotNum] = pButtonIcon.clone();
				break;
			case 26:
				menuButtons[slotNum] = qButtonIcon.clone();
				break;
			case 27:
				menuButtons[slotNum] = rButtonIcon.clone();
				break;
			case 28:
				menuButtons[slotNum] = sButtonIcon.clone();
				break;
			case 29:
				menuButtons[slotNum] = tButtonIcon.clone();
				break;
			case 30:
				menuButtons[slotNum] = uButtonIcon.clone();
				break;
			case 31:
				menuButtons[slotNum] = vButtonIcon.clone();
				break;
			case 32:
				menuButtons[slotNum] = wButtonIcon.clone();
				break;
			case 33:
				menuButtons[slotNum] = xButtonIcon.clone();
				break;
			case 34:
				menuButtons[slotNum] = yButtonIcon.clone();
				break;
			case 35:
				menuButtons[slotNum] = zButtonIcon.clone();
				break;
			case 36:
				menuButtons[slotNum] = backButtonIcon.clone();
				break;
			case 40:
				menuButtons[slotNum] = exitIcon.clone();
				break;
			default:
				menuButtons[slotNum] = backgroundIcon.clone();
				break;
			}
		}
	}

	public void openMenuFor(Player player) {
		inventory = Bukkit.createInventory(player, 45, this.menuName);
		inventory.setContents(menuButtons);

		player.openInventory(inventory);
	}

}
