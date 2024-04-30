package com.tooneCode.core.model.params;

import java.util.List;

import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

public class CandidateAnalyzeParams extends TextDocumentPositionParams {
    private List<TextEdit> candidates;

    public CandidateAnalyzeParams() {
    }

    public CandidateAnalyzeParams(@NonNull TextDocumentIdentifier textDocument, @NonNull Position position) {
        super(textDocument, position);
    }

    public CandidateAnalyzeParams(@NotNull List<TextEdit> candidates, @NonNull TextDocumentIdentifier textDocument, @NonNull Position position) {


        this(textDocument, position);
        this.candidates = candidates;
    }

    public List<TextEdit> getCandidates() {
        return this.candidates;
    }

    public void setCandidates(List<TextEdit> candidates) {
        this.candidates = candidates;
    }

    @Pure
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this);
        b.add("candidates", this.candidates);
        b.add("textDocument", this.getTextDocument());
        //b.add("uri", this.getUri());
        b.add("position", this.getPosition());
        return b.toString();
    }

    @Pure
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else if (!super.equals(obj)) {
            return false;
        } else {
            CandidateAnalyzeParams other = (CandidateAnalyzeParams) obj;
            if (this.candidates == null) {
                if (other.candidates != null) {
                    return false;
                }
            } else if (!this.candidates.equals(other.candidates)) {
                return false;
            }

            return true;
        }
    }

    @Pure
    public int hashCode() {
        return 31 * super.hashCode() + (this.candidates == null ? 0 : this.candidates.hashCode());
    }
}
