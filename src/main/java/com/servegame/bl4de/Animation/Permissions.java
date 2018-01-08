package com.servegame.bl4de.Animation;

/**
 * File: Permissions.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class Permissions {
    // Animation permissions
    public static final String ANIMATION_BASE               = "animate.base";
    public static final String ANIMATION_CREATE             = "animate.create.base";
    public static final String ANIMATION_DELETE             = "animate.delete.base";
    public static final String ANIMATION_INFO               = "animate.info.base";
    public static final String ANIMATION_HELP               = "animate.help.base";
    public static final String ANIMATION_LIST               = "animate.list.base";
    public static final String ANIMATION_START              = "animate.start.base";
    public static final String ANIMATION_START_FLAG_F       = "animate.start.flag.frame";
    public static final String ANIMATION_START_FLAG_D       = "animate.start.flag.delay";
    public static final String ANIMATION_START_FLAG_C       = "animate.start.flag.cycles";
    public static final String ANIMATION_STOP               = "animate.stop.base";
    public static final String ANIMATION_PAUSE              = "animate.pause.base";
    public static final String ANIMATION_SET_NAME           = "animate.set.name";
    public static final String ANIMATION_SET_POS            = "animate.set.pos";
    public static final String ANIMATION_STATS              = "animate.stats.base";
    public static final String ANIMATION_STOP_ALL           = "animate.stopall.base";

    // Frame permissions
    public static final String FRAME_CREATE                 = "animate.frame.create.base";
    public static final String FRAME_DELETE                 = "animate.frame.delete.base";
    public static final String FRAME_DISPLAY                = "animate.frame.display.base";
    public static final String FRAME_DUPLICATE              = "animate.frame.duplicate.base";
    public static final String FRAME_LIST                   = "animate.frame.list.base";
    public static final String FRAME_UPDATE                 = "animate.frame.update.base";

    // Admin permissions
    public static final String STOP_ALL_ANIMATIONS          = "animate.admin.stopall";
    public static final String LIST_RUNNING_ANIMAITONS      = "animate.admin.listall";
    public static final String FORCE_STOP_ANIMATION         = "animate.admin.forcestop";
    public static final String TOGGLE_DEBUG                 = "animate.admin.debug";
    public static final String REFRESH_ALL_ANIMATIONS       = "animate.admin.refreshall";

}
