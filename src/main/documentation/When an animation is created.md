**Action:**  
User runs /animate create \<name>  

**Checks:**
* Ensures the command caller is an instance of a Player
* Ensures the caller has specified a \<name>
* Ensures the caller specified a \<name> that is <= 255 chars long
* Ensures the caller specified a \<name> that hasn't already been used by the specific caller

**Internal logic that creates the blank animation:**  
At a glance:
* Creates a new Animation, giving the UUID of the player and the \<name> specified
* Writes the new Animation to the ANIMATION_TABLE
* Creates a new table for the frames for this animations. This table is named 'playerUUID_animationName_frames'. e.g: ff780114-e585-444c-9cb0-40006c41bcbe_sweetAnimation_frames

Specifically:
* Is this going to be a thing? **TODO**