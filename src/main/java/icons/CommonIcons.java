package icons;

import javax.swing.*;

import com.intellij.openapi.util.IconLoader;

public class CommonIcons {
    public static Icon AI;

    static {
        //AI = IconLoader.findIcon("/cosy/icons/logo_13.svg");
        AI = IconUtil.pluginIcon;
    }
}
