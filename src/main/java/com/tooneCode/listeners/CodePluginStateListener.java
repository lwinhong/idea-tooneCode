package com.tooneCode.listeners;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginStateListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import java.io.File;

import com.tooneCode.TooneCodeApp;
import com.tooneCode.common.CodeConfig;
import com.tooneCode.constants.Constants;
import com.tooneCode.util.ApplicationUtil;
import org.jetbrains.annotations.NotNull;

public class CodePluginStateListener implements PluginStateListener {
   private static final Logger log = Logger.getInstance(CodePluginStateListener.class);

   public void install(@NotNull IdeaPluginDescriptor descriptor) {

      if (Constants.PLUGIN_ID.equals(descriptor.getPluginId().getIdString())) {
         log.info("trigger install");
         ApplicationUtil.killCosyProcess();
      }

      TooneCodeApp.init();
   }

   public void uninstall(@NotNull IdeaPluginDescriptor descriptor) {

      if (Constants.PLUGIN_ID.equals(descriptor.getPluginId().getIdString())) {
         log.info("trigger uninstall");
         File homeDir = CodeConfig.getHomeDirectory().toFile();
         if (!homeDir.exists()) {
            log.warn("Cannot find code home directory.");
            return;
         }

//         ApplicationManager.getApplication().invokeLater(() -> {
//            BinaryUninstaller.uninstall(homeDir);
//         });
         ApplicationUtil.killCosyProcess();
      }

   }

}

