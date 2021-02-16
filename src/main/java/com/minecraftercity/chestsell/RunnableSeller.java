package com.minecraftercity.chestsell;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import java.io.IOException;


public class RunnableSeller implements Runnable
{
    public Main main;
    public Location location;

    public RunnableSeller(Main main, Location location)
    {
        this.main = main;
        this.location = location;
    }

    @Override
    public void run()
    {
        if(location.getBlock().getType() == Material.CHEST) {
            Chest chest = ((Chest) location.getBlock().getState());
            ItemStack[] items = chest.getInventory().getContents();
            FileConfiguration config = null;
            try {
                config = this.main.getCustomConfig();
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            if(items.length > 0 ) {
                for (ItemStack item : items) {
                    if(item != null) {
                        int price = config.getInt(item.getType().toString(), 0);

                        FLocation flocation = new FLocation(this.location);
                        Faction faction = Board.getInstance().getFactionAt(flocation);

                        if(price > 0 && !faction.isWilderness()
                                && !faction.isSafeZone()
                                && !faction.isWarZone()) {
                            double totalPrice = item.getAmount() * price;
                            Economy economy = (Economy) Main.getEconomy();
                            economy.depositPlayer(faction.getOfflinePlayer(), totalPrice);

                            chest.getInventory().remove(item);
                        }
                    }
                }
            }
        } else {
            Config config = new Config(this.main);
            config.removeLocation(location);
        }

    }
}
