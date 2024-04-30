package com.tooneCode.core.model.model;

import java.util.Locale;
import lombok.Generated;

public class DisplayTask {
    private String displayText;
    private String displayEnText;
    private String chatTask;
    private String prompt;

    public String getDisplayTextLocale() {
        String displayText = this.getDisplayEnText();
        if (Locale.CHINESE.getLanguage().equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            displayText = this.getDisplayText();
        }

        return displayText;
    }

    @Generated
    public static DisplayTaskBuilder builder() {
        return new DisplayTaskBuilder();
    }

    @Generated
    public String getDisplayText() {
        return this.displayText;
    }

    @Generated
    public String getDisplayEnText() {
        return this.displayEnText;
    }

    @Generated
    public String getChatTask() {
        return this.chatTask;
    }

    @Generated
    public String getPrompt() {
        return this.prompt;
    }

    @Generated
    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    @Generated
    public void setDisplayEnText(String displayEnText) {
        this.displayEnText = displayEnText;
    }

    @Generated
    public void setChatTask(String chatTask) {
        this.chatTask = chatTask;
    }

    @Generated
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DisplayTask)) {
            return false;
        } else {
            DisplayTask other = (DisplayTask)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$displayText = this.getDisplayText();
                    Object other$displayText = other.getDisplayText();
                    if (this$displayText == null) {
                        if (other$displayText == null) {
                            break label59;
                        }
                    } else if (this$displayText.equals(other$displayText)) {
                        break label59;
                    }

                    return false;
                }

                Object this$displayEnText = this.getDisplayEnText();
                Object other$displayEnText = other.getDisplayEnText();
                if (this$displayEnText == null) {
                    if (other$displayEnText != null) {
                        return false;
                    }
                } else if (!this$displayEnText.equals(other$displayEnText)) {
                    return false;
                }

                Object this$chatTask = this.getChatTask();
                Object other$chatTask = other.getChatTask();
                if (this$chatTask == null) {
                    if (other$chatTask != null) {
                        return false;
                    }
                } else if (!this$chatTask.equals(other$chatTask)) {
                    return false;
                }

                Object this$prompt = this.getPrompt();
                Object other$prompt = other.getPrompt();
                if (this$prompt == null) {
                    if (other$prompt != null) {
                        return false;
                    }
                } else if (!this$prompt.equals(other$prompt)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof DisplayTask;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $displayText = this.getDisplayText();
        result = result * 59 + ($displayText == null ? 43 : $displayText.hashCode());
        Object $displayEnText = this.getDisplayEnText();
        result = result * 59 + ($displayEnText == null ? 43 : $displayEnText.hashCode());
        Object $chatTask = this.getChatTask();
        result = result * 59 + ($chatTask == null ? 43 : $chatTask.hashCode());
        Object $prompt = this.getPrompt();
        result = result * 59 + ($prompt == null ? 43 : $prompt.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getDisplayText();
        return "DisplayTask(displayText=" + var10000 + ", displayEnText=" + this.getDisplayEnText() + ", chatTask=" + this.getChatTask() + ", prompt=" + this.getPrompt() + ")";
    }

    @Generated
    public DisplayTask() {
    }

    @Generated
    public DisplayTask(String displayText, String displayEnText, String chatTask, String prompt) {
        this.displayText = displayText;
        this.displayEnText = displayEnText;
        this.chatTask = chatTask;
        this.prompt = prompt;
    }

    @Generated
    public static class DisplayTaskBuilder {
        @Generated
        private String displayText;
        @Generated
        private String displayEnText;
        @Generated
        private String chatTask;
        @Generated
        private String prompt;

        @Generated
        DisplayTaskBuilder() {
        }

        @Generated
        public DisplayTaskBuilder displayText(String displayText) {
            this.displayText = displayText;
            return this;
        }

        @Generated
        public DisplayTaskBuilder displayEnText(String displayEnText) {
            this.displayEnText = displayEnText;
            return this;
        }

        @Generated
        public DisplayTaskBuilder chatTask(String chatTask) {
            this.chatTask = chatTask;
            return this;
        }

        @Generated
        public DisplayTaskBuilder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        @Generated
        public DisplayTask build() {
            return new DisplayTask(this.displayText, this.displayEnText, this.chatTask, this.prompt);
        }

        @Generated
        public String toString() {
            return "DisplayTask.DisplayTaskBuilder(displayText=" + this.displayText + ", displayEnText=" + this.displayEnText + ", chatTask=" + this.chatTask + ", prompt=" + this.prompt + ")";
        }
    }
}

