# Minecraft Animations
Author: TheCahyag  
Start Date: 7/20/17  
Objective: To create an intuitive tool that allows for the creation of frame-based animations in Minecraft.  
Description: The plugin has been developed using the SpongeAPI and currently only
supports use on SpongeVanilla. The plugin's
purpose is to easily create frame-based animations in Minecraft within a static 3D sub
space (possibly a dynamic subspace in the far future), allowing users to easily edit individual 
frames leading to an animation.

## Current State of the Plugin
The Animation plugin works mostly as intended. The max animation size has been scaled up significantly.

See the wiki for the commands and their respective usages. (Still a WIP)

#### Known bugs / Not Implemented Yet
* Many tile entities are wonky and ~~mostly don't work~~ don't retain their data
* At a certain point the animation becomes too big and won't load -- leads to a OutOfMemory exception
* Pause button does nothing
* Settings button does nothing

#### Matt Notes
* Pausing the the animation should stop the animation on the given frame, 

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

### Testing / Stress testing
* Local server
* Public server on the box
* Find a point at which the number of block changes lag the server

## Bug List
* ~~Sometimes when running a command the server will stop for 40.7 seconds (1.11.2|6.1.0)~~ Who knows what this was...
* If the animation database file is being used an error is thrown to console, this needs to be accounted for so that when the database file can't be used the plugin is disabled
* Removing a frame that doesn't exist -- /animate name frame delete_frame 0 will throw an error if there are no frames for the given animation
* Flag edge cases: Some value flags don't ensure the number specified are valid

## Random Notes for me
If Sponge changes the blocks at a chunk that isn't loaded and there's no one to see the changes, 
did Sponge change the blocks?  
Make a frame library? For each person? (Don't really think this would work out that well)

See Location#addScheduledUpdate()  
Checkout MagicaVoxel
