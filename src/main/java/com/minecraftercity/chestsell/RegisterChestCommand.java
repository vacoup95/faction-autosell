package com.minecraftercity.chestsell;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class RegisterChestCommand implements CommandExecutor
{
    private final Main main;

    public RegisterChestCommand(Main main)
    {
        this.main = main;
    }

    public boolean registerChest(Location location)
    {
        Config config = new Config(this.main);
        List<Location> getLocations = (List<Location>) config.getLocations();

        if(getLocations == null) {
            getLocations = new ArrayList<Location>();
        }
        getLocations.add(location);

        config.setLocations(getLocations);
        return true;
    }

    public boolean playerAttemptRegister(Player player)
    {
        Block b = player.getTargetBlock(null, 5);
        Material chest = Material.getMaterial("CHEST");

        return b.getType() == chest && this.registerChest(b.getLocation());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(!this.playerAttemptRegister(player)) {
                player.sendMessage("Please point at a chest to register one!");
                return true;
            }
            player.sendMessage("Chest registered successfully");
            return true;
        }
        return false;
    }
}
