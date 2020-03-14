package me.ItemBank.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.ItemBank.main.Session.ACCOUNT;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.Items;

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

	@EventHandler
	public void playerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = e.getClickedBlock();
			if (isSign(e.getClickedBlock().getType())) {
				Sign sign = (Sign) block.getState();
				if (isBankSign(sign)) {
					sessions.put(player, new Session(player));
					bankMenus.bankManinMenu.openMenuFor(player);
				}
			}
		}
	}

	private boolean isBankSign(Sign sign) {
		if (sign.getLine(0).equals(magicLine)) {
			return true;
		}
		return false;
	}

	private boolean isSign(Material type) {
		switch (type) {
		case OAK_WALL_SIGN:
		case BIRCH_WALL_SIGN:
		case JUNGLE_WALL_SIGN:
		case DARK_OAK_WALL_SIGN:
		case ACACIA_WALL_SIGN:
		case SPRUCE_WALL_SIGN:
		case OAK_SIGN:
		case BIRCH_SIGN:
		case JUNGLE_SIGN:
		case DARK_OAK_SIGN:
		case ACACIA_SIGN:
		case SPRUCE_SIGN:
			return true;
		default:
			return false;
		}
	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();

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
		plugin.consoleMessage(itemStack.toString() + "\n");
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
		} else if (inventoryName.equals(bankMenus.depositOrWithdrawMenu.menuName)) {
			if (event.getRawSlot() < 18) {
				event.setCancelled(true);
				depositOrWithdrawMenuClicked(event, player);
			}
		} else if (inventoryName.equals(bankMenus.depositMenu.menuName)) {
			if (event.getRawSlot() > 44 && event.getRawSlot() < 54) {
				event.setCancelled(true);
				depositMenuClicked(event, player);
			}
		} else if (inventoryName.equals(bankMenus.withdrawMenu.menuName)) {
			if (event.getRawSlot() < 45) {
				event.setCancelled(true);
				withdrawButtonClicked(event, player);
			}
		} else if (isListOfItemsMenu(inventoryName)) {
			if (event.getRawSlot() < 54) {
				event.setCancelled(true);
				listOfItemsMenuButtonClicked(event, player);
			}
		} else if (isAmountMenu(inventoryName)) {
			if (event.getRawSlot() < 27) {
				event.setCancelled(true);
				amountMenuClicked(event, player);
			}
		}
	}

	private void amountMenuClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();
		Session session = sessions.get(player);

		switch (slotClicked) {
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
		case 18:
			bankMenus.listOfItemsMenu.openMenuFor(player, sessions.get(player).getPageNum());
			break;
		case 22:
			player.closeInventory();
			break;
		case 26:
			withdrawItemsToPlayer(player);
			session.setAmountSelected(0);
			bankMenus.amountMenu.openMenuFor(player, session.getMaterialSelected());
			break;
		default:
			break;
		}
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
			item.accountAmounts.put(globalUUID, item.accountAmounts.get(globalUUID) - amountSelect);
		} else if (account == ACCOUNT.PRIVATE) {
			item.accountAmounts.put(player.getUniqueId(), item.accountAmounts.get(player.getUniqueId()) - amountSelect);
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
			}
		} else if (slotClicked == 45) {
			bankMenus.withdrawMenu.openMenuFor(player);
		} else if (slotClicked == 49) {
			player.closeInventory();
		} else if (slotClicked == 47) {
			if (event.getInventory().getContents()[slotClicked].getType() == Material.LIME_STAINED_GLASS_PANE) {
				pageNum = session.getPageNum() - 1;
				session.setPageNum(pageNum);
				bankMenus.listOfItemsMenu.openMenuFor(player, pageNum);
			}
		} else if (slotClicked >= 0 && slotClicked <= 44) {
			if (event.getInventory().getContents()[slotClicked] != null) {
				session.setMaterialSelected(event.getInventory().getContents()[slotClicked].getType());
				bankMenus.amountMenu.openMenuFor(player, event.getInventory().getContents()[slotClicked].getType());
			}
		}
	}

	private void withdrawButtonClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();

		if (slotClicked >= 9 && slotClicked <= 35) {
			openListOfItemsMenu(player, event.getInventory().getContents()[slotClicked].getItemMeta().getDisplayName());
		} else if (slotClicked == 36) {
			bankMenus.depositOrWithdrawMenu.openMenuFor(player);
		} else if (slotClicked == 40) {
			player.closeInventory();
		}
	}

	private void openListOfItemsMenu(Player player, String letter) {

		// All lettered items
		ArrayList<Material> items = new ArrayList<Material>();
		// Get all materials in bank
		for (Material material : bankItemsData.bankItemData.keySet()) {
			items.add(material);
		}

		// Alphabetize items
		Collections.sort(items);

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
					if (material.name().startsWith(letter)) {
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

	private boolean accountHasItem(Material material, UUID accountId) {
		if (bankItemsData.bankItemData.get(material).accountAmounts.get(accountId) != null) {
			return true;
		}
		return false;
	}

	private void depositMenuClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();

		switch (slotClicked) {
		case 45:
			bankMenus.depositOrWithdrawMenu.openMenuFor(player);
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

		for (int itemNum = 0; itemNum < 45; itemNum++) {
			if (isValidItem(items[itemNum])) {
				if (account == ACCOUNT.GLOBAL) {
					depositItems(items[itemNum], globalUUID);
				} else {
					depositItems(items[itemNum], player.getUniqueId());
				}

				items[itemNum] = null;
			}
		}
		event.getInventory().setContents(items);
		player.sendMessage("Items deposited");
		bankItemsData.saveBankData();
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

		switch (itemStack.getType()) {
		case POTION:
		case SPLASH_POTION:
		case LINGERING_POTION:
		case ENCHANTED_BOOK:
		case TIPPED_ARROW:
		case PLAYER_HEAD:
			return false;
		default:
			break;
		}

		// If item is damaged
		if (((Damageable) itemStack.getItemMeta()).getDamage() != 0) {
			return false;
		}

		if (isShulkerBoxWithItems(itemStack)) {
			return false;
		}

		if (itemStack.getEnchantments().isEmpty() == false) {
			return false;
		}

		if (isBannerWithPattern(itemStack)) {
			return false;
		}

		// Item is valid
		return true;
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

		switch (slotClicked) {
		case 2:
			bankMenus.depositMenu.openMenuFor(player);
			break;
		case 6:
			bankMenus.withdrawMenu.openMenuFor(player);
			break;
		case 9:
			bankMenus.bankManinMenu.openMenuFor(player);
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

		switch (slotClicked) {
		case 2:
			sessions.get(player).setAccount(ACCOUNT.GLOBAL);
			bankMenus.depositOrWithdrawMenu.openMenuFor(player);
			break;
		case 6:
			sessions.get(player).setAccount(ACCOUNT.PRIVATE);
			bankMenus.depositOrWithdrawMenu.openMenuFor(player);
			break;
		case 13:
			player.closeInventory();
			break;
		default:
			break;
		}
	}

	public void createBank(Player player) {
		Block block = player.getTargetBlockExact(5);

		if (isSign(block.getType())) {
			Sign sign = (Sign) block.getState();
			sign.setLine(0, magicLine);
			sign.setLine(1, ChatColor.BLUE + "~Item Bank~");
			sign.setLine(2, ChatColor.BLACK + "Right click to use");
			sign.setLine(3, magicLine);
			sign.update();
			player.sendMessage("ee");
		}
	}
}
