package com.tooneCode.services.model;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import lombok.Generated;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class Measurements {
    Character charBeforeCaret;
    Character charAfterCaret;
    Character charTrimBeforeCaret;
    Character charTrimAfterCaret;
    Integer lastLineLengthBeforeCaret;
    Integer lastLineTrimEndLengthBeforeCaret;
    Integer documentLength;
    Integer caretCharOffset;
    Long completionDelayMs;

    public static Measurements build(@NonNull Editor editor) {
        if (editor == null) {
            throw new NullPointerException("editor is marked @NonNull but is null");
        } else {
            String text = editor.getDocument().getText();
            int caretOffset = editor.getCaretModel().getOffset();
            int lineNumber = editor.getDocument().getLineNumber(caretOffset);
            Measurements measurements = new Measurements();
            if (StringUtils.isEmpty(text)) {
                return measurements;
            } else {
                measurements.setCaretCharOffset(caretOffset);
                String prefix;
                if (text.length() > measurements.getCaretCharOffset()) {
                    measurements.setCharAfterCaret(text.charAt(measurements.getCaretCharOffset()));
                    prefix = text.substring(measurements.getCaretCharOffset()).trim();
                    if (!prefix.isEmpty()) {
                        measurements.setCharTrimAfterCaret(prefix.charAt(0));
                    }
                }

                if (measurements.getCaretCharOffset() > 1) {
                    measurements.setCharBeforeCaret(text.charAt(measurements.getCaretCharOffset() - 2));
                    prefix = text.substring(0, measurements.getCaretCharOffset()).trim();
                    if (!prefix.isEmpty()) {
                        measurements.setCharTrimBeforeCaret(prefix.charAt(prefix.length() - 1));
                    }
                }

                measurements.setDocumentLength(text.length());
                if (lineNumber > 0) {
                    int lastLineStartOffset = editor.getDocument().getLineStartOffset(lineNumber - 1);
                    int lastLineEndOffset = editor.getDocument().getLineEndOffset(lineNumber - 1);
                    String lastLine = editor.getDocument().getText(new TextRange(lastLineStartOffset, lastLineEndOffset));
                    measurements.setLastLineLengthBeforeCaret(lastLine.length());
                    measurements.setLastLineTrimEndLengthBeforeCaret(StringUtils.stripEnd(lastLine, " \t\r\n").length());
                }

                return measurements;
            }
        }
    }

    @Generated
    public Character getCharBeforeCaret() {
        return this.charBeforeCaret;
    }

    @Generated
    public Character getCharAfterCaret() {
        return this.charAfterCaret;
    }

    @Generated
    public Character getCharTrimBeforeCaret() {
        return this.charTrimBeforeCaret;
    }

    @Generated
    public Character getCharTrimAfterCaret() {
        return this.charTrimAfterCaret;
    }

    @Generated
    public Integer getLastLineLengthBeforeCaret() {
        return this.lastLineLengthBeforeCaret;
    }

    @Generated
    public Integer getLastLineTrimEndLengthBeforeCaret() {
        return this.lastLineTrimEndLengthBeforeCaret;
    }

    @Generated
    public Integer getDocumentLength() {
        return this.documentLength;
    }

    @Generated
    public Integer getCaretCharOffset() {
        return this.caretCharOffset;
    }

    @Generated
    public Long getCompletionDelayMs() {
        return this.completionDelayMs;
    }

    @Generated
    public void setCharBeforeCaret(Character charBeforeCaret) {
        this.charBeforeCaret = charBeforeCaret;
    }

    @Generated
    public void setCharAfterCaret(Character charAfterCaret) {
        this.charAfterCaret = charAfterCaret;
    }

    @Generated
    public void setCharTrimBeforeCaret(Character charTrimBeforeCaret) {
        this.charTrimBeforeCaret = charTrimBeforeCaret;
    }

    @Generated
    public void setCharTrimAfterCaret(Character charTrimAfterCaret) {
        this.charTrimAfterCaret = charTrimAfterCaret;
    }

    @Generated
    public void setLastLineLengthBeforeCaret(Integer lastLineLengthBeforeCaret) {
        this.lastLineLengthBeforeCaret = lastLineLengthBeforeCaret;
    }

    @Generated
    public void setLastLineTrimEndLengthBeforeCaret(Integer lastLineTrimEndLengthBeforeCaret) {
        this.lastLineTrimEndLengthBeforeCaret = lastLineTrimEndLengthBeforeCaret;
    }

    @Generated
    public void setDocumentLength(Integer documentLength) {
        this.documentLength = documentLength;
    }

    @Generated
    public void setCaretCharOffset(Integer caretCharOffset) {
        this.caretCharOffset = caretCharOffset;
    }

    @Generated
    public void setCompletionDelayMs(Long completionDelayMs) {
        this.completionDelayMs = completionDelayMs;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Measurements)) {
            return false;
        } else {
            Measurements other = (Measurements) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label119:
                {
                    Object this$charBeforeCaret = this.getCharBeforeCaret();
                    Object other$charBeforeCaret = other.getCharBeforeCaret();
                    if (this$charBeforeCaret == null) {
                        if (other$charBeforeCaret == null) {
                            break label119;
                        }
                    } else if (this$charBeforeCaret.equals(other$charBeforeCaret)) {
                        break label119;
                    }

                    return false;
                }

                Object this$charAfterCaret = this.getCharAfterCaret();
                Object other$charAfterCaret = other.getCharAfterCaret();
                if (this$charAfterCaret == null) {
                    if (other$charAfterCaret != null) {
                        return false;
                    }
                } else if (!this$charAfterCaret.equals(other$charAfterCaret)) {
                    return false;
                }

                label105:
                {
                    Object this$charTrimBeforeCaret = this.getCharTrimBeforeCaret();
                    Object other$charTrimBeforeCaret = other.getCharTrimBeforeCaret();
                    if (this$charTrimBeforeCaret == null) {
                        if (other$charTrimBeforeCaret == null) {
                            break label105;
                        }
                    } else if (this$charTrimBeforeCaret.equals(other$charTrimBeforeCaret)) {
                        break label105;
                    }

                    return false;
                }

                Object this$charTrimAfterCaret = this.getCharTrimAfterCaret();
                Object other$charTrimAfterCaret = other.getCharTrimAfterCaret();
                if (this$charTrimAfterCaret == null) {
                    if (other$charTrimAfterCaret != null) {
                        return false;
                    }
                } else if (!this$charTrimAfterCaret.equals(other$charTrimAfterCaret)) {
                    return false;
                }

                label91:
                {
                    Object this$lastLineLengthBeforeCaret = this.getLastLineLengthBeforeCaret();
                    Object other$lastLineLengthBeforeCaret = other.getLastLineLengthBeforeCaret();
                    if (this$lastLineLengthBeforeCaret == null) {
                        if (other$lastLineLengthBeforeCaret == null) {
                            break label91;
                        }
                    } else if (this$lastLineLengthBeforeCaret.equals(other$lastLineLengthBeforeCaret)) {
                        break label91;
                    }

                    return false;
                }

                Object this$lastLineTrimEndLengthBeforeCaret = this.getLastLineTrimEndLengthBeforeCaret();
                Object other$lastLineTrimEndLengthBeforeCaret = other.getLastLineTrimEndLengthBeforeCaret();
                if (this$lastLineTrimEndLengthBeforeCaret == null) {
                    if (other$lastLineTrimEndLengthBeforeCaret != null) {
                        return false;
                    }
                } else if (!this$lastLineTrimEndLengthBeforeCaret.equals(other$lastLineTrimEndLengthBeforeCaret)) {
                    return false;
                }

                label77:
                {
                    Object this$documentLength = this.getDocumentLength();
                    Object other$documentLength = other.getDocumentLength();
                    if (this$documentLength == null) {
                        if (other$documentLength == null) {
                            break label77;
                        }
                    } else if (this$documentLength.equals(other$documentLength)) {
                        break label77;
                    }

                    return false;
                }

                label70:
                {
                    Object this$caretCharOffset = this.getCaretCharOffset();
                    Object other$caretCharOffset = other.getCaretCharOffset();
                    if (this$caretCharOffset == null) {
                        if (other$caretCharOffset == null) {
                            break label70;
                        }
                    } else if (this$caretCharOffset.equals(other$caretCharOffset)) {
                        break label70;
                    }

                    return false;
                }

                Object this$completionDelayMs = this.getCompletionDelayMs();
                Object other$completionDelayMs = other.getCompletionDelayMs();
                if (this$completionDelayMs == null) {
                    if (other$completionDelayMs != null) {
                        return false;
                    }
                } else if (!this$completionDelayMs.equals(other$completionDelayMs)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Measurements;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $charBeforeCaret = this.getCharBeforeCaret();
        result = result * 59 + ($charBeforeCaret == null ? 43 : $charBeforeCaret.hashCode());
        Object $charAfterCaret = this.getCharAfterCaret();
        result = result * 59 + ($charAfterCaret == null ? 43 : $charAfterCaret.hashCode());
        Object $charTrimBeforeCaret = this.getCharTrimBeforeCaret();
        result = result * 59 + ($charTrimBeforeCaret == null ? 43 : $charTrimBeforeCaret.hashCode());
        Object $charTrimAfterCaret = this.getCharTrimAfterCaret();
        result = result * 59 + ($charTrimAfterCaret == null ? 43 : $charTrimAfterCaret.hashCode());
        Object $lastLineLengthBeforeCaret = this.getLastLineLengthBeforeCaret();
        result = result * 59 + ($lastLineLengthBeforeCaret == null ? 43 : $lastLineLengthBeforeCaret.hashCode());
        Object $lastLineTrimEndLengthBeforeCaret = this.getLastLineTrimEndLengthBeforeCaret();
        result = result * 59 + ($lastLineTrimEndLengthBeforeCaret == null ? 43 : $lastLineTrimEndLengthBeforeCaret.hashCode());
        Object $documentLength = this.getDocumentLength();
        result = result * 59 + ($documentLength == null ? 43 : $documentLength.hashCode());
        Object $caretCharOffset = this.getCaretCharOffset();
        result = result * 59 + ($caretCharOffset == null ? 43 : $caretCharOffset.hashCode());
        Object $completionDelayMs = this.getCompletionDelayMs();
        result = result * 59 + ($completionDelayMs == null ? 43 : $completionDelayMs.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Character var10000 = this.getCharBeforeCaret();
        return "Measurements(charBeforeCaret=" + var10000 + ", charAfterCaret=" + this.getCharAfterCaret() + ", charTrimBeforeCaret=" + this.getCharTrimBeforeCaret() + ", charTrimAfterCaret=" + this.getCharTrimAfterCaret() + ", lastLineLengthBeforeCaret=" + this.getLastLineLengthBeforeCaret() + ", lastLineTrimEndLengthBeforeCaret=" + this.getLastLineTrimEndLengthBeforeCaret() + ", documentLength=" + this.getDocumentLength() + ", caretCharOffset=" + this.getCaretCharOffset() + ", completionDelayMs=" + this.getCompletionDelayMs() + ")";
    }

    @Generated
    public Measurements() {
    }
}
