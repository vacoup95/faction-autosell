package com.minecraftercity.chestsell;

import org.bukkit.Location;

import java.util.List;

public class RunnableBalancer implements Runnable
{
    public Main main;

    public RunnableBalancer(Main main)
    {
        this.main = main;
    }

    @Override
    public void run()
    {
        Config config = new Config(this.main);
        List<Location> locations = (List<Location>) config.getLocations();
        for(Location location : locations) {
            this.main.getServer()
                    .getScheduler()
                    .runTaskTimer(this.main, new RunnableSeller(this.main, location), 50, 100);
        }
    }
}
