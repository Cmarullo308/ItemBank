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
	// Choose amount page
	private int maxAmount;
	private int amountSelected;

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
		this.amountSelected = amountSelected;
	}

	enum ACCOUNT {
		GLOBAL, PRIVATE
	}
}
