# Minecraft Animations
Author: TheCahyag  
Start Date: 7/20/17  
Description: The plugin has been developed using the SpongeAPI and currently only 
supports uses on SpongeVanilla (Although currently not in a working state). The plugin's
purpose is to easily (hopefully) create frame based animations in Minecraft within a static 3D sub
space, allowing users to easily (hopefully) edit individual frames leading to a animation.

## TODO
* Set warning for a animation that has a large subspace (since it might lag the server)
* Set up permissions
* Implement commands
    * ~~/animate (Can't be called, but finished)~~
    * ~~/animate create \<name>~~
    * ~~/animate delete \<name> -f~~
    * /animate start \<name> -f\<num> -d\<num> -c\<num>
    * /animate stop \<name>
    * /animate list
    * /animate \<name> info/animate \<name> frame create <name> -h
    * /animate \<name> frame delete <name|num> -f
    * /animate \<name> frame display <name|num>
    * /animate \<name> frame duplicate <name|num> [num]
    * /animate \<name> frame <name|num> info
    * /animate \<name> frame list
    * /animate \<name> frame update <name|num> -o
* Add a way to start an animation without a command? Or just force people to use command blocks to run the command
* ~~Create~~ Format a info message that can be displayed for each animation
* Refactor core models to check all input that is given
* Refactor ListAnimation command to display buttons that will act as commands
* Add .complete() lists on all arguments for tabbing arguments
* Should the BlockSnapshots be able to be converted between a .schematic file for MCEdit and stuff? (Look into this)
* When running /animate list if there are zero animations send a "there are no animations" message
    * Maybe have a counter of how many animations there are and how many are being displayed
* Look into making FrameElement and AnimationElement



## Random Notes for me
If Sponge changes the blocks at a chunk that isn't loaded and there's no one to see the changes, 
did Sponge change the blocks?  
Make a frame library? For each person? (Don't really think this would work out that well)

See Location#addScheduledUpdate()  
Checkout MagicaVoxel
