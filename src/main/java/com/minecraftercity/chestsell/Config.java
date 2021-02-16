package com.minecraftercity.chestsell;

import org.bukkit.Location;
import java.util.List;

public class Config
{
    private Main main;

    public Config(Main main)
    {
        this.main = main;
    }

    public void save()
    {
        this.main.saveConfig();
    }

    public List<?> getLocations()
    {
        return this.main.getConfig().getList("locations", null);
    }

    public void removeLocation(Location location)
    {
        List<Location> locations = (List<Location>) this.main.getConfig().getList("locations", null);
        locations.remove(location);

        this.setLocations(locations);
    }

    public void setLocations(List<Location> location)
    {
        this.main.getConfig().set("locations", location);
        this.save();
    }
}
