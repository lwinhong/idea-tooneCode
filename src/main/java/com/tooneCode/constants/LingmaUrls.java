package com.tooneCode.constants;

import com.tooneCode.core.ConfigManager;
import com.tooneCode.core.model.model.ConfigInfo;
import lombok.Generated;

public enum LingmaUrls {
    OPEN_FEEDBACK_URL("feedbackUrl", "url_config.feedback", "https://developer.aliyun.com/ask/new?spm=a2c6h.13066369.question.23.38d01bdfIPOy5Z&excode=devops&exdcode=tongyilingma"),
    MESSAGE_FEEDBACK_URL("feedbackUrl", "url_config.feedback", "https://developer.aliyun.com/ask/new?spm=a2c6h.13066369.question.23.38d01bdfIPOy5Z&excode=devops&exdcode=tongyilingma"),
    SURVEY_URL("surveyUrl", "url_config.survey_feedback", "https://survey.aliyun.com/apps/zhiliao/gLgsYL8mB"),
    HELP_DOCUMENT_URL("helpDocumentUrl", "url_config.help", "https://help.aliyun.com/document_detail/2590613.html"),
    PRIVACY_URL("privacyDocumentUrl", "url_config.privacy", "https://help.aliyun.com/document_detail/2590617.html"),
    EXPR_APPLY_URL("exprApplyUrl", "url_config.join", "https://tongyi.aliyun.com/lingma"),
    NETWORK_ERROR_URL("networkErrorUrl", "url_config.network_error", "https://help.aliyun.com/document_detail/2671485.html"),
    FAQ_URL("faqUrl", "url_config.faq", "https://help.aliyun.com/document_detail/2590620.html"),
    GET_ACCESS_KEY_URL("accessKeyUrl", "url_config.access_key", "https://help.aliyun.com/zh/ram/user-guide/create-an-accesskey-pair");

    final String refId;
    final String key;
    final String defaultUrl;

    private LingmaUrls(String refId, String key, String defaultUrl) {
        this.refId = refId;
        this.key = key;
        this.defaultUrl = defaultUrl;
    }

    public String getRealUrl() {
        ConfigInfo info = ConfigManager.INSTANCE.getConfig();
        return info == null ? this.defaultUrl : info.getStringByPath(this.key, this.defaultUrl);
    }

    public static LingmaUrls fromRefId(String refId) {
        LingmaUrls[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            LingmaUrls url = var1[var3];
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
