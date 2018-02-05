# Minecraft Animations
* [Source](https://github.com/TheCahyag/MinecraftAnimation)
* [Issues](https://github.com/TheCahyag/MinecraftAnimation/issues)
* [Downloads](https://github.com/TheCahyag/MinecraftAnimation/releases)
* [Planned Enhancements](https://github.com/TheCahyag/MinecraftAnimation/projects)

**Current Active Branches are:**  
* sponge-api/7 (this branch, for 1.12.2)
* ~~sponge-api/6.1 (for 1.11.2)~~ Discontinued with v0.3.0, not much need for 1.11.2 versions. Message me if I'm wrong.

Author: TheCahyag  
Start Date: 7/20/17  
Objective: To create an intuitive tool that allows for the creation of frame-based animations in Minecraft.  
Description: The plugin has been developed using the SpongeAPI and currently only supports use on SpongeVanilla (will expand to SpongeForge soon). The plugin's
purpose is to easily create frame-based animations in Minecraft within a static 3D sub
space (possibly a dynamic subspace in the far future), allowing users to easily edit individual frames leading to an animation.  
Like this:  
<a href="https://media.giphy.com/media/3oFzm2IvnWvpGMYfh6/giphy.gif"><img src="https://media.giphy.com/media/3oFzm2IvnWvpGMYfh6/giphy.gif" title="Smiley face" /></a>  
See more [here](https://github.com/TheCahyag/MinecraftAnimation/blob/sponge-api/7/EXAMPLES.md)!

#### Known bugs / Not Implemented Yet
* Blocks that hold inventory don't retain their data

## Contributions
Contributions are always welcome! Please see [Contributions.md](https://github.com/TheCahyag/MinecraftAnimation/blob/sponge-api/7/CONTRIBUTIONS.md) for more information on contributing.

## License
This project and is licensed under the MIT License. See [License.md](https://github.com/TheCahyag/MinecraftAnimation/blob/sponge-api/7/LICENSE) for more info.

## Random Notes for me
If Sponge changes the blocks at a chunk that isn't loaded and there's no one to see the changes, 
did Sponge change the blocks?  
Make a frame library? For each person? (Don't really think this would work out that well)

See Location#addScheduledUpdate()  
Checkout MagicaVoxel
