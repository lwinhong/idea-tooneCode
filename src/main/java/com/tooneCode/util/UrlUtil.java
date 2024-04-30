package com.tooneCode.util;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import java.awt.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.HyperlinkEvent;

import com.tooneCode.constants.LingmaUrls;
import com.tooneCode.editor.ActionTriggerHelper;
import com.tooneCode.ui.enums.TriggerPlaceEnum;
import org.apache.commons.lang3.StringUtils;

public class UrlUtil {
    private static final Logger log = Logger.getInstance(UrlUtil.class);
    private static final String URL_REGEX = "^(http|https)://\\S*$";
    private static final Pattern URL_PATTERN = Pattern.compile("^(http|https)://\\S*$");

    public UrlUtil() {
    }

    public static boolean verifyDomain(String domainUrl) {
        Matcher matcher = URL_PATTERN.matcher(domainUrl);
        if (matcher.matches()) {
            return true;
        } else {
            log.info(String.format("Domain is not valid: %s", domainUrl));
            return false;
        }
    }

    public static boolean performHyperlink(Project project, HyperlinkEvent e, Component component) {
        String command = e.getDescription();
        if (StringUtils.isEmpty(command)) {
            return false;
        } else if (command.startsWith("http")) {
            BrowserUtil.browse(command);
            return true;
        } else {
            String refId;
            if (command.startsWith("action:")) {
                if (project == null) {
                    return false;
                } else {
                    refId = command.substring("action:".length());
                    ActionTriggerHelper.triggerSelectionAction(project, refId, TriggerPlaceEnum.TOOL_WINDOW.getName(), component);
                    return true;
                }
            } else {
                if (command.startsWith("url:")) {
                    refId = command.substring("url:".length());
                    LingmaUrls url = LingmaUrls.fromRefId(refId);
                    if (url != null) {
                        return CodeBrowserUtil.browse(url);
                    }

                    log.warn("can not find url refId: " + refId);
                }

                return false;
            }
        }
    }
}
