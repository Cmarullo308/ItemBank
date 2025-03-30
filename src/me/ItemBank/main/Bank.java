package me.ItemBank.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.HangingSign;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.Damageable;
import me.ItemBank.main.BankMenus.BANKMENU;
import me.ItemBank.main.Session.ACCOUNT;

public class Bank implements Listener {
	ItemBank plugin;

	BankItemsData bankItemsData;
	BankMenus bankMenus;
	UUID globalUUID;

	String magicLine;

	ConcurrentHashMap<Player, Session> sessions = new ConcurrentHashMap<Player, Session>();

	public Bank(ItemBank plugin) {
		bankMenus = new BankMenus(plugin);
		bankItemsData = new BankItemsData(plugin);
		globalUUID = UUID.fromString("97fcfd91-33f2-43b4-9993-6f24c011d624");

		this.plugin = plugin;

		magicLine = ChatColor.GOLD + "" + ChatColor.MAGIC + "aaaaaaaaaaaaaa";
	}

	public void setup() {
		bankMenus.setupMenus();
	}

	public class ItemComparator implements Comparator<Material> {
		@Override
		public int compare(Material material1, Material material2) {
			return material1.toString().compareTo(material2.toString());
		}

	}

	@EventHandler
	public void playerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = e.getClickedBlock();
			if (isSign(e.getClickedBlock().getType())) {
				Sign sign = (Sign) block.getState();
				if (isBankSign(sign)) {
					e.setCancelled(true);
					openBank(player);
				}
			}
		}
	}

	public void openBank(Player player) {
		sessions.put(player, new Session());
		sessions.get(player).setCurrentMenu(BANKMENU.BANKMAIN);
		bankMenus.bankManinMenu.openMenuFor(player);
	}

	private boolean isBankSign(Sign sign) {
		if (sign.getSide(Side.FRONT).getLine(0).equals(magicLine)) {
			return true;
		} else if (sign.getSide(Side.BACK).getLine(0).equals(magicLine)) {
			return true;
		}
		return false;
	}

	private boolean isSign(Material type) {
		if (type.toString().endsWith("_SIGN")) {
			return true;
		}
		return false;
	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();

		if (sessions.contains(player)) {
			sessions.get(player).setCurrentMenu(BANKMENU.NONE);
		}

		String inventoryName = event.getView().getTitle();
		if (inventoryName.substring(2).equals("Deposit Menu")) {

			ItemStack[] items = event.getInventory().getContents();
			for (int itemSlot = 0; itemSlot <= 44; itemSlot++) {
				if (items[itemSlot] != null) {
					givePlayerBackDepositItems(player, items[itemSlot]);
				}
			}
		}

	}

	private void givePlayerBackDepositItems(Player player, ItemStack itemStack) {
		HashMap<Integer, ItemStack> excessItems = player.getInventory().addItem(itemStack);
		for (Map.Entry<Integer, ItemStack> ex : excessItems.entrySet()) {
			player.getWorld().dropItem(player.getLocation(), ex.getValue());
		}
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		String inventoryName = event.getView().getTitle();
		Player player = (Player) event.getWhoClicked();

		// Bank Main Menu
		if (inventoryName.equals(bankMenus.bankManinMenu.menuName)) {
			if (event.getRawSlot() < 18) {
				event.setCancelled(true);
				bankMainMenuButtonClicked(event, player);
			}
			// Clicked within DepositOrWithdraw menu
//		} else if (inventoryName.equals(bankMenus.depositOrWithdrawMenu.menuName)) {
		} else if (isDepositOrWithdrawMenu(inventoryName)) {
			if (event.getRawSlot() < 18) {
				event.setCancelled(true);
				depositOrWithdrawMenuClicked(event, player);
			}
			// Clicked within Deposit menu
		} else if (inventoryName.equals(bankMenus.depositMenu.menuName)) {
			if (event.getRawSlot() > 44 && event.getRawSlot() < 54) {
				event.setCancelled(true);
				depositMenuClicked(event, player);
			}
			// Clicked within withdraw menu
		} else if (inventoryName.equals(bankMenus.withdrawMenu.menuName)) {
			if (event.getRawSlot() < 45) {
				event.setCancelled(true);
				withdrawButtonClicked(event, player);
			}
			// Clicked within withdrawMenuCategories menu
		} else if (inventoryName.equals(bankMenus.withdrawMenuCategoriesMenu.menuName)) {
			event.setCancelled(true);
			withdrawMenuCategoriesMenuClicked(event, player);
		}
		// Clicked within ListOfItems menu
		else if (isListOfItemsMenu(inventoryName)) {
			event.setCancelled(true);
			if (event.getRawSlot() < 54) {
				listOfItemsMenuButtonClicked(event, player);
			}
			// Clicked within amount menu
		} else if (isAmountMenu(inventoryName)) {
			if (event.getRawSlot() < 27) {
				event.setCancelled(true);
				amountMenuClicked(event, player);
			}
		}
	}

	private boolean isDepositOrWithdrawMenu(String inventoryName) {
		if (inventoryName.length() < 21) {
			return false;
		}

		if (inventoryName.substring(0, 21).equals(bankMenus.depositOrWithdrawMenu.menuName)) {
			return true;
		}

		return false;
	}

	private void withdrawMenuCategoriesMenuClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();
		Session session = sessions.get(player);
		ItemStack[] eventContents = event.getInventory().getContents();

		if (slotClicked == 4) {
			session.setPageNum(1); // delete later?
			bankMenus.withdrawMenu.openMenuFor(player);
			session.setCameFromCategories(false);
		} else if (slotClicked == 45) {
			bankMenus.depositOrWithdrawMenu.openMenuFor(player);
			session.setCameFromCategories(false);
		} else if (slotClicked == 47 && eventContents[slotClicked].getType() == Material.LIME_STAINED_GLASS_PANE) {
			bankMenus.withdrawMenuCategoriesMenu.openMenuFor(player, session.getPageNum() - 1);
		} else if (slotClicked == 51 && eventContents[slotClicked].getType() == Material.LIME_STAINED_GLASS_PANE) {
			bankMenus.withdrawMenuCategoriesMenu.openMenuFor(player, session.getPageNum() + 1);
		} else if (slotClicked == 49) {
			player.closeInventory();
		} else {
			// If clicked on a blank spot
			if (eventContents[slotClicked].getItemMeta().getDisplayName().equals(" ")) {
				return;
			}

			if (eventContents[slotClicked] != null) {
				session.setCategory(eventContents[slotClicked].getItemMeta().getDisplayName());
				openListOfItemsMenuFromCategory(player, session.getCategory());
			}
		}
	}

	private void amountMenuClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();
		Session session = sessions.get(player);

		switch (slotClicked) {
		case 10:
			session.setAmountSelected(session.getAmountSelected() - 64);
			bankMenus.amountMenu.openMenuFor(player, session.getMaterialSelected());
			break;
		case 11:
			session.setAmountSelected(session.getAmountSelected() - 10);
			bankMenus.amountMenu.openMenuFor(player, session.getMaterialSelected());
			break;
		case 12:
			session.setAmountSelected(session.getAmountSelected() - 1);
			bankMenus.amountMenu.openMenuFor(player, session.getMaterialSelected());
			break;
		case 13:
			session.setAmountSelected(0);
			bankMenus.amountMenu.openMenuFor(player, session.getMaterialSelected());
			break;
		case 14:
			session.setAmountSelected(session.getAmountSelected() + 1);
			bankMenus.amountMenu.openMenuFor(player, session.getMaterialSelected());
			break;
		case 15:
			session.setAmountSelected(session.getAmountSelected() + 10);
			bankMenus.amountMenu.openMenuFor(player, session.getMaterialSelected());
			break;
		case 16:
			session.setAmountSelected(session.getAmountSelected() + 64);
			bankMenus.amountMenu.openMenuFor(player, session.getMaterialSelected());
			break;
		case 18:
			bankMenus.listOfItemsMenu.openMenuFor(player, sessions.get(player).getPageNum());
			session.setAmountSelected(0);
			session.setMaterialSelected(null);
			break;
		case 22:
			player.closeInventory();
			break;
		case 26:
			withdrawItemsToPlayer(player);

			int amountSelected = session.getAmountSelected();

			// Item amount updates after withdraw without having to reload the menu
			session.amounts.set(session.items.indexOf(session.getMaterialSelected()),
					session.getMaxAmount() - amountSelected);

			session.setAmountSelected(0);
			if (session.getAccount() == ACCOUNT.GLOBAL) {
				updateForOtherPlayersInAmountMenu(player, session.getMaterialSelected());
				updateItemListForOtherPlayers(player, amountSelected, session.getMaterialSelected());
			}
			bankMenus.amountMenu.openMenuFor(player, session.getMaterialSelected());
			break;
		default:
			break;
		}
	}

	private void updateItemListForOtherPlayers(Player originalPlayer, int amountSelected, Material selectedMaterial) {
		for (Player playerInServer : Bukkit.getOnlinePlayers()) {
			if (playerInServer.equals(originalPlayer)) {
				continue;
			}
			if (sessions.get(playerInServer) != null) {
				Session playerInServerSession = sessions.get(playerInServer);
				if (playerInServerSession.getAccount() == ACCOUNT.GLOBAL) {
					if (playerInServerSession.amounts != null) {
						if (playerInServerSession.items.contains(selectedMaterial)) {
							int indexOfMaterial = playerInServerSession.items.indexOf(selectedMaterial);
							playerInServerSession.amounts.set(indexOfMaterial,
									playerInServerSession.amounts.get(indexOfMaterial) - amountSelected);
							reloadListOfItemsMenuForPlayersInMenu(playerInServer, playerInServerSession);
						}
					}
				}
			}
		}
	}

	private void reloadListOfItemsMenuForPlayersInMenu(Player playerInServer, Session playerInServerSession) {
		if (playerInServerSession.getCurrentMenu() == BANKMENU.LISTOFITEMS) {
			ItemStack[] invItems = playerInServer.getOpenInventory().getTopInventory().getContents();
			if (invItems.length > 9 && invItems[49].getType() == Material.BARRIER) {
				if (invItems[49].getItemMeta().getDisplayName().equals(ChatColor.RED + "Exit")) {
					bankMenus.listOfItemsMenu.openMenuFor(playerInServer, playerInServerSession.getPageNum());
				}
			}
		}
	}

	private void updateForOtherPlayersInAmountMenu(Player player, Material materialSelected) {
		for (Player playerInServer : Bukkit.getOnlinePlayers()) {
			if (sessions.get(playerInServer) != null) {
				Session playerInServerSession = sessions.get(playerInServer);
				if (playerInServerSession.getMaterialSelected() != null
						&& playerInServerSession.getMaterialSelected().equals(materialSelected)) {
					if (playerInServerSession.getCurrentMenu() == BANKMENU.AMOUNT
							&& playerInSameMenu(playerInServer, materialSelected)) {
						bankMenus.amountMenu.openMenuFor(playerInServer, materialSelected);
					}
				}
			}
		}
	}

	// Prevents the amount menu from opening up for people not currently using the
	// bank
	private boolean playerInSameMenu(Player player, Material material) {
		ItemStack[] invItems = player.getOpenInventory().getTopInventory().getContents();

		if (invItems[4] == null) {
			return false;
		}

		if (invItems[4].getType() != material) {
			return false;
		}

		if (invItems[22].getType() != Material.BARRIER) {
			return false;
		}

		if (!invItems[22].getItemMeta().getDisplayName().equals(ChatColor.RED + "Exit")) {
			return false;
		}

		return true;
	}

	private void withdrawItemsToPlayer(Player player) {
		Session session = sessions.get(player);
		ACCOUNT account = session.getAccount();
		Material itemType = session.getMaterialSelected();
		int amountSelect = session.getAmountSelected();

		BankItem item = bankItemsData.bankItemData.get(itemType);

		// ----give to player
		HashMap<Integer, ItemStack> excessItems = player.getInventory().addItem(new ItemStack(itemType, amountSelect));
		for (Map.Entry<Integer, ItemStack> ex : excessItems.entrySet()) {
			player.getWorld().dropItem(player.getLocation(), ex.getValue());
		}

		// Remove from bank
		if (account == ACCOUNT.GLOBAL) {
			if (item.accountAmounts.get(globalUUID) != null) {
				item.accountAmounts.put(globalUUID, item.accountAmounts.get(globalUUID) - amountSelect);
				if (bankItemsData.bankItemData.get(itemType).accountAmounts.get(globalUUID) < 1) {
					bankItemsData.bankItemData.get(itemType).accountAmounts.remove(globalUUID);
				}
			}
		} else if (account == ACCOUNT.PRIVATE) {
			if (item.accountAmounts.get(player.getUniqueId()) != null) {
				item.accountAmounts.put(player.getUniqueId(),
						item.accountAmounts.get(player.getUniqueId()) - amountSelect);
				if (bankItemsData.bankItemData.get(itemType).accountAmounts.get(player.getUniqueId()) < 1) {
					bankItemsData.bankItemData.get(itemType).accountAmounts.remove(player.getUniqueId());
				}
			}
		}

		bankItemsData.saveBankData();
	}

	private boolean isAmountMenu(String inventoryName) {
		if (inventoryName.length() < 13) {
			return false;
		}

		return inventoryName.substring(2, 13).equals("Amount Menu");
	}

	private boolean isListOfItemsMenu(String inventoryName) {
		if (inventoryName.length() < 15) {
			return false;
		}

		if (inventoryName.substring(0, 15).equals(bankMenus.listOfItemsMenu.menuName)) {
			return true;
		}

		return false;
	}

	private void listOfItemsMenuButtonClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();
		Session session = sessions.get(player);
		int pageNum;

		if (slotClicked == 51) {
			if (event.getInventory().getContents()[slotClicked].getType() == Material.LIME_STAINED_GLASS_PANE) {
				pageNum = session.getPageNum() + 1;
				session.setPageNum(pageNum);
				bankMenus.listOfItemsMenu.openMenuFor(player, pageNum);
				session.setCurrentMenu(BANKMENU.LISTOFITEMS);
			}
		} else if (slotClicked == 45) {
			session.setPageNum(1);
			if (!session.cameFromCategories()) {
				bankMenus.withdrawMenu.openMenuFor(player);
			} else {
				bankMenus.withdrawMenuCategoriesMenu.openMenuFor(player, 1);
			}
			session.setCurrentMenu(BANKMENU.WITHDRAW);
		} else if (slotClicked == 49) {
			player.closeInventory();
		} else if (slotClicked == 47) {
			if (event.getInventory().getContents()[slotClicked].getType() == Material.LIME_STAINED_GLASS_PANE) {
				pageNum = session.getPageNum() - 1;
				session.setPageNum(pageNum);
				bankMenus.listOfItemsMenu.openMenuFor(player, pageNum);
				session.setCurrentMenu(BANKMENU.LISTOFITEMS);
			}
		} else if (slotClicked >= 0 && slotClicked <= 44) {
			if (event.getInventory().getContents()[slotClicked] != null) {
				session.setMaterialSelected(event.getInventory().getContents()[slotClicked].getType());
				bankMenus.amountMenu.openMenuFor(player, event.getInventory().getContents()[slotClicked].getType());
				session.setCurrentMenu(BANKMENU.AMOUNT);
			}
		}
	}

	private void withdrawButtonClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();
		Session session = sessions.get(player);

		if (slotClicked == 4) {
			bankMenus.withdrawMenuCategoriesMenu.openMenuFor(player, 1);
			session.setCameFromCategories(true);
			session.setPageNum(1);
			session.setCurrentMenu(BANKMENU.WITHDRAWCATEGORIES);
		} else if (slotClicked >= 9 && slotClicked <= 35) {
			session.setCurrentMenu(BANKMENU.LISTOFITEMS);
			openListOfItemsMenu(player, event.getInventory().getContents()[slotClicked].getItemMeta().getDisplayName());
		} else if (slotClicked == 36) {
			bankMenus.depositOrWithdrawMenu.openMenuFor(player);
			session.setCurrentMenu(BANKMENU.DEPOSITORWHITDRAW);
		} else if (slotClicked == 40) {
			player.closeInventory();
		}
	}

	private void openListOfItemsMenuFromCategory(Player player, String category) {
		// All lettered items
		ArrayList<Material> items = new ArrayList<Material>();
		// Get all materials in bank
		for (Material material : bankItemsData.bankItemData.keySet()) {
			items.add(material);
		}

		// Alphabetize items
		Collections.sort(items, new ItemComparator());

		// Extract all items that belong to player
		Session session = sessions.get(player);
		session.items = new ArrayList<Material>();
		session.amounts = new ArrayList<Integer>();
		session.setCurrentMenu(BANKMENU.LISTOFITEMS);

		UUID uuid;

		if (session.getAccount() == ACCOUNT.GLOBAL) {
			uuid = plugin.bank.globalUUID;
		} else {
			uuid = player.getUniqueId();
		}

		for (Material material : items) {
			if (accountHasItem(material, uuid)) {
				if (isInSelectedCategory(material, session.getCategory())) {
					session.items.add(material);
					session.amounts.add(bankItemsData.bankItemData.get(material).accountAmounts.get(uuid));
				}
			}
		}

		session.numOfPages = session.items.size() / 45;
		if (session.items.size() % 45 != 0) {
			session.numOfPages++;
		}

		session.setPageNum(1);
		bankMenus.listOfItemsMenu.openMenuFor(player, 1);
	}

	private boolean isInSelectedCategory(Material material, String category) {
		// Make sure case is lowercase and item name is all CAPS

		switch (category.toLowerCase()) {
		case "wool":
			if (material.toString().endsWith("WOOL")) {
				return true;
			}
			break;
		case "carpets":
			if (material.toString().endsWith("_CARPET")) {
				return true;
			}
			break;
		case "wood":
			if (material.toString().endsWith("WOOD")) {
				return true;
			} else if (material.toString().endsWith("LOG")) {
				return true;
			} else if (material.toString().endsWith("PLANKS")) {
				return true;
			} else if (material.toString().endsWith("HYPHAE")) {
				return true;
			} else if (material.toString().endsWith("STEM") && !material.equals(Material.MUSHROOM_STEM)) {
				return true;
			}

			switch (material) {
			case BAMBOO_MOSAIC:
			case BAMBOO_BLOCK:
			case STRIPPED_BAMBOO_BLOCK:
			case CREAKING_HEART:
				return true;
			default:
				break;
			}

			break;
		case "stairs":
			if (material.toString().endsWith("STAIRS")) {
				return true;
			}
			break;
		case "weapons":
			switch (material) {
			case BOW:
			case ARROW:
			case IRON_SWORD:
			case WOODEN_SWORD:
			case STONE_SWORD:
			case DIAMOND_SWORD:
			case GOLDEN_SWORD:
			case TRIDENT:
			case CROSSBOW:
			case WIND_CHARGE:
				return true;
			default:
				break;
			}
			break;
		case "potion ingredients":
			switch (material) {
			case FERMENTED_SPIDER_EYE:
			case REDSTONE:
			case GLOWSTONE:
			case SPIDER_EYE:
			case SUGAR:
			case BROWN_MUSHROOM:
			case GLISTERING_MELON_SLICE:
			case MELON_SLICE:
			case GOLDEN_CARROT:
			case TURTLE_HELMET:
			case GLASS_BOTTLE:
			case NETHER_WART:
			case GUNPOWDER:
			case RABBIT_FOOT:
			case MAGMA_CREAM:
			case BLAZE_POWDER:
			case GHAST_TEAR:
			case PUFFERFISH:
			case PHANTOM_MEMBRANE:
				return true;
			default:
				break;
			}
			break;
		case "fences and walls":
			if (material.toString().contains("FENCE")) {
				return true;
			}
			if (material.toString().endsWith("WALL")) {
				return true;
			}
			break;
		case "signs":
			if (material.toString().endsWith("_SIGN")) {
				return true;
			}
			break;
		case "mined":
			switch (material) {
			case DIAMOND_BLOCK:
			case DIAMOND:
			case IRON_NUGGET:
			case IRON_INGOT:
			case IRON_BLOCK:
			case GOLD_NUGGET:
			case GOLD_INGOT:
			case GOLD_BLOCK:
			case GLOWSTONE:
			case GLOWSTONE_DUST:
			case COAL:
			case COAL_BLOCK:
			case EMERALD:
			case EMERALD_BLOCK:
			case REDSTONE:
			case REDSTONE_BLOCK:
			case LAPIS_BLOCK:
			case LAPIS_LAZULI:
			case QUARTZ:
			case QUARTZ_BLOCK:
			case NETHERITE_BLOCK:
			case NETHERITE_INGOT:
			case NETHERITE_SCRAP:
			case AMETHYST_SHARD:
			case COPPER_INGOT:
			case COPPER_BLOCK:
			case RESIN_CLUMP:
			case RESIN_BLOCK:
			case RESIN_BRICK:
				return true;
			default:
				break;
			}
			break;
		case "armor":
			switch (material) {
			case LEATHER_HELMET:
			case CHAINMAIL_HELMET:
			case IRON_HELMET:
			case DIAMOND_HELMET:
			case GOLDEN_HELMET:
			case LEATHER_CHESTPLATE:
			case CHAINMAIL_CHESTPLATE:
			case IRON_CHESTPLATE:
			case DIAMOND_CHESTPLATE:
			case GOLDEN_CHESTPLATE:
			case LEATHER_LEGGINGS:
			case CHAINMAIL_LEGGINGS:
			case IRON_LEGGINGS:
			case DIAMOND_LEGGINGS:
			case GOLDEN_LEGGINGS:
			case LEATHER_BOOTS:
			case CHAINMAIL_BOOTS:
			case IRON_BOOTS:
			case DIAMOND_BOOTS:
			case GOLDEN_BOOTS:
			case TURTLE_HELMET:
			case SHIELD:
			case IRON_HORSE_ARMOR:
			case GOLDEN_HORSE_ARMOR:
			case DIAMOND_HORSE_ARMOR:
			case LEATHER_HORSE_ARMOR:
			case NETHERITE_HELMET:
			case NETHERITE_CHESTPLATE:
			case NETHERITE_LEGGINGS:
			case NETHERITE_BOOTS:
				return true;
			default:
				break;
			}
			break;
		case "transportation":
			switch (material) {
			case POWERED_RAIL:
			case DETECTOR_RAIL:
			case RAIL:
			case ACTIVATOR_RAIL:
			case MINECART:
			case SADDLE:
			case OAK_BOAT:
			case CHEST_MINECART:
			case FURNACE_MINECART:
			case CARROT_ON_A_STICK:
			case TNT_MINECART:
			case HOPPER_MINECART:
			case ELYTRA:
			case SPRUCE_BOAT:
			case BIRCH_BOAT:
			case JUNGLE_BOAT:
			case ACACIA_BOAT:
			case DARK_OAK_BOAT:
				return true;
			default:
				break;
			}
			break;
		case "dyes":
			if (material.toString().endsWith("DYE")) {
				return true;
			}
			break;
		case "concrete":
			if (material.toString().endsWith("CONCRETE")) {
				return true;
			} else if (material.toString().endsWith("POWDER")) {
				if (material != Material.GUNPOWDER && material != Material.BLAZE_POWDER) {
					return true;
				}
			}
			break;
		case "seeds":
			switch (material) {
			case WHEAT_SEEDS:
			case PUMPKIN_SEEDS:
			case MELON_SEEDS:
			case BEETROOT_SEEDS:
				return true;
			default:
				break;
			}
			break;
		case "buttons":
			if (material.toString().endsWith("BUTTON")) {
				return true;
			}
			break;
		case "boats":
			if (material.toString().endsWith("BOAT")) {
				return true;
			} else if (material.toString().endsWith("_RAFT")) {
				return true;
			}
			break;
		case "banners":
			if (material.toString().endsWith("BANNER") || material.toString().endsWith("BANNER_PATTERN")) {
				return true;
			}
			break;
		case "music discs":
			if (material.toString().startsWith("MUSIC")) {
				return true;
			}

			if (material == Material.DISC_FRAGMENT_5) {
				return true;
			}
			break;
		case "redstone":
			if (material.toString().endsWith("_PRESSURE_PLATE")) {
				return true;
			}

			if (material.toString().endsWith("_TRAPDOOR")) {
				return true;
			}

			if (material.toString().endsWith("_DOOR")) {
				return true;
			}

			if (material.toString().endsWith("_FENCE_GATE")) {
				return true;
			}

			switch (material) {
			case DISPENSER:
			case NOTE_BLOCK:
			case STICKY_PISTON:
			case PISTON:
			case TNT:
			case LEVER:
			case REDSTONE_TORCH:
			case STONE_BUTTON:
			case REDSTONE_LAMP:
			case TRIPWIRE_HOOK:
			case OAK_BUTTON:
			case SPRUCE_BUTTON:
			case BIRCH_BUTTON:
			case JUNGLE_BUTTON:
			case ACACIA_BUTTON:
			case DARK_OAK_BUTTON:
			case TRAPPED_CHEST:
			case LIGHT_WEIGHTED_PRESSURE_PLATE:
			case HEAVY_WEIGHTED_PRESSURE_PLATE:
			case DAYLIGHT_DETECTOR:
			case REDSTONE_BLOCK:
			case HOPPER:
			case DROPPER:
			case OBSERVER:
			case REPEATER:
			case COMPARATOR:
			case REDSTONE:
			case LECTERN:
			case SCULK_SENSOR:
			case CALIBRATED_SCULK_SENSOR:
			case CHISELED_BOOKSHELF:
				return true;
			default:
				break;
			}
			break;
		case "plants and leaves":
			if (material.toString().endsWith("_LEAVES")) {
				return true;
			}

			if (material.toString().endsWith("_SAPLING")) {
				return true;
			}

			if (material.toString().contains("_CORAL")) {
				return true;
			}

			switch (material) {
			case AZALEA:
			case FLOWERING_AZALEA:
			case COBWEB:
			case SHORT_GRASS:
			case FERN:
			case DEAD_BUSH:
			case SEAGRASS:
			case SEA_PICKLE:
			case DANDELION:
			case POPPY:
			case BLUE_ORCHID:
			case ALLIUM:
			case AZURE_BLUET:
			case RED_TULIP:
			case ORANGE_TULIP:
			case WHITE_TULIP:
			case PINK_TULIP:
			case OXEYE_DAISY:
			case CORNFLOWER:
			case LILY_OF_THE_VALLEY:
			case WITHER_ROSE:
			case BROWN_MUSHROOM:
			case RED_MUSHROOM:
			case CHORUS_FLOWER:
			case CACTUS:
			case VINE:
			case LILY_PAD:
			case SUNFLOWER:
			case LILAC:
			case ROSE_BUSH:
			case PEONY:
			case TALL_GRASS:
			case LARGE_FERN:
			case CRIMSON_ROOTS:
			case POTTED_CRIMSON_ROOTS:
			case POTTED_WARPED_ROOTS:
			case WARPED_ROOTS:
			case NETHER_SPROUTS:
			case TWISTING_VINES:
			case TWISTING_VINES_PLANT:
			case BIG_DRIPLEAF:
			case SMALL_DRIPLEAF:
			case HANGING_ROOTS:
			case MOSS_BLOCK:
			case MOSS_CARPET:
			case SPORE_BLOSSOM:
			case MANGROVE_PROPAGULE:
			case FLOWER_POT:
			case PINK_PETALS:
			case PITCHER_PLANT:
			case TORCHFLOWER:
			case PITCHER_POD:
			case TORCHFLOWER_SEEDS:
			case COCOA_BEANS:
			case OPEN_EYEBLOSSOM:
			case CLOSED_EYEBLOSSOM:
			case PALE_HANGING_MOSS:
			case PALE_MOSS_BLOCK:
			case PALE_MOSS_CARPET:
			case BUSH:
			case CACTUS_FLOWER:
			case FIREFLY_BUSH:
			case LEAF_LITTER:
			case WILDFLOWERS:
			case SHORT_DRY_GRASS:
			case TALL_DRY_GRASS:
				return true;
			default:
				break;
			}
			break;
		case "stones":
			switch (material) {
			case STONE:
			case GRANITE:
			case POLISHED_GRANITE:
			case DIORITE:
			case POLISHED_DIORITE:
			case ANDESITE:
			case POLISHED_ANDESITE:
			case COBBLESTONE:
			case BEDROCK:
			case GRAVEL:
			case STONE_SLAB:
			case SMOOTH_STONE_SLAB:
			case SANDSTONE_SLAB:
			case CUT_SANDSTONE_SLAB:
			case COBBLESTONE_SLAB:
			case STONE_BRICK_SLAB:
			case RED_SANDSTONE_SLAB:
			case CUT_RED_SANDSTONE_SLAB:
			case SMOOTH_STONE:
			case OBSIDIAN:
			case COBBLESTONE_STAIRS:
			case STONE_BRICKS:
			case MOSSY_STONE_BRICKS:
			case CRACKED_STONE_BRICKS:
			case CHISELED_STONE_BRICKS:
			case STONE_BRICK_STAIRS:
			case END_STONE:
			case END_STONE_BRICKS:
			case SANDSTONE_STAIRS:
			case RED_SANDSTONE:
			case CHISELED_RED_SANDSTONE:
			case CUT_RED_SANDSTONE:
			case RED_SANDSTONE_STAIRS:
			case POLISHED_GRANITE_STAIRS:
			case SMOOTH_RED_SANDSTONE_STAIRS:
			case MOSSY_STONE_BRICK_SLAB:
			case POLISHED_DIORITE_STAIRS:
			case MOSSY_COBBLESTONE_STAIRS:
			case END_STONE_BRICK_STAIRS:
			case STONE_STAIRS:
			case SMOOTH_SANDSTONE_STAIRS:
			case GRANITE_STAIRS:
			case ANDESITE_STAIRS:
			case POLISHED_ANDESITE_STAIRS:
			case DIORITE_STAIRS:
			case POLISHED_GRANITE_SLAB:
			case SMOOTH_RED_SANDSTONE_SLAB:
			case POLISHED_DIORITE_SLAB:
			case MOSSY_COBBLESTONE_SLAB:
			case END_STONE_BRICK_SLAB:
			case SMOOTH_SANDSTONE_SLAB:
			case GRANITE_SLAB:
			case ANDESITE_SLAB:
			case POLISHED_ANDESITE_SLAB:
			case DIORITE_SLAB:
			case BASALT:
			case POLISHED_BASALT:
			case GILDED_BLACKSTONE:
			case LODESTONE:
			case CRYING_OBSIDIAN:
			case CALCITE:
			case DRIPSTONE_BLOCK:
			case POINTED_DRIPSTONE:
			case SMOOTH_BASALT:
				// Deepslate
			case DEEPSLATE:
			case COBBLED_DEEPSLATE:
			case POLISHED_DEEPSLATE:
			case INFESTED_DEEPSLATE:
			case DEEPSLATE_BRICKS:
			case CRACKED_DEEPSLATE_BRICKS:
			case DEEPSLATE_TILES:
			case CRACKED_DEEPSLATE_TILES:
			case CHISELED_DEEPSLATE:
			case REINFORCED_DEEPSLATE:
			case SUSPICIOUS_GRAVEL:
			case COBBLED_DEEPSLATE_STAIRS:
			case COBBLED_DEEPSLATE_SLAB:
			case POLISHED_DEEPSLATE_STAIRS:
			case POLISHED_DEEPSLATE_SLAB:
			case DEEPSLATE_BRICK_STAIRS:
			case DEEPSLATE_BRICK_SLAB:
			case DEEPSLATE_TILE_STAIRS:
			case DEEPSLATE_TILE_SLAB:
				// Tuff
			case TUFF:
			case TUFF_STAIRS:
			case TUFF_SLAB:
			case TUFF_WALL:
			case CHISELED_TUFF:
			case POLISHED_TUFF:
			case POLISHED_TUFF_STAIRS:
			case POLISHED_TUFF_SLAB:
			case POLISHED_TUFF_WALL:
			case TUFF_BRICKS:
			case TUFF_BRICK_STAIRS:
			case TUFF_BRICK_SLAB:
			case TUFF_BRICK_WALL:
			case CHISELED_TUFF_BRICKS:
			//Resin
			case RESIN_BRICKS:
			case CHISELED_RESIN_BRICKS:
				return true;
			default:
				break;
			}
			break;
		case "copper":
			if (material.toString().contains("COPPER")) {
				return true;
			}
			break;
		case "slabs":
			if (material.toString().endsWith("SLAB")) {
				return true;
			}
			break;
		case "doors":
			if (material.toString().endsWith("DOOR")) {
				return true;
			}
			break;
		case "ores":
			switch (material) {
			// Gold
			case GOLD_ORE:
			case DEEPSLATE_GOLD_ORE:
			case RAW_GOLD:
			case NETHER_GOLD_ORE:
				// Iron
			case IRON_ORE:
			case DEEPSLATE_IRON_ORE:
			case RAW_IRON:
				// Coal
			case COAL_ORE:
			case DEEPSLATE_COAL_ORE:
				// Copper
			case COPPER_ORE:
			case DEEPSLATE_COPPER_ORE:
			case RAW_COPPER:
				// Lapis
			case LAPIS_ORE:
			case DEEPSLATE_LAPIS_ORE:
				// Diamond
			case DIAMOND_ORE:
			case DEEPSLATE_DIAMOND_ORE:
				// Redstone
			case REDSTONE_ORE:
			case DEEPSLATE_REDSTONE_ORE:
				// Emerald
			case EMERALD_ORE:
			case DEEPSLATE_EMERALD_ORE:
				// Netherquartz
			case NETHER_QUARTZ_ORE:
				// Ancient Debris
			case ANCIENT_DEBRIS:
				return true;
			default:
				break;
			}
			break;
		case "clay":
			if (material.toString().endsWith("TERRACOTTA")) {
				return true;
			} else if (material == Material.CLAY || material == Material.CLAY_BALL) {
				return true;
			}
			break;
		case "tools":
			if (material.toString().endsWith("SHOVEL")) {
				return true;
			} else if (material.toString().endsWith("PICKAXE")) {
				return true;
			} else if (material.toString().endsWith("AXE")) {
				return true;
			} else if (material.toString().endsWith("HOE")) {
				return true;
			} else if (material.toString().contains("COMPASS")) {
				return true;
			}
			switch (material) {
			case SHEARS:
			case FLINT_AND_STEEL:
			case COMPASS:
			case FISHING_ROD:
			case CLOCK:
			case LEAD:
			case NAME_TAG:
			case WARPED_FUNGUS_ON_A_STICK:
			case SPYGLASS:
			case GOAT_HORN:
			case RECOVERY_COMPASS:
			case BRUSH:
				return true;
			default:
				break;
			}
			break;
		case "food":
			switch (material) {
			case APPLE:
			case MUSHROOM_STEW:
			case BREAD:
			case PORKCHOP:
			case COOKED_PORKCHOP:
			case GOLDEN_APPLE:
			case ENCHANTED_GOLDEN_APPLE:
			case COD:
			case SALMON:
			case TROPICAL_FISH:
			case PUFFERFISH:
			case COOKED_COD:
			case COOKED_SALMON:
			case CAKE:
			case COOKIE:
			case MELON_SLICE:
			case DRIED_KELP:
			case BEEF:
			case COOKED_BEEF:
			case CHICKEN:
			case COOKED_CHICKEN:
			case ROTTEN_FLESH:
			case SPIDER_EYE:
			case CARROT:
			case POTATO:
			case BAKED_POTATO:
			case POISONOUS_POTATO:
			case PUMPKIN_PIE:
			case RABBIT:
			case COOKED_RABBIT:
			case RABBIT_STEW:
			case MUTTON:
			case COOKED_MUTTON:
			case BEETROOT:
			case BEETROOT_SOUP:
			case SWEET_BERRIES:
			case HONEY_BOTTLE:
			case GLOW_BERRIES:
				return true;
			default:
				break;
			}
			break;
		case "light":
			if (material.toString().endsWith("CANDLE")) {
				return true;
			}

			if (material.toString().endsWith("COPPER_BULB")) {
				return true;
			}
			switch (material) {
			case BEACON:
			case END_GATEWAY:
			case END_PORTAL:
			case FIRE:
			case LAVA:
			case GLOWSTONE:
			case JACK_O_LANTERN:
			case REDSTONE_LAMP:
			case SEA_LANTERN:
			case SEA_PICKLE:
			case SHROOMLIGHT:
			case CONDUIT:
			case LANTERN:
			case CAMPFIRE:
			case END_ROD:
			case TORCH:
			case NETHER_PORTAL:
			case CRYING_OBSIDIAN:
			case SOUL_FIRE:
			case SOUL_LANTERN:
			case SOUL_TORCH:
			case SOUL_CAMPFIRE:
			case ENDER_CHEST:
			case REDSTONE_TORCH:
			case MAGMA_BLOCK:
			case BREWING_STAND:
			case BROWN_MUSHROOM:
			case DRAGON_EGG:
			case END_PORTAL_FRAME:
			case RESPAWN_ANCHOR:
			case GLOW_LICHEN:
			case LIGHT:
			case GLOW_ITEM_FRAME:
			case OCHRE_FROGLIGHT:
			case VERDANT_FROGLIGHT:
			case PEARLESCENT_FROGLIGHT:
				return true;
			default:
				break;
			}
			break;
		case "spawn eggs":
			if (material.toString().endsWith("SPAWN_EGG")) {
				return true;
			}
			break;
		case "shulker boxes":
			if (material.toString().endsWith("SHULKER_BOX")) {
				return true;
			}
			break;
		case "beds":
			if (material.toString().endsWith("_BED")) {
				return true;
			}
			break;
		case "glass":
			if (material.toString().endsWith("GLASS") || material.toString().endsWith("PANE")) {
				return true;
			}
			break;
		case "pressure plates":
			if (material.toString().endsWith("PRESSURE_PLATE")) {
				return true;
			}
			break;
		case "buckets":
			if (material.toString().endsWith("BUCKET")) {
				return true;
			}
			break;
		case "pottery sherds":
			if (material.toString().endsWith("_SHERD")) {
				return true;
			}
			break;
		case "smithing templates":
			if (material.toString().endsWith("SMITHING_TEMPLATE")) {
				return true;
			} else if (material == Material.SMITHING_TABLE) {
				return true;
			}
			break;
		default:
			return false;
		}

		return false;

	}

	private void openListOfItemsMenu(Player player, String letter) {
		// All lettered items
		ArrayList<Material> items = new ArrayList<Material>();
		// Get all materials in bank
		for (Material material : bankItemsData.bankItemData.keySet()) {
			items.add(material);
		}

		// Alphabetize items
		Collections.sort(items, new ItemComparator());

		// Extract all items that belong to player
		Session session = sessions.get(player);
		session.items = new ArrayList<Material>();
		session.amounts = new ArrayList<Integer>();

		letter = letter.substring(2);
		boolean specificLetter;
		if (letter.equalsIgnoreCase("All Items")) {
			specificLetter = false;
		} else {
			specificLetter = true;
		}

		UUID uuid;

		if (session.getAccount() == ACCOUNT.GLOBAL) {
			uuid = plugin.bank.globalUUID;
		} else {
			uuid = player.getUniqueId();
		}

		for (Material material : items) {
			if (accountHasItem(material, uuid)) {
				if (!specificLetter) {
					session.items.add(material);
					session.amounts.add(bankItemsData.bankItemData.get(material).accountAmounts.get(uuid));
				} else {
					if (getIngameItemName(material.name()).startsWith(letter)) {
						session.items.add(material);
						session.amounts.add(bankItemsData.bankItemData.get(material).accountAmounts.get(uuid));
					}
				}
			}
		}

		session.numOfPages = session.items.size() / 45;
		if (session.items.size() % 45 != 0) {
			session.numOfPages++;
		}

		session.setPageNum(1);
		bankMenus.listOfItemsMenu.openMenuFor(player, 1);
		// ----
	}

	public String getIngameItemName(String name) {
		switch (name) {
		case "COOKED_BEEF":
			return "Steak";
		case "PORKCHOP":
			return "Raw Porkchop";
		case "COD":
			return "Raw Cod";
		case "SALMON":
			return "Raw Salmon";
		case "MUTTON":
			return "Raw Mutton";
		case "CHICKEN":
			return "Raw Chicken";
		case "RABBIT":
			return "Raw Rabbit";
		default:
			return name;
		}
	}

	private boolean accountHasItem(Material material, UUID accountId) {
		if (bankItemsData.bankItemData.get(material).accountAmounts.get(accountId) != null) {
			return true;
		}
		return false;
	}

	private void depositMenuClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();
		Session session = sessions.get(player);

		switch (slotClicked) {
		case 45:
			bankMenus.depositOrWithdrawMenu.openMenuFor(player);
			session.setCurrentMenu(BANKMENU.DEPOSITORWHITDRAW);
			break;
		case 47:
			moveAllItemsDown(event, player);
			break;
		case 49:
			player.closeInventory();
			break;
		case 51:
			moveAllItemsUp(event, player);
			break;
		case 53:
			depositToAccount(event, player, sessions.get(player).getAccount());
			break;
		default:
			break;
		}
	}

	private void moveAllItemsDown(InventoryClickEvent event, Player player) {
		ItemStack[] topContents = event.getInventory().getContents();
		ItemStack[] playerContents = player.getInventory().getContents();

		boolean movedItem = false;

		// For each slot in top menu
		for (int topSlot = 0; topSlot < 45; topSlot++) {
			movedItem = false;
			if (topContents[topSlot] != null) {
				// Search player inv for empty slot big section
				for (int bottomSlot = 9; bottomSlot < 36; bottomSlot++) {
					if (playerContents[bottomSlot] == null) {
						playerContents[bottomSlot] = topContents[topSlot].clone();
						topContents[topSlot] = null;
						movedItem = true;
						break;
					}
				}
				if (movedItem) {
					continue;
				}

				// if big sectio empty
				for (int bottomSlot = 0; bottomSlot < 9; bottomSlot++) {
					if (playerContents[bottomSlot] == null) {
						playerContents[bottomSlot] = topContents[topSlot].clone();
						topContents[topSlot] = null;
						movedItem = true;
						break;
					}
				}
			}
		}

		player.getInventory().setContents(playerContents);
		event.getInventory().setContents(topContents);
	}

	private void moveAllItemsUp(InventoryClickEvent event, Player player) {
		ItemStack[] topContents = event.getInventory().getContents();
		ItemStack[] playerContents = player.getInventory().getContents();

		// For ever slot in player inv
		for (int bottomSlot = 9; bottomSlot < 36; bottomSlot++) {
			if (playerContents[bottomSlot] != null) {
				// Search top inventory for empty slot
				for (int topSlot = 0; topSlot < 45; topSlot++) {
					if (topContents[topSlot] == null) {
						topContents[topSlot] = playerContents[bottomSlot].clone();
						playerContents[bottomSlot] = null;
						break;
					}
				}
			}
		}

		event.getInventory().setContents(topContents);
		player.getInventory().setContents(playerContents);
	}

	private void depositToAccount(InventoryClickEvent event, Player player, ACCOUNT account) {
		ItemStack[] items = event.getInventory().getContents();

		int numberOfItemsDeposited = 0;

		for (int itemNum = 0; itemNum < 45; itemNum++) {
			if (isValidItem(items[itemNum])) {
				if (account == ACCOUNT.GLOBAL) {
					depositItems(items[itemNum], globalUUID);
					updateForOtherPlayersInAmountMenu(player, items[itemNum].getType());
					updateListOfItemsForOtherPlayersAfterDeposit(items[itemNum], !anyItemStacksLeft(itemNum, items));
				} else {
					depositItems(items[itemNum], player.getUniqueId());
				}

				numberOfItemsDeposited += items[itemNum].getAmount();
				items[itemNum] = null;
			}
		}
		event.getInventory().setContents(items);
		if (numberOfItemsDeposited > 0) {
			player.sendMessage(numberOfItemsDeposited + " Items deposited");
		}
		bankItemsData.saveBankData();
	}

	private boolean anyItemStacksLeft(int itemNum, ItemStack[] items) {
		for (int i = 44; i > 0; i--) {
			if (items[i] != null && i > itemNum) {
				return true;
			}
		}

		return false;
	}

	private void updateListOfItemsForOtherPlayersAfterDeposit(ItemStack itemStack, boolean readyToReload) {
		for (Player playerInServer : Bukkit.getOnlinePlayers()) {
			if (sessions.get(playerInServer) != null) {
				Session playerInServerSession = sessions.get(playerInServer);
				if (playerInServerSession.getAccount() == ACCOUNT.GLOBAL) {
					if (playerInServerSession.amounts != null) {
						if (playerInServerSession.items.contains(itemStack.getType())) {
							int indexOfMaterial = playerInServerSession.items.indexOf(itemStack.getType());
							playerInServerSession.amounts.set(indexOfMaterial,
									playerInServerSession.amounts.get(indexOfMaterial) + itemStack.getAmount());
							if (readyToReload) {

								reloadListOfItemsMenuForPlayersInMenu(playerInServer, playerInServerSession);
							}
						}
					}
				}
			}
		}
	}

	private void depositItems(ItemStack items, UUID playerID) {
		ConcurrentHashMap<Material, BankItem> data = bankItemsData.bankItemData;
		Material type = items.getType();

		// ~~~change to contains
		if (data.get(type) == null) {
			data.put(type, new BankItem(type, plugin));
		}
		ConcurrentHashMap<UUID, Integer> accountAmounts = data.get(type).accountAmounts;

		if (accountAmounts.get(playerID) != null) {
			accountAmounts.put(playerID, accountAmounts.get(playerID) + items.getAmount());
		} else {
			accountAmounts.put(playerID, items.getAmount());
		}
	}

	private boolean isValidItem(ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}

		if (itemStack.getItemMeta().getLore() != null) {
			return false;
		}

		if (itemStack.getItemMeta().hasDisplayName()) {
			return false;
		}

		if (itemStack.getItemMeta() instanceof ArmorMeta) {
			ArmorMeta armorMeta = (ArmorMeta) itemStack.getItemMeta();
			if (armorMeta.getTrim() != null) {
				return false;
			}
		}

		switch (itemStack.getType()) {
		case POTION:
		case SPLASH_POTION:
		case LINGERING_POTION:
		case ENCHANTED_BOOK:
		case TIPPED_ARROW:
		case PLAYER_HEAD:
		case FILLED_MAP:
		case TROPICAL_FISH_BUCKET:
		case PUFFERFISH_BUCKET:
		case SALMON_BUCKET:
		case COD_BUCKET:
		case AXOLOTL_BUCKET:
		case TADPOLE_BUCKET:
			return false;
		default:
			break;
		}

		// If item is damaged
		if (((Damageable) itemStack.getItemMeta()).getDamage() != 0) {
			return false;
		}

		// Is a non-empty shulker box
		if (isShulkerBoxWithItems(itemStack)) {
			return false;
		}

		// Item has enchantments
		if (itemStack.getEnchantments().isEmpty() == false) {
			return false;
		}

		// Is a banner with a pattern
		if (isBannerWithPattern(itemStack)) {
			return false;
		}

		// Lodestone Compass
		if (itemStack.getType() == Material.COMPASS && itemStack.getItemMeta().toString().contains("internal=")) {
			return false;
		}

		if (isBundleWithItems(itemStack)) {
			return false;
		}

		// Item is valid
		return true;
	}

	private boolean isBundleWithItems(ItemStack itemStack) {
		if (itemStack.getType().toString().endsWith("BUNDLE")) {
			BundleMeta bundleMeta = (BundleMeta) itemStack.getItemMeta();

			List<ItemStack> items = bundleMeta.getItems();
			return !items.isEmpty();
		}

		return false;
	}

	private boolean isBannerWithPattern(ItemStack itemStack) {
		if (itemStack.getType().toString().endsWith("BANNER")) {
			BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
			if (meta.numberOfPatterns() != 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the itemstack is a shulker box AND has items in it
	 * 
	 * @param itemStack
	 * @return true if itemStack is a shulker box with items in it, false otherwise
	 */
	private boolean isShulkerBoxWithItems(ItemStack itemStack) {
		if (itemStack.getItemMeta() instanceof BlockStateMeta) {
			BlockStateMeta itemMeta = (BlockStateMeta) itemStack.getItemMeta();
			if (itemMeta.getBlockState() instanceof ShulkerBox) {
				ShulkerBox shulkerBox = (ShulkerBox) itemMeta.getBlockState();
				for (ItemStack item : shulkerBox.getInventory().getContents()) {
					if (item != null) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Determines which button was clicked in the "Deposit or Withdraw" menu
	 * 
	 * @param event
	 * @param player
	 */
	private void depositOrWithdrawMenuClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();
		Session session = sessions.get(player);

		switch (slotClicked) {
		case 2:
			bankMenus.depositMenu.openMenuFor(player);
			session.setCurrentMenu(BANKMENU.DEPOSIT);
			break;
		case 6:
			bankMenus.withdrawMenu.openMenuFor(player);
			session.setCurrentMenu(BANKMENU.WITHDRAW);
			break;
		case 9:
			bankMenus.bankManinMenu.openMenuFor(player);
			session.setCurrentMenu(BANKMENU.BANKMAIN);
			break;
		case 13:
			player.closeInventory();
			break;
		default:
			break;
		}
	}

	private void bankMainMenuButtonClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();

		Session session = sessions.get(player);

		switch (slotClicked) {
		case 2:
			session.setAccount(ACCOUNT.GLOBAL);
			bankMenus.depositOrWithdrawMenu.openMenuFor(player);
			session.setCurrentMenu(BANKMENU.DEPOSITORWHITDRAW);
			break;
		case 6:
			session.setAccount(ACCOUNT.PRIVATE);
			bankMenus.depositOrWithdrawMenu.openMenuFor(player);
			session.setCurrentMenu(BANKMENU.DEPOSITORWHITDRAW);
			break;
		case 13:
			player.closeInventory();
			break;
		default:
			break;
		}
	}

	/**
	 * Creates a bank
	 * 
	 * @param player
	 * @param side
	 * @return
	 */
	public void createBank(Player player, int sideNum) {
		/**
		 * side = 0 = front, side = 1 = back, side = 2 = both
		 */

		Block block = player.getTargetBlockExact(5);

		if (!isSign(block.getType())) {
			return;
		}

		Sign sign = (Sign) block.getState();

		if (sideNum == 0 || sideNum == 1) {
			setSignSideText(sign, sideNum == 0 ? Side.FRONT : Side.BACK);
		} else {
			setSignSideText(sign, Side.FRONT);
			setSignSideText(sign, Side.BACK);
		}

		return;
	}

	private void setSignSideText(Sign sign, Side signSide) {
		sign.getSide(signSide).setLine(0, magicLine);
		if (sign instanceof HangingSign) {
			sign.getSide(signSide).setLine(1, ChatColor.BLUE + "~Bank~");
			sign.getSide(signSide).setLine(2, ChatColor.BLACK + "Right click");
		} else {
			sign.getSide(signSide).setLine(1, ChatColor.BLUE + "~Item Bank~");
			sign.getSide(signSide).setLine(2, ChatColor.BLACK + "Right click to use");
		}
		sign.getSide(signSide).setLine(3, magicLine);
		sign.update();
	}
}
