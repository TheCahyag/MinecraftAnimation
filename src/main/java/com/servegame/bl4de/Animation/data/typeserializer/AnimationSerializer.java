package com.servegame.bl4de.Animation.data.typeserializer;

import com.google.common.reflect.TypeToken;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.model.SubSpace3D;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.List;
import java.util.UUID;

/**
 * File: AnimationSerializer.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationSerializer implements TypeSerializer<Animation> {
    @Override
    public Animation deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        // Essential parts
        UUID owner = UUID.fromString(value.getNode("owner").getString());
        String name = value.getNode("name").getString();

        // Extra metadata
        int status = value.getNode("status").getInt();
        List<Frame> frames = value.getNode("frames").getList(TypeToken.of(Frame.class));
        //SubSpace3D subSpace = value.getNode("subSpace").getValue(TypeToken.of(SubSpace3D.class));
        int frameIndex = value.getNode("frameIndex").getInt();
        int tickDelay = value.getNode("tickDelay").getInt();
        int cycles = value.getNode("cycles").getInt();
        Animation animation = new Animation(owner, name);
        animation.setStatusAsInt(status);
        animation.setFrames(frames);
        //animation.setSubSpace(subSpace);
        animation.setFrameIndex(frameIndex);
        animation.setTickDelay(tickDelay);
        animation.setCycles(cycles);
        return animation;
    }

    @Override
    public void serialize(TypeToken<?> type, Animation obj, ConfigurationNode value) throws ObjectMappingException {
        // Essential parts
        value.getNode("owner").setValue(obj.getOwner().toString());
        value.getNode("name").setValue(obj.getAnimationName());

        // Extra metadata
        value.getNode("status").setValue(obj.getStatusAsInt());
        value.getNode("frames").setValue(obj.getFrames());

        value.getNode("frameIndex").setValue(obj.getFrameIndex());
        value.getNode("tickDelay").setValue(obj.getTickDelay());
        value.getNode("cycles").setValue(obj.getCycles());
        //value.getNode("subSpace").setValue(obj.getSubSpace());
    }
}
