package com.tooneCode.constants;

import com.tooneCode.core.ConfigManager;
import com.tooneCode.core.model.model.ConfigInfo;
import lombok.Generated;

public enum TooneCodeUrls {
    OPEN_FEEDBACK_URL("feedbackUrl", "url_config.feedback", "http://www.toone.com.cn"),
    MESSAGE_FEEDBACK_URL("feedbackUrl", "url_config.feedback", "http://www.toone.com.cn"),
    SURVEY_URL("surveyUrl", "url_config.survey_feedback", "http://www.toone.com.cn"),
    HELP_DOCUMENT_URL("helpDocumentUrl", "url_config.help", "http://www.toone.com.cn"),
    PRIVACY_URL("privacyDocumentUrl", "url_config.privacy", "http://www.toone.com.cn"),
    EXPR_APPLY_URL("exprApplyUrl", "url_config.join", "http://www.toone.com.cn"),
    NETWORK_ERROR_URL("networkErrorUrl", "url_config.network_error", "http://www.toone.com.cn"),
    FAQ_URL("faqUrl", "url_config.faq", "http://www.toone.com.cn"),
    GET_ACCESS_KEY_URL("accessKeyUrl", "url_config.access_key", "http://www.toone.com.cn");

    final String refId;
    final String key;
    final String defaultUrl;

    private TooneCodeUrls(String refId, String key, String defaultUrl) {
        this.refId = refId;
        this.key = key;
        this.defaultUrl = defaultUrl;
    }

    public String getRealUrl() {
        ConfigInfo info = ConfigManager.INSTANCE.getConfig();
        return info == null ? this.defaultUrl : info.getStringByPath(this.key, this.defaultUrl);
    }

    public static TooneCodeUrls fromRefId(String refId) {
        TooneCodeUrls[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            TooneCodeUrls url = var1[var3];
            if (url.refId.equalsIgnoreCase(refId)) {
                return url;
            }
        }

        return null;
    }

    @Generated
    public String getRefId() {
        return this.refId;
    }

    @Generated
    public String getKey() {
        return this.key;
    }

    @Generated
    public String getDefaultUrl() {
        return this.defaultUrl;
    }
}
