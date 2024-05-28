package icons;

import javax.swing.*;

import com.intellij.openapi.util.IconLoader;

public class CommonIcons {
    public static Icon AI;
    public static Icon consoleIcon;

    static {
        //AI = IconLoader.findIcon("/code/icons/logo_13.svg");
        AI = IconUtil.pluginIcon;
        consoleIcon = IconUtil.consoleIcon;
    }
}
