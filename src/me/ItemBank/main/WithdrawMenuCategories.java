package me.ItemBank.main;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class WithdrawMenuCategories {
	ItemBank plugin;

	ArrayList<ItemStack> categories;

	Inventory inventory;
	String menuName;
	ItemStack[] menuButtons;
	ItemStack sortAlphabeticalIcon;
	ItemStack backButtonIcon;
	ItemStack backgroundIcon;
	ItemStack previousPageButtonIcon;
	ItemStack nextPageButtonIcon;
	ItemStack exitButtonIcon;

	public WithdrawMenuCategories(ItemBank plugin, String menuName) {
		this.plugin = plugin;
		this.menuName = ChatColor.BLUE + menuName;
	}

	public void setup() {
		BankMenus bankMenus = plugin.bank.bankMenus;

		sortAlphabeticalIcon = bankMenus.makeButton(Material.BOOKSHELF, ChatColor.GOLD + "Alphabetical");
		backButtonIcon = bankMenus.makeButton(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back");
		backgroundIcon = bankMenus.makeButton(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ");
		exitButtonIcon = bankMenus.makeButton(Material.BARRIER, ChatColor.RED + "Exit");
		previousPageButtonIcon = bankMenus.makeButton(Material.LIME_STAINED_GLASS_PANE,
				ChatColor.GOLD + "Previous Page");
		nextPageButtonIcon = bankMenus.makeButton(Material.LIME_STAINED_GLASS_PANE, ChatColor.GOLD + "Next Page");

		menuButtons = new ItemStack[54];
		menuButtons[4] = sortAlphabeticalIcon.clone();
		menuButtons[45] = backButtonIcon.clone();
		menuButtons[49] = exitButtonIcon.clone();

		for (int slotNum = 0; slotNum < 9; slotNum++) {
			if (menuButtons[slotNum] == null) {
				menuButtons[slotNum] = backgroundIcon.clone();
			}
		}

		for (int slotNum = 45; slotNum < 54; slotNum++) {
			if (menuButtons[slotNum] == null) {
				menuButtons[slotNum] = backgroundIcon.clone();
			}
		}
		
		// --Setup categories
		categories = new ArrayList<ItemStack>();

		addCategory(Material.OAK_WOOD, "Wood");
		addCategory(Material.WHITE_WOOL, "Wool");
		addCategory(Material.STONE_BRICK_STAIRS, "Stairs");
		addCategory(Material.DIAMOND_SHOVEL, "Tools");
		addCategory(Material.APPLE, "Food");
		addCategory(Material.DIAMOND_SWORD, "Weapons");
		addCategory(Material.DIAMOND_CHESTPLATE, "Armor");
		addCategory(Material.RAIL, "Transportation");
		addCategory(Material.REDSTONE, "Redstone");
		addCategory(Material.RED_TULIP, "Plants and leaves");
		addCategory(Material.STONE, "Stones");
		addCategory(Material.STONE_SLAB, "Slabs");
		addCategory(Material.OAK_DOOR, "Doors");
		addCategory(Material.IRON_ORE, "Ores");
		addCategory(Material.CLAY_BALL, "Clay");
		addCategory(Material.STONE_BRICK_WALL, "Walls");
		addCategory(Material.PURPLE_DYE, "Dyes");
		addCategory(Material.GRAY_CONCRETE, "Concrete");
		addCategory(Material.WHEAT_SEEDS, "Seeds");
		addCategory(Material.STONE_BUTTON, "Buttons");
		addCategory(Material.CYAN_BANNER, "Banners");
		addCategory(Material.MUSIC_DISC_WAIT, "Music Discs");
		addCategory(Material.OAK_BOAT, "Boats");
		addCategory(Material.SPIDER_EYE, "Potion Ingredients");
		addCategory(Material.OAK_FENCE, "Fences");
		addCategory(Material.OAK_SIGN, "Signs");
		addCategory(Material.DIAMOND, "Mined");
		addCategory(Material.TORCH, "Light");
		addCategory(Material.CREEPER_SPAWN_EGG, "Spawn Eggs");
		addCategory(Material.SHULKER_BOX, "Shulker Boxes");
		addCategory(Material.RED_BED, "Beds");
		addCategory(Material.GLASS, "Glass");

		sortCategories(0, categories.size() - 1);
	}

	private void sortCategories(int low, int high) {
		int i = low;
		int j = high;
		ItemStack pivot = categories.get(low + (high - low) / 2);

		while (i <= j) {
			while (categories.get(i).getItemMeta().getDisplayName()
					.compareTo(pivot.getItemMeta().getDisplayName()) < 0) {
				i++;
			}

			while (categories.get(j).getItemMeta().getDisplayName()
					.compareTo(pivot.getItemMeta().getDisplayName()) > 0) {
				j--;
			}

			if (i <= j) {
				ItemStack temp = categories.get(i);
				categories.set(i, categories.get(j));
				categories.set(j, temp);
				i++;
				j--;
			}
		}
		if (low < j) {
			sortCategories(low, j);
		}
		if (i < high) {
			sortCategories(i, high);
		}
	}

	private void addCategory(Material iconMaterial, String displayName) {
		ItemStack category = new ItemStack(iconMaterial);
		ItemMeta meta = category.getItemMeta();
		meta.setDisplayName(displayName);
		category.setItemMeta(meta);
		categories.add(category);
	}

	public void openMenuFor(Player player, int pageNum) {
		inventory = Bukkit.createInventory(player, 54, this.menuName);
		Session session = plugin.bank.sessions.get(player);
		session.setPageNum(pageNum);

		int categoryPageAmount = categories.size() / 45;
		if (categories.size() % 45 != 0) {
			categoryPageAmount++;
		}

		int listSlotNum = (pageNum - 1) * 36;

		for (int slotNum = 9; slotNum < 45; slotNum++) {
			if (listSlotNum < categories.size()) {
				menuButtons[slotNum] = categories.get(listSlotNum);
			} else {
				menuButtons[slotNum] = null;
			}

			listSlotNum++;
		}

		if (pageNum == 1 && categoryPageAmount > 1) { // First page and no other pages
			menuButtons[47] = backgroundIcon.clone();
			menuButtons[51] = nextPageButtonIcon.clone();
		} else if (pageNum > 1 && pageNum < categoryPageAmount) { // Middle page
			menuButtons[47] = previousPageButtonIcon.clone();
			menuButtons[51] = nextPageButtonIcon.clone();
		} else if (pageNum == categoryPageAmount && categoryPageAmount > 1) { // Last page
			menuButtons[47] = previousPageButtonIcon.clone();
			menuButtons[51] = backgroundIcon.clone();
		}

		inventory.setContents(menuButtons);

		player.openInventory(inventory);
	}
}
