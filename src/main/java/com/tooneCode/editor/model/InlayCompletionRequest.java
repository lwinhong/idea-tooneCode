package com.tooneCode.editor.model;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.tooneCode.core.model.CompletionParams;
import com.tooneCode.editor.InlayCompletionCollector;
import org.jetbrains.annotations.NotNull;

public class InlayCompletionRequest implements Disposable {
    CompletionParams params;
    Editor editor;
    int cursorOffset;
    boolean useTabs;
    int tabWidth;
    InlayCompletionCollector collector;
    boolean canceled = false;

    public InlayCompletionRequest(CompletionParams params, Editor editor) {
        this.params = params;
        this.editor = editor;
        this.cursorOffset = editor.getCaretModel().getOffset();
        Project project = editor.getProject();
        if (project != null) {
            this.useTabs = editor.getSettings().isUseTabCharacter(project);
            this.tabWidth = editor.getSettings().getTabSize(project);
        }

    }

    public InlayCompletionRequest(CompletionParams params, Editor editor, int cursorOffset) {
        this.params = params;
        this.editor = editor;
        this.cursorOffset = cursorOffset;
        Project project = editor.getProject();
        if (project != null) {
            this.useTabs = editor.getSettings().isUseTabCharacter(project);
            this.tabWidth = editor.getSettings().getTabSize(project);
        }

    }

    public void dispose() {
        this.canceled = true;
    }

    public void cancel() {
        if (!this.canceled) {
            this.canceled = true;
            Disposer.dispose(this);
        }
    }

    public boolean equalsRequest(@NotNull InlayCompletionRequest o) {
        if (o == null) {
            //$$$reportNull$$$0(0);
        }

        if (this.params != null && o.getParams() != null) {
            return this.params.getRequestId() == o.getParams().getRequestId();
        } else {
            return false;
        }
    }

    public Disposable getDisposable() {
        return this;
    }

    
    public CompletionParams getParams() {
        return this.params;
    }

    
    public Editor getEditor() {
        return this.editor;
    }

    
    public int getCursorOffset() {
        return this.cursorOffset;
    }

    
    public boolean isUseTabs() {
        return this.useTabs;
    }

    
    public int getTabWidth() {
        return this.tabWidth;
    }

    
    public InlayCompletionCollector getCollector() {
        return this.collector;
    }

    
    public boolean isCanceled() {
        return this.canceled;
    }

    
    public void setParams(CompletionParams params) {
        this.params = params;
    }

    
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    
    public void setCursorOffset(int cursorOffset) {
        this.cursorOffset = cursorOffset;
    }

    
    public void setUseTabs(boolean useTabs) {
        this.useTabs = useTabs;
    }

    
    public void setTabWidth(int tabWidth) {
        this.tabWidth = tabWidth;
    }

    
    public void setCollector(InlayCompletionCollector collector) {
        this.collector = collector;
    }

    
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof InlayCompletionRequest)) {
            return false;
        } else {
            InlayCompletionRequest other = (InlayCompletionRequest) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label63:
                {
                    Object this$params = this.getParams();
                    Object other$params = other.getParams();
                    if (this$params == null) {
                        if (other$params == null) {
                            break label63;
                        }
                    } else if (this$params.equals(other$params)) {
                        break label63;
                    }

                    return false;
                }

                Object this$editor = this.getEditor();
                Object other$editor = other.getEditor();
                if (this$editor == null) {
                    if (other$editor != null) {
                        return false;
                    }
                } else if (!this$editor.equals(other$editor)) {
                    return false;
                }

                if (this.getCursorOffset() != other.getCursorOffset()) {
                    return false;
                } else if (this.isUseTabs() != other.isUseTabs()) {
                    return false;
                } else if (this.getTabWidth() != other.getTabWidth()) {
                    return false;
                } else {
                    label45:
                    {
                        Object this$collector = this.getCollector();
                        Object other$collector = other.getCollector();
                        if (this$collector == null) {
                            if (other$collector == null) {
                                break label45;
                            }
                        } else if (this$collector.equals(other$collector)) {
                            break label45;
                        }

                        return false;
                    }

                    if (this.isCanceled() != other.isCanceled()) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    
    protected boolean canEqual(Object other) {
        return other instanceof InlayCompletionRequest;
    }

    
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        Object $editor = this.getEditor();
        result = result * 59 + ($editor == null ? 43 : $editor.hashCode());
        result = result * 59 + this.getCursorOffset();
        result = result * 59 + (this.isUseTabs() ? 79 : 97);
        result = result * 59 + this.getTabWidth();
        Object $collector = this.getCollector();
        result = result * 59 + ($collector == null ? 43 : $collector.hashCode());
        result = result * 59 + (this.isCanceled() ? 79 : 97);
        return result;
    }

    
    public String toString() {
        CompletionParams var10000 = this.getParams();
        return "InlayCompletionRequest(params=" + var10000 + ", editor=" + this.getEditor() + ", cursorOffset=" + this.getCursorOffset() + ", useTabs=" + this.isUseTabs() + ", tabWidth=" + this.getTabWidth() + ", collector=" + this.getCollector() + ", canceled=" + this.isCanceled() + ")";
    }
}
