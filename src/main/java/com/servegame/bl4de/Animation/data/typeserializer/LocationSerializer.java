package com.servegame.bl4de.Animation.data.typeserializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * File: LocationSerializer.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 * The main contents of this were copy-pastaed from BlackScarx
 * https://forums.spongepowered.org/t/resolve-add-location-in-config-and-load-it/11659/2
 */
public class LocationSerializer implements TypeSerializer<Location> {
    @Override
    public Location deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String name = value.getNode("WorldName").getValue(TypeToken.of(String.class));
        double X = value.getNode("X").getValue(TypeToken.of(double.class));
        double Y = value.getNode("Y").getValue(TypeToken.of(double.class));
        double Z = value.getNode("Z").getValue(TypeToken.of(double.class));
        if (!Sponge.getServer().getWorld(name).isPresent()) {
            System.out.println("The world don't exist");
            return null;
        }
        World w = Sponge.getServer().getWorld(name).get();
        return w.getLocation(X, Y, Z);
    }

    @Override
    public void serialize(TypeToken<?> type, Location obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("WorldName").setValue(Sponge.getServer().getWorld(obj.createSnapshot().getWorldUniqueId()).get().getName());
        value.getNode("X").setValue(obj.getX());
        value.getNode("Y").setValue(obj.getY());
        value.getNode("Z").setValue(obj.getZ());
    }
}
