package com.tooneCode.common;

import com.intellij.DynamicBundle;
import com.intellij.openapi.diagnostic.Logger;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class CodeBundle extends DynamicBundle {
    private static final Logger LOG = Logger.getInstance(CodeBundle.class);
    private static final @NonNls String BUNDLE = "messageBundle";
    private static final CodeBundle INSTANCE = new CodeBundle();

    private CodeBundle() {
        super("messageBundle");
    }

    public static CodeBundle getInstance() {
        return INSTANCE;
    }

    public static @NotNull @Nls String message(@NotNull String key, @NotNull Object... params) {
        if (key == null) {
//            $$$reportNull$$$0(0);
        }

        if (params == null) {
//            $$$reportNull$$$0(1);
        }

        String fullName = INSTANCE.getMessage("cosy.plugin.name", new Object[0]);
        String simpleName = INSTANCE.getMessage("cosy.plugin.simple.name", new Object[0]);
        String value = INSTANCE.getMessage(key, params);
        String var10000 = value.replace("<PLUGIN_NAME>", fullName).replace("<SIMPLE_NAME>", simpleName);
        if (var10000 == null) {
//            $$$reportNull$$$0(2);
        }

        return var10000;
    }

    public static @NotNull @Nls String messageVpc(@NotNull @PropertyKey(
            resourceBundle = "messageBundle"
    ) String key, @NotNull Object... params) {
        if (key == null) {
//            $$$reportNull$$$0(3);
        }

        if (params == null) {
//            $$$reportNull$$$0(4);
        }

        String originKey = key;
        if (CodeConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
            key = key + ".vpc";
        }

        String fullName = INSTANCE.getMessage("cosy.plugin.name", new Object[0]);
        String simpleName = INSTANCE.getMessage("cosy.plugin.simple.name", new Object[0]);
        String value = INSTANCE.messageOrDefault(key, "", params);
        if (StringUtils.isBlank(value)) {
            value = INSTANCE.messageOrDefault(originKey, "", params);
        }

        String var10000 = value.replace("<PLUGIN_NAME>", fullName).replace("<SIMPLE_NAME>", simpleName);
        if (var10000 == null) {
            //$$$reportNull$$$0(5);
        }

        return var10000;
    }

    public static @NotNull Supplier<@Nls String> messagePointer(@NotNull @PropertyKey(
            resourceBundle = "messageBundle"
    ) String key, @NotNull Object... params) {
        if (key == null) {
            //$$$reportNull$$$0(6);
        }

        if (params == null) {
            //$$$reportNull$$$0(7);
        }

        Supplier var10000 = INSTANCE.getLazyMessage(key, params);
        if (var10000 == null) {
            //$$$reportNull$$$0(8);
        }

        return var10000;
    }

    protected ResourceBundle findBundle(@NotNull String pathToBundle, @NotNull ClassLoader baseLoader, @NotNull ResourceBundle.@NotNull Control control) {
        if (pathToBundle == null) {
            //$$$reportNull$$$0(9);
        }

        if (baseLoader == null) {
            //$$$reportNull$$$0(10);
        }

        if (control == null) {
            //$$$reportNull$$$0(11);
        }

        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle(pathToBundle, locale, baseLoader, control);
        if (bundle != null) {
            LOG.info("found bundle locate:" + bundle.getLocale());
        } else {
            LOG.warn("cannot found bundle locate, using super finder");
            bundle = super.findBundle(pathToBundle, baseLoader, control);
        }

        return bundle;
    }
}

