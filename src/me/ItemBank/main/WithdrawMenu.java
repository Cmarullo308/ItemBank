package me.ItemBank.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javafx.scene.transform.MatrixType;
import net.md_5.bungee.api.ChatColor;

public class WithdrawMenu {
	ItemBank plugin;

	Inventory inventory;
	String menuName;
	ItemStack[] menuButtons;
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
		ArrayList<String> lore = new ArrayList<String>();

		lore.add(ChatColor.BLUE + "~ItemBank~");
		allItemsButton = bankMenus.makeButton(Material.ENDER_CHEST, ChatColor.GOLD + "All Items", lore);
		aButtonIcon = bankMenus.makeButton(Material.SHULKER_BOX, "A", lore);
		bButtonIcon = bankMenus.makeButton(Material.WHITE_SHULKER_BOX, "B", lore);
		cButtonIcon = bankMenus.makeButton(Material.ORANGE_SHULKER_BOX, "C", lore);
		dButtonIcon = bankMenus.makeButton(Material.MAGENTA_SHULKER_BOX, "D", lore);
		eButtonIcon = bankMenus.makeButton(Material.LIGHT_BLUE_SHULKER_BOX, "E", lore);
		fButtonIcon = bankMenus.makeButton(Material.YELLOW_SHULKER_BOX, "F", lore);
		gButtonIcon = bankMenus.makeButton(Material.LIME_SHULKER_BOX, "G", lore);
		hButtonIcon = bankMenus.makeButton(Material.PINK_SHULKER_BOX, "H", lore);
		iButtonIcon = bankMenus.makeButton(Material.GRAY_SHULKER_BOX, "I", lore);
		jButtonIcon = bankMenus.makeButton(Material.LIGHT_GRAY_SHULKER_BOX, "J", lore);
		kButtonIcon = bankMenus.makeButton(Material.CYAN_SHULKER_BOX, "K", lore);
		lButtonIcon = bankMenus.makeButton(Material.PURPLE_SHULKER_BOX, "L", lore);
		mButtonIcon = bankMenus.makeButton(Material.BLUE_SHULKER_BOX, "M", lore);
		nButtonIcon = bankMenus.makeButton(Material.BROWN_SHULKER_BOX, "N", lore);
		oButtonIcon = bankMenus.makeButton(Material.GREEN_SHULKER_BOX, "O", lore);
		pButtonIcon = bankMenus.makeButton(Material.RED_SHULKER_BOX, "P", lore);
		qButtonIcon = bankMenus.makeButton(Material.BLACK_SHULKER_BOX, "Q", lore);
		rButtonIcon = bankMenus.makeButton(Material.SHULKER_BOX, "R", lore);
		sButtonIcon = bankMenus.makeButton(Material.WHITE_SHULKER_BOX, "S", lore);
		tButtonIcon = bankMenus.makeButton(Material.ORANGE_SHULKER_BOX, "T", lore);
		uButtonIcon = bankMenus.makeButton(Material.MAGENTA_SHULKER_BOX, "U", lore);
		vButtonIcon = bankMenus.makeButton(Material.LIGHT_BLUE_SHULKER_BOX, "V", lore);
		wButtonIcon = bankMenus.makeButton(Material.YELLOW_SHULKER_BOX, "W", lore);
		xButtonIcon = bankMenus.makeButton(Material.LIME_SHULKER_BOX, "X", lore);
		yButtonIcon = bankMenus.makeButton(Material.PINK_SHULKER_BOX, "Y", lore);
		zButtonIcon = bankMenus.makeButton(Material.GRAY_SHULKER_BOX, "Z", lore);
		backgroundIcon = bankMenus.makeButton(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ", lore);
		backButtonIcon = bankMenus.makeButton(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back", lore);
		exitIcon = bankMenus.makeButton(Material.BARRIER, ChatColor.RED + "Exit", lore);

		menuButtons = new ItemStack[45];
		for (int slotNum = 0; slotNum < 45; slotNum++) {
			switch (slotNum) {
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
