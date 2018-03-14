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
    public static final String ANIMATION_SETTING_FRAME      = "animate.setting.frame";
    public static final String ANIMATION_SETTING_DELAY      = "animate.setting.delay";
    public static final String ANIMATION_SETTING_CYCLES     = "animate.setting.cycles";
    public static final String ANIMATION_STOP               = "animate.stop.base";
    public static final String ANIMATION_PAUSE              = "animate.pause.base";
    public static final String ANIMATION_SET_NAME           = "animate.set.name";
    public static final String ANIMATION_SET_POS            = "animate.set.pos";
    public static final String ANIMATION_STATS              = "animate.stats.base";

    // Frame permissions
    public static final String FRAME_CREATE                 = "animate.frame.create.base";
    public static final String FRAME_DELETE                 = "animate.frame.delete.base";
    public static final String FRAME_DISPLAY                = "animate.frame.display.base";
    public static final String FRAME_DUPLICATE              = "animate.frame.duplicate.base";
    public static final String FRAME_INFO                   = "animate.frame.info.base";
    public static final String FRAME_LIST                   = "animate.frame.list.base";
    public static final String FRAME_UPDATE                 = "animate.frame.update.base";
    public static final String FRAME_SET_NAME               = "animate.frame.set.name";

    // Admin permissions
    public static final String STOP_ALL_ANIMATIONS          = "animate.admin.stopall";
    public static final String FORCE_STOP_ANIMATION         = "animate.admin.forcestop"; // Haven't implemented this command yet
    public static final String LIST_RUNNING_ANIMAITONS      = "animate.admin.listallrunning"; // Haven't implemented this command yet
    public static final String LIST_ALL_ANIMAITONS          = "animate.admin.listall";
    public static final String ANIMATION_ADMIN_START        = "animate.admin.start";
    public static final String ANIMATION_ADMIN_PAUSE        = "animate.admin.pause";
    public static final String ANIMATION_ADMIN_STOP         = "animate.admin.stop";

    public static final String TOGGLE_DEBUG                 = "animate.admin.debug";
    public static final String REFRESH_ALL_ANIMATIONS       = "animate.admin.refreshall";

}
