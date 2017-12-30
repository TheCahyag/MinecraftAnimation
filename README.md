# Minecraft Animations
Author: TheCahyag  
Start Date: 7/20/17  
Objective: To create an intuitive tool that allows for the creation of frame-based animations in Minecraft.  
Description: The plugin has been developed using the SpongeAPI and currently only supports use on SpongeVanilla (will expand to SpongeForge soon). The plugin's
purpose is to easily create frame-based animations in Minecraft within a static 3D sub
space (possibly a dynamic subspace in the far future), allowing users to easily edit individual frames leading to an animation. Like this:  
<a href="https://media.giphy.com/media/3oFzm2IvnWvpGMYfh6/giphy.gif"><img src="https://media.giphy.com/media/3oFzm2IvnWvpGMYfh6/giphy.gif" title="Smiley face" /></a>

#### Known bugs / Not Implemented Yet
* Blocks that hold inventory don't retain their data
* Big animations will take awhile to load which leads to the server lagging. (An Animation that has 4 frames with each holding 15000 blocks of data will take ~7 seconds to load, this will be optimized in the future)
* Settings button does nothing (Will be updated in the UI Enhancement update)

## Animations to show off
* Door opening
* Bridge extending
* Running horse
* Fan spinning
* Grandfather clock with the pendulum ticking and tocking
* Big tree that sways in the breeze
* Two moving arrows pointing towards a door
* Christmas tree with lights blinking
* Arcade machine with the screen playing an animation

## Bug List
* If the animation database file is being used an error is thrown to console, this needs to be accounted for so that when the database file can't be used the plugin is disabled
* Removing a frame that doesn't exist -- /animate name frame delete_frame 0 will throw an error if there are no frames for the given animation
* Flag edge cases: Some value flags don't ensure the number specified are valid

## Random Notes for me
If Sponge changes the blocks at a chunk that isn't loaded and there's no one to see the changes, 
did Sponge change the blocks?  
Make a frame library? For each person? (Don't really think this would work out that well)

See Location#addScheduledUpdate()  
Checkout MagicaVoxel
