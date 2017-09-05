# Minecraft Animations
Author: TheCahyag  
Start Date: 7/20/17  
Description: The plugin has been developed using the SpongeAPI and currently only 
supports uses on SpongeVanilla (Although currently not in a working state). The plugin's
purpose is to easily (hopefully) create frame based animations in Minecraft within a static 3D sub
space, allowing users to easily (hopefully) edit individual frames leading to a animation.

## Command Structure
/animate create \<name>  
/animate delete \<name> -f  
/animate help  
/animate list  
/animate start \<name> [-f\<num>] [-d\<num>] [-c\<num>]  
/animate stop \<name>  
/animate pause \<name>  
/animate \<name> info  
/animate \<name> set <pos1|pos2>  
/animate \<name> set name <new_name>  
/animate \<name> frame create [name]  
/animate \<name> frame delete <name|num> -f  
/animate \<name> frame display <name|num>  
/animate \<name> frame duplicate <name|num> [num]   
/animate \<name> frame list  
/animate \<name> frame update <name|num> -o  
/animate \<name> frame <name|num> info  
/animate \<name> frame <name|num> set name <new_name>

## TODO
* ~~Set warning for a animation that has a large subspace (since it might lag the server)~~
* ~~Set up permissions (kinda done with this, need to add permissions for sub commands and such)~~
    * Setup specific permissions for the usage of flags
    * Permissions that only allow certain values to be set with certain flags
* ~~Add a way to start an animation without a command? Or just force people to use command blocks to run the command~~ (No go)
* ~~Create~~ ~~Format a info message that can be displayed for each animation~~
* ~~Refactor core models to check all input that is given~~
* ~~Refactor ListAnimation command to display buttons that will act as commands~~
* Add .complete() lists on all arguments for tabbing arguments
* Should the BlockSnapshots be able to be converted between a .schematic file for MCEdit and stuff? (Look into this)
* ~~When running /animate list if there are zero animations send a "there are no animations" message~~
    * Maybe have a counter of how many animations there are and how many are being displayed
* Look into making FrameElement and AnimationElement
* ~~Should the state of the subspace be saved before an animation is played, stuff other than the animation stuff?~~ Not sure what I meant by this
* Provide flexibility to change the subspace corners after frames are made, or if the corner is set to something different just change all the frames (make sure to give a warning)
* Fix the buttons so that clicking on the spaces don't execute the command
* Implement pause operation
    * Should stopping an animation remove all visible contents of the animation
    * Pausing should keep track of the current frame it is on
* Make a config where the user can specify the default values of animation's states (frame, delay, cycles) or just other config stuff
* ~~Register the commands that arn't registered see InfoAnimation~~
* ~~Make specific response messages for every command rather than generic messages?~~
* Fix /animate delete sub command
* Use pageation to display only a certain number of frames and animations when listing the frames and animations
* Make a animation's subspace the place where the animation is played, but allow a different subspace for the frame so the user can place all frames down at the same time.
* When a user updates a frame it would be cool to show the additions and deletions.
   * Additions being when a block goes from air to not air
   * Deletions being when a block goes from not air to air
   * And if a block changes that would be both a deletion and an addition
* Add sound to animations? Allow adding a sound or a list of sounds to play among rendering a frame of a animation
* If the animation is already running and the user calls /animate start <name> and specifies a flag, those flag values should update on the currently playing animation (this would probably require stopping the animation, changing the internal states and then starting the animation backup on the frame it stopped on)
* Add condition for pausing and playing an animation
    * An animation can only be played if it's initialized and has more than one frame
    * An animation can only be paused if the current status is a played status
* Add a message in the bottom middle of the users screen showing what frame the animation is on
* Fix NPE from logger

## Animations to show off
* Door opening
* Bridge extending
* Running horse
* Fan spinning
* Grandfather clock with the pendulum ticking and tocking
* Big tree that sways in the breeze

### Testing / Stress testing
* Local server
* Public server on the box

## Bug List
* Sometimes when running a command the server will stop for 40.7 seconds (1.11.2|6.1.0)
* If the animation database file is being used an error is thrown to console, this needs to be accounted for so that when the database file can't be used the plugin is disabled


## Random Notes for me
If Sponge changes the blocks at a chunk that isn't loaded and there's no one to see the changes, 
did Sponge change the blocks?  
Make a frame library? For each person? (Don't really think this would work out that well)

See Location#addScheduledUpdate()  
Checkout MagicaVoxel
