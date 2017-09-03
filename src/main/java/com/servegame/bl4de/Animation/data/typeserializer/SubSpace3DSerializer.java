package com.servegame.bl4de.Animation.data.typeserializer;

import com.google.common.reflect.TypeToken;
import com.servegame.bl4de.Animation.model.SubSpace3D;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.world.Location;

/**
 * File: SubSpace3DSerializer.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SubSpace3DSerializer implements TypeSerializer<SubSpace3D> {
    @Override
    public SubSpace3D deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        Location cornerOne = value.getNode("cornerOne").getValue(TypeToken.of(Location.class));
        Location cornerTwo = value.getNode("cornerTwo").getValue(TypeToken.of(Location.class));
       // BlockSnapshot[][][] contents = value.getNode("contents").getValue(TypeToken.of(BlockSnapshot.class));
        return new SubSpace3D(cornerOne, cornerTwo);
    }

    @Override
    public void serialize(TypeToken<?> type, SubSpace3D obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("cornerOne").setValue(obj.getCornerOne().get());
        value.getNode("cornerTwo").setValue(obj.getCornerTwo().get());
    }
}
