package me.ItemBank.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.ItemBank.main.Session.ACCOUNT;
import net.md_5.bungee.api.ChatColor;

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
		} else if (inventoryName.equals(bankMenus.amountMenu.menuName)) {
			if (event.getRawSlot() < 27) {
				event.setCancelled(true);
			}
		}
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
		} else if (slotClicked == 47) {
			if (event.getInventory().getContents()[slotClicked].getType() == Material.LIME_STAINED_GLASS_PANE) {
				pageNum = session.getPageNum() - 1;
				session.setPageNum(pageNum);
				bankMenus.listOfItemsMenu.openMenuFor(player, pageNum);
			}
		}
	}

	private void withdrawButtonClicked(InventoryClickEvent event, Player player) {
		int slotClicked = event.getRawSlot();

		if (slotClicked == 9) {
			openListOfItemsMenu(player, "all");
		}
	}

	private void openListOfItemsMenu(Player player, String letter) {

		// All lettered items
		if (letter.equalsIgnoreCase("all")) {
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
			for (Material material : items) {
				if (session.getAccount() == ACCOUNT.GLOBAL) {
					if (globalAccountHasItem(material)) {
						session.items.add(material);
						session.amounts.add(bankItemsData.bankItemData.get(material).accountAmounts.get(globalUUID));
					}
				}
			}

			session.setPageNum(1);
			bankMenus.listOfItemsMenu.openMenuFor(player, 1);
		}
		// ----
	}

	private boolean globalAccountHasItem(Material material) {
		if (bankItemsData.bankItemData.get(material).accountAmounts.get(globalUUID) != null) {
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
		case 49:
			player.closeInventory();
			break;
		case 53:
			depositToGlobalAccount(event, player, sessions.get(player).getAccount());
			break;
		default:
			break;
		}
	}

	private void depositToGlobalAccount(InventoryClickEvent event, Player player, ACCOUNT account) {
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

		return true;
	}

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
