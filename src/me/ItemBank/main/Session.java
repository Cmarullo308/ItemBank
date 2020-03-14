package me.ItemBank.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Session {
	private Player player;
	private ACCOUNT account;
	// ListOfItems page (Withdraw
	private int pageNum;
	ArrayList<Material> items;
	ArrayList<Integer> amounts;
	int numOfPages;
	// Choose amount page
	private int maxAmount;
	private int amountSelected;
	private Material materialSelected;

	public Session(Player player) {
		this.player = player;
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

	enum ACCOUNT {
		GLOBAL, PRIVATE
	}
}
