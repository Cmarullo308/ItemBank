package SpigotTools;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackEditor {
	public static void setDisplayName(ItemStack itemStack, String name) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
	}
}
