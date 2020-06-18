package me.ItemBank.main;

import java.util.ArrayList;
import org.bukkit.Material;
import me.ItemBank.main.BankMenus.BANKMENU;

public class Session {
	private BANKMENU currentMenu;

//	private Player player;
	private ACCOUNT account;
	// ListOfItems page (Withdraw)
	private int pageNum;
	ArrayList<Material> items;
	ArrayList<Integer> amounts;
	int numOfPages;
	// Choose amount page
	private int maxAmount;
	private int amountSelected;
	private Material materialSelected;
	private String category;
	private boolean cameFromCategories;

	public Session() {
		cameFromCategories = false;
	}

	public boolean cameFromCategories() {
		return cameFromCategories;
	}

	public void setCameFromCategories(boolean cameFromCategories) {
		this.cameFromCategories = cameFromCategories;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ACCOUNT getAccount() {
		return account;
	}

	public void setAccount(ACCOUNT account) {
		this.account = account;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}

	public int getAmountSelected() {
		return amountSelected;
	}

	public void setAmountSelected(int amountSelected) {
		if (amountSelected > maxAmount) {
			this.amountSelected = maxAmount;
		} else if (amountSelected < 0) {
			this.amountSelected = 0;
		} else {
			this.amountSelected = amountSelected;
		}
	}

	public Material getMaterialSelected() {
		return materialSelected;
	}

	public void setMaterialSelected(Material materialSelected) {
		this.materialSelected = materialSelected;
	}

	public BANKMENU getCurrentMenu() {
		return currentMenu;
	}

	public void setCurrentMenu(BANKMENU currentMenu) {
		this.currentMenu = currentMenu;
	}

	public int getNumOfPages() {
		return numOfPages;
	}

	public void setNumOfPages(int numOfPages) {
		this.numOfPages = numOfPages;
	}

	enum ACCOUNT {
		GLOBAL, PRIVATE
	}
}
