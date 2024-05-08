package com.tooneCode.core.model;

import java.util.Objects;

import com.tooneCode.core.model.params.CompletionContextParams;
import com.tooneCode.core.model.params.RemoteModelParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

public class CompletionParams extends TextDocumentPositionParams {
    private String requestId;
    private String fileContent;
    private Boolean useLocalModel;
    private Boolean UseRemoteModel;
    private RemoteModelParams remoteModelParams;
    private CompletionContextParams completionContextParams;


    public CompletionParams() {
    }

    public CompletionParams(@NonNull TextDocumentIdentifier textDocument, @NonNull Position position) {
        super(textDocument, position);
    }

    public CompletionParams(@NotNull String fileContent, @NonNull TextDocumentIdentifier textDocument, @NonNull Position position) {
        super(textDocument, position);
        this.fileContent = fileContent;
    }

    public CompletionParams(@NotNull String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContent() {
        return this.fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public Boolean getUseLocalModel() {
        return this.useLocalModel;
    }

    public void setUseLocalModel(Boolean useLocalModel) {
        this.useLocalModel = useLocalModel;
    }

    public Boolean getUseRemoteModel() {
        return this.UseRemoteModel;
    }

    public void setUseRemoteModel(Boolean useRemoteModel) {
        this.UseRemoteModel = useRemoteModel;
    }

    public RemoteModelParams getRemoteModelParams() {
        return this.remoteModelParams;
    }

    public void setRemoteModelParams(RemoteModelParams remoteModelParams) {
        this.remoteModelParams = remoteModelParams;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public CompletionContextParams getCompletionContextParams() {
        return this.completionContextParams;
    }

    public void setCompletionContextParams(CompletionContextParams completionContextParams) {
        this.completionContextParams = completionContextParams;
    }

    //@Pure
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this);
        b.add("requestId", this.getRequestId());
        b.add("fileContent", this.fileContent);
        b.add("textDocument", this.getTextDocument());
        //b.add("uri", this.getUri());
        b.add("position", this.getPosition());
        return b.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof CompletionParams)) {
            return false;
        } else if (!super.equals(o)) {
            return false;
        } else {
            CompletionParams that = (CompletionParams) o;
            return Objects.equals(this.requestId, that.requestId)
                    && Objects.equals(this.fileContent, that.fileContent)
                    && Objects.equals(this.useLocalModel, that.useLocalModel)
                    && Objects.equals(this.UseRemoteModel, that.UseRemoteModel);

        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{super.hashCode(), this.requestId, this.fileContent, this.useLocalModel, this.UseRemoteModel});
    }
}

