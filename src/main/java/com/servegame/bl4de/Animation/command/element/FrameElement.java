package com.servegame.bl4de.Animation.command.element;

import com.servegame.bl4de.Animation.model.Frame;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * File: FrameElement.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class FrameElement extends CommandElement {

    private Frame frame;

    /**
     * TODO
     * @param key
     * @param frame
     */
    public FrameElement(Text key, Frame frame){
        super(key);
        this.frame = frame;
    }

    @Nullable
    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        return null;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return null;
    }

    public Optional<Frame> getFrame() {
        return Optional.ofNullable(this.frame);
    }
}
