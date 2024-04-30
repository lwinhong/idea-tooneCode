package com.tooneCode.services.model;

import java.util.Set;
import lombok.Generated;

public class TextChangeStat {
    String filePath;
    Integer startLineNumber;
    Set<Integer> changeLineNumbers;
    long changeCharChangeLength;
    long changeNewLineCount;
    long validNewLineCount;
    boolean accepted;
    String language;
    String source;
    long timestamp;

    @Generated
    public TextChangeStat() {
    }

    @Generated
    public String getFilePath() {
        return this.filePath;
    }

    @Generated
    public Integer getStartLineNumber() {
        return this.startLineNumber;
    }

    @Generated
    public Set<Integer> getChangeLineNumbers() {
        return this.changeLineNumbers;
    }

    @Generated
    public long getChangeCharChangeLength() {
        return this.changeCharChangeLength;
    }

    @Generated
    public long getChangeNewLineCount() {
        return this.changeNewLineCount;
    }

    @Generated
    public long getValidNewLineCount() {
        return this.validNewLineCount;
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
    public long getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Generated
    public void setStartLineNumber(Integer startLineNumber) {
        this.startLineNumber = startLineNumber;
    }

    @Generated
    public void setChangeLineNumbers(Set<Integer> changeLineNumbers) {
        this.changeLineNumbers = changeLineNumbers;
    }

    @Generated
    public void setChangeCharChangeLength(long changeCharChangeLength) {
        this.changeCharChangeLength = changeCharChangeLength;
    }

    @Generated
    public void setChangeNewLineCount(long changeNewLineCount) {
        this.changeNewLineCount = changeNewLineCount;
    }

    @Generated
    public void setValidNewLineCount(long validNewLineCount) {
        this.validNewLineCount = validNewLineCount;
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
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TextChangeStat)) {
            return false;
        } else {
            TextChangeStat other = (TextChangeStat)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label91: {
                    Object this$filePath = this.getFilePath();
                    Object other$filePath = other.getFilePath();
                    if (this$filePath == null) {
                        if (other$filePath == null) {
                            break label91;
                        }
                    } else if (this$filePath.equals(other$filePath)) {
                        break label91;
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

                Object this$changeLineNumbers = this.getChangeLineNumbers();
                Object other$changeLineNumbers = other.getChangeLineNumbers();
                if (this$changeLineNumbers == null) {
                    if (other$changeLineNumbers != null) {
                        return false;
                    }
                } else if (!this$changeLineNumbers.equals(other$changeLineNumbers)) {
                    return false;
                }

                if (this.getChangeCharChangeLength() != other.getChangeCharChangeLength()) {
                    return false;
                } else if (this.getChangeNewLineCount() != other.getChangeNewLineCount()) {
                    return false;
                } else if (this.getValidNewLineCount() != other.getValidNewLineCount()) {
                    return false;
                } else if (this.isAccepted() != other.isAccepted()) {
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

                    if (this.getTimestamp() != other.getTimestamp()) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TextChangeStat;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $filePath = this.getFilePath();
        result = result * 59 + ($filePath == null ? 43 : $filePath.hashCode());
        Object $startLineNumber = this.getStartLineNumber();
        result = result * 59 + ($startLineNumber == null ? 43 : $startLineNumber.hashCode());
        Object $changeLineNumbers = this.getChangeLineNumbers();
        result = result * 59 + ($changeLineNumbers == null ? 43 : $changeLineNumbers.hashCode());
        long $changeCharChangeLength = this.getChangeCharChangeLength();
        result = result * 59 + (int)($changeCharChangeLength >>> 32 ^ $changeCharChangeLength);
        long $changeNewLineCount = this.getChangeNewLineCount();
        result = result * 59 + (int)($changeNewLineCount >>> 32 ^ $changeNewLineCount);
        long $validNewLineCount = this.getValidNewLineCount();
        result = result * 59 + (int)($validNewLineCount >>> 32 ^ $validNewLineCount);
        result = result * 59 + (this.isAccepted() ? 79 : 97);
        Object $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        Object $source = this.getSource();
        result = result * 59 + ($source == null ? 43 : $source.hashCode());
        long $timestamp = this.getTimestamp();
        result = result * 59 + (int)($timestamp >>> 32 ^ $timestamp);
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getFilePath();
        return "TextChangeStat(filePath=" + var10000 + ", startLineNumber=" + this.getStartLineNumber() + ", changeLineNumbers=" + this.getChangeLineNumbers() + ", changeCharChangeLength=" + this.getChangeCharChangeLength() + ", changeNewLineCount=" + this.getChangeNewLineCount() + ", validNewLineCount=" + this.getValidNewLineCount() + ", accepted=" + this.isAccepted() + ", language=" + this.getLanguage() + ", source=" + this.getSource() + ", timestamp=" + this.getTimestamp() + ")";
    }
}

