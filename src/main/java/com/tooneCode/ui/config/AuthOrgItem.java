package com.tooneCode.ui.config;

import com.tooneCode.common.CodeBundle;
import lombok.Generated;

public class AuthOrgItem {
    private static final int MAX_DISPLAY_NAME_LENGTH = 24;
    public static final String PERSONAL_TYPE = "personal";
    public static final String ORG_TYPE = "org";
    private String identifier;
    private String name;
    private String type;

    public String getKey() {
        return "personal".equals(this.type) ? null : this.identifier;
    }

    public String getDisplay() {
        String displayName = this.name;
        if (displayName.length() > 24) {
            displayName = displayName.substring(0, 24) + "...";
        }

        return "personal".equals(this.type) ? String.format("%s - %s", CodeBundle.message("settings.login.label.org.personal", new Object[0]), displayName) : String.format("%s - %s", displayName, this.identifier);
    }

    @Generated
    public static AuthOrgItemBuilder builder() {
        return new AuthOrgItemBuilder();
    }

    @Generated
    public String getIdentifier() {
        return this.identifier;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AuthOrgItem)) {
            return false;
        } else {
            AuthOrgItem other = (AuthOrgItem) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47:
                {
                    Object this$identifier = this.getIdentifier();
                    Object other$identifier = other.getIdentifier();
                    if (this$identifier == null) {
                        if (other$identifier == null) {
                            break label47;
                        }
                    } else if (this$identifier.equals(other$identifier)) {
                        break label47;
                    }

                    return false;
                }

                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                Object this$type = this.getType();
                Object other$type = other.getType();
                if (this$type == null) {
                    if (other$type != null) {
                        return false;
                    }
                } else if (!this$type.equals(other$type)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AuthOrgItem;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $identifier = this.getIdentifier();
        result = result * 59 + ($identifier == null ? 43 : $identifier.hashCode());
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getIdentifier();
        return "AuthOrgItem(identifier=" + var10000 + ", name=" + this.getName() + ", type=" + this.getType() + ")";
    }

    @Generated
    public AuthOrgItem() {
    }

    @Generated
    public AuthOrgItem(String identifier, String name, String type) {
        this.identifier = identifier;
        this.name = name;
        this.type = type;
    }

    @Generated
    public static class AuthOrgItemBuilder {
        @Generated
        private String identifier;
        @Generated
        private String name;
        @Generated
        private String type;

        @Generated
        AuthOrgItemBuilder() {
        }

        @Generated
        public AuthOrgItemBuilder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        @Generated
        public AuthOrgItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        @Generated
        public AuthOrgItemBuilder type(String type) {
            this.type = type;
            return this;
        }

        @Generated
        public AuthOrgItem build() {
            return new AuthOrgItem(this.identifier, this.name, this.type);
        }

        @Generated
        public String toString() {
            return "AuthOrgItem.AuthOrgItemBuilder(identifier=" + this.identifier + ", name=" + this.name + ", type=" + this.type + ")";
        }
    }
}

