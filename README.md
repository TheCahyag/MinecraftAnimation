# Minecraft Animations
Author: TheCahyag  
Start Date: 7/20/17  
Objective: To create an intuitive tool that allows for the creation of frame-based animations in Minecraft.  
Description: The plugin has been developed using the SpongeAPI and currently only
supports use on SpongeVanilla (Although currently in a semi-working state -- see plugin state below). The plugin's
purpose is to easily (hopefully) create frame-based animations in Minecraft within a static 3D sub
space (possibly a dynamic subspace in the far future), allowing users to easily (hopefully) edit individual 
frames leading to an animation.

## Current State of the Plugin
The Animation plugin works mostly as intended. The max animation size has been scaled up significantly.

See the wiki for the commands and their respective usages. (Still a WIP)

## TODO
### High Priority
[BUGFIX] After playing the animation for awhile the frames fall out of sync
### Medium Priority
[WIKI] Write basic usage guidelines  
[PERMISSIONS] Setup specific permissions for the usage of flags  
[CODE STRUCTURE] Look into making FrameElement and AnimationElement  
[FEEDBACK] Send a message for when an animation starts, stops, or pauses  
[ENHANCEMENT] Implement pause operation
* Should stopping an animation remove all visible contents of the animation
* Pausing should keep track of the current frame it is on  

[BUGFIX] Setting the cycles when starting an animation doesn't do anything  
[REQUIREMENT] Define characters that are illegal to have in a frame/animation name  
[BUGFIX] There's a problem when displaying the volume of small subspaces, a 1x4x1 will have a volume of 0  

### Low Priority
[PERMISSIONS] Permissions that only allow certain values to be set with certain flags  
[COMMAND] Add .complete() lists on all arguments for tabbing arguments  
[BOTHERING] Fix the buttons so that clicking on the spaces don't execute the command  
[BOTHERING] Fix the method to create frame names, it doesn't reset for every animation  
[ENHANCEMENT] Make a config where the user can specify the default values of animation's states 
(frame, delay, cycles) or just other config stuff  
[ENHANCEMENT] Use pageation to display only a certain number of frames and animations when listing 
the frames and animations  
[ENHANCEMENT] Allow animations to be public, which would allow anyone to start the given animation  
[ENHANCEMENT] Allow animations to be started without a player present (which would hopefully allow stuff)  
### Backlog
[ENHANCEMENT] Should the BlockSnapshots be able to be converted between a .schematic file for 
MCEdit and stuff? (Look into this)  
[ENHANCEMENT] Provide flexibility to change the subspace corners after frames are made, or if 
the corner is set to something different just change all the frames (make sure to give a warning)  
[ENHANCEMENT] Add options to the way the animation loops
* Sequentially 
* Random order
* Sequentially then reverse (0, 1, ..., n, n, n-1, n-2, n-..., 0)  

[ENHANCEMENT] Have a "up-time" for each frame in an animation instead of having an animation wide 
delay time. So you could tell the first frame to be live for 30 ticks, but the next frame would be 
live for 5.  
[ENHANCEMENT] What if we have an edit mode? 
* An item can be used for switching between frames. So while in edit mode you can use a stick (or something) to go forward a frame (left click) or go back a frame (right click)
* Would we set a tool for saving changes? ~~So they would switch between a tool for changing frames and a tool for saving?~~ Or when they switch between frames it would save automatically

[CODE STRUCTURE] When animations get very large with lots of frames and lots of data, 
it's going to take a really long time to load. Possible solution: We have a table for each 
animation that for every row represents one frame. When loading in the animation we can create
one thread for every row in the table that will load in one frame and add it to the animation.
When all the threads are done executing that will tell us the animation is fully created.  
### Done
* ~~Set warning for a animation that has a large subspace (since it might lag the server)~~
* ~~Set up permissions (kinda done with this, need to add permissions for sub commands and such)~~
* ~~Add a way to start an animation without a command? Or just force people to use command blocks to run the command~~ (No go)
* ~~Create~~ ~~Format a info message that can be displayed for each animation~~
* ~~Refactor core models to check all input that is given~~
* ~~Refactor ListAnimation command to display buttons that will act as commands~~
* ~~When running /animate list if there are zero animations send a "there are no animations" message~~
    * ~~Maybe have a counter of how many animations there are and how many are being displayed~~
* ~~Should the state of the subspace be saved before an animation is played, stuff other than the animation stuff?~~ Not sure what I meant by this
* ~~Register the commands that arn't registered see InfoAnimation~~
* ~~Make specific response messages for every command rather than generic messages?~~
* ~~Fix /animate delete sub command~~  
* [BUGFIX] ~~Fix Mark Invalid error when un-serializing data~~
* ~~Add condition for pausing and playing an animation~~
    * ~~An animation can only be played if it's initialized and has more than one frame~~
    * ~~An animation can only be paused if the current status is a played status~~
* ~~In the frame info view if there are contents show the number of not air blocks~~
* ~~In the animation info view add a line for the volume of the master subspace (no longer think I want to do this)~~
to sort...
* Make a animation's subspace the place where the animation is played, but allow a different subspace for the frame so the user can place all frames down at the same time.
* When a user updates a frame it would be cool to show the additions and deletions.
   * Additions being when a block goes from air to not air
   * Deletions being when a block goes from not air to air
   * And if a block changes that would be both a deletion and an addition
* Add sound to animations? Allow adding a sound or a list of sounds to play among rendering a frame of a animation
* If the animation is already running and the user calls /animate start <name> and specifies a flag, those flag values should update on the currently playing animation (this would probably require stopping the animation, changing the internal states and then starting the animation backup on the frame it stopped on)
* Add a message in the bottom middle of the users screen showing what frame the animation is on
* Fix NPE from logger (little vague)
* For the time inbetween frames, they shouldn't be able to give a number > 1. Should there be a ceiling?
* After an animation is created should it automatically call /animate <name> info or /animate list to show the newly created animation

## Animations to show off
* Door opening
* Bridge extending
* Running horse
* Fan spinning
* Grandfather clock with the pendulum ticking and tocking
* Big tree that sways in the breeze
* Two moving arrows pointing towards a door

### Testing / Stress testing
* Local server
* Public server on the box
* Find a point at which the number of block changes lag the server

## Bug List
* Sometimes when running a command the server will stop for 40.7 seconds (1.11.2|6.1.0)
* If the animation database file is being used an error is thrown to console, this needs to be accounted for so that when the database file can't be used the plugin is disabled
* Removing a frame that doesn't exist -- /animate name frame delete_frame 0 will throw an error if there are no frames for the given animation


## Random Notes for me
If Sponge changes the blocks at a chunk that isn't loaded and there's no one to see the changes, 
did Sponge change the blocks?  
Make a frame library? For each person? (Don't really think this would work out that well)

See Location#addScheduledUpdate()  
Checkout MagicaVoxel
