package com.tooneCode.services.model;

import com.intellij.openapi.project.Project;
import lombok.Generated;

public class TextChangeContext {
    public static final String CHAT_SOURCE = "chat";
    public static final String COMPLETION_SOURCE = "completion";
    Project project;
    String filePath;
    String addedText;
    Integer startLineNumber;
    boolean accepted;
    String language;
    String source;

    @Generated
    public static TextChangeContextBuilder builder() {
        return new TextChangeContextBuilder();
    }

    @Generated
    public Project getProject() {
        return this.project;
    }

    @Generated
    public String getFilePath() {
        return this.filePath;
    }

    @Generated
    public String getAddedText() {
        return this.addedText;
    }

    @Generated
    public Integer getStartLineNumber() {
        return this.startLineNumber;
    }

    @Generated
    public boolean isAccepted() {
        return this.accepted;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public String getSource() {
        return this.source;
    }

    @Generated
    public void setProject(Project project) {
        this.project = project;
    }

    @Generated
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Generated
    public void setAddedText(String addedText) {
        this.addedText = addedText;
    }

    @Generated
    public void setStartLineNumber(Integer startLineNumber) {
        this.startLineNumber = startLineNumber;
    }

    @Generated
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public void setSource(String source) {
        this.source = source;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TextChangeContext)) {
            return false;
        } else {
            TextChangeContext other = (TextChangeContext) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label87:
                {
                    Object this$project = this.getProject();
                    Object other$project = other.getProject();
                    if (this$project == null) {
                        if (other$project == null) {
                            break label87;
                        }
                    } else if (this$project.equals(other$project)) {
                        break label87;
                    }

                    return false;
                }

                Object this$filePath = this.getFilePath();
                Object other$filePath = other.getFilePath();
                if (this$filePath == null) {
                    if (other$filePath != null) {
                        return false;
                    }
                } else if (!this$filePath.equals(other$filePath)) {
                    return false;
                }

                label73:
                {
                    Object this$addedText = this.getAddedText();
                    Object other$addedText = other.getAddedText();
                    if (this$addedText == null) {
                        if (other$addedText == null) {
                            break label73;
                        }
                    } else if (this$addedText.equals(other$addedText)) {
                        break label73;
                    }

                    return false;
                }

                Object this$startLineNumber = this.getStartLineNumber();
                Object other$startLineNumber = other.getStartLineNumber();
                if (this$startLineNumber == null) {
                    if (other$startLineNumber != null) {
                        return false;
                    }
                } else if (!this$startLineNumber.equals(other$startLineNumber)) {
                    return false;
                }

                if (this.isAccepted() != other.isAccepted()) {
                    return false;
                } else {
                    Object this$language = this.getLanguage();
                    Object other$language = other.getLanguage();
                    if (this$language == null) {
                        if (other$language != null) {
                            return false;
                        }
                    } else if (!this$language.equals(other$language)) {
                        return false;
                    }

                    Object this$source = this.getSource();
                    Object other$source = other.getSource();
                    if (this$source == null) {
                        if (other$source != null) {
                            return false;
                        }
                    } else if (!this$source.equals(other$source)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TextChangeContext;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $project = this.getProject();
        result = result * 59 + ($project == null ? 43 : $project.hashCode());
        Object $filePath = this.getFilePath();
        result = result * 59 + ($filePath == null ? 43 : $filePath.hashCode());
        Object $addedText = this.getAddedText();
        result = result * 59 + ($addedText == null ? 43 : $addedText.hashCode());
        Object $startLineNumber = this.getStartLineNumber();
        result = result * 59 + ($startLineNumber == null ? 43 : $startLineNumber.hashCode());
        result = result * 59 + (this.isAccepted() ? 79 : 97);
        Object $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        Object $source = this.getSource();
        result = result * 59 + ($source == null ? 43 : $source.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Project var10000 = this.getProject();
        return "TextChangeContext(project=" + var10000 + ", filePath=" + this.getFilePath() + ", addedText=" + this.getAddedText() + ", startLineNumber=" + this.getStartLineNumber() + ", accepted=" + this.isAccepted() + ", language=" + this.getLanguage() + ", source=" + this.getSource() + ")";
    }

    @Generated
    public TextChangeContext() {
    }

    @Generated
    public TextChangeContext(Project project, String filePath, String addedText, Integer startLineNumber, boolean accepted, String language, String source) {
        this.project = project;
        this.filePath = filePath;
        this.addedText = addedText;
        this.startLineNumber = startLineNumber;
        this.accepted = accepted;
        this.language = language;
        this.source = source;
    }

    @Generated
    public static class TextChangeContextBuilder {
        @Generated
        private Project project;
        @Generated
        private String filePath;
        @Generated
        private String addedText;
        @Generated
        private Integer startLineNumber;
        @Generated
        private boolean accepted;
        @Generated
        private String language;
        @Generated
        private String source;

        @Generated
        TextChangeContextBuilder() {
        }

        @Generated
        public TextChangeContextBuilder project(Project project) {
            this.project = project;
            return this;
        }

        @Generated
        public TextChangeContextBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        @Generated
        public TextChangeContextBuilder addedText(String addedText) {
            this.addedText = addedText;
            return this;
        }

        @Generated
        public TextChangeContextBuilder startLineNumber(Integer startLineNumber) {
            this.startLineNumber = startLineNumber;
            return this;
        }

        @Generated
        public TextChangeContextBuilder accepted(boolean accepted) {
            this.accepted = accepted;
            return this;
        }

        @Generated
        public TextChangeContextBuilder language(String language) {
            this.language = language;
            return this;
        }

        @Generated
        public TextChangeContextBuilder source(String source) {
            this.source = source;
            return this;
        }

        @Generated
        public TextChangeContext build() {
            return new TextChangeContext(this.project, this.filePath, this.addedText, this.startLineNumber, this.accepted, this.language, this.source);
        }

        @Generated
        public String toString() {
            return "TextChangeContext.TextChangeContextBuilder(project=" + this.project + ", filePath=" + this.filePath + ", addedText=" + this.addedText + ", startLineNumber=" + this.startLineNumber + ", accepted=" + this.accepted + ", language=" + this.language + ", source=" + this.source + ")";
        }
    }
}

