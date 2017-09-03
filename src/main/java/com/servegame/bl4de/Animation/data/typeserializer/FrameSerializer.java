package com.servegame.bl4de.Animation.data.typeserializer;

import com.google.common.reflect.TypeToken;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.model.SubSpace3D;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.UUID;

/**
 * File: FrameSerializer.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class FrameSerializer implements TypeSerializer<Frame> {
    @Override
    public Frame deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        UUID creator = value.getNode("creator").getValue(TypeToken.of(UUID.class));
        String name = value.getNode("name").getString();
        SubSpace3D subSpace3D = value.getNode("subSpace").getValue(TypeToken.of(SubSpace3D.class));
        return new Frame(creator, name, subSpace3D);
    }

    @Override
    public void serialize(TypeToken<?> type, Frame obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("creator").setValue(obj.getCreator());
        value.getNode("name").setValue(obj.getName());
        value.getNode("subSpace").setValue(obj.getSubspace());
    }
}
