package com.servegame.bl4de.Animation.command.element;

import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * File: AnimationElement.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationElement extends CommandElement {

    private Animation animation;

    /**
     * TODO
     * @param key
     * @param name
     * @param owner
     */
    public AnimationElement(Text key, String name, UUID owner){
        super(key);
        this.animation = AnimationController.getAnimation(name, owner).orElse(null);
    }

    @Nullable
    @Override
    protected Optional<Animation> parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        return null;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return null;
    }

    /**
     * Getter for the {@link Animation}
     * @return Optional of the animation
     */
    public Optional<Animation> getAnimation(){
        return Optional.ofNullable(this.animation);
    }
}
