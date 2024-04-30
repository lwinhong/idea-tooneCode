package com.tooneCode.core.model.params;

import java.util.List;

import com.tooneCode.core.model.model.ClassPair;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

public class InheritClassAnalyzeParams extends TextDocumentPositionParams {
    private List<ClassPair> classPairs;

    public InheritClassAnalyzeParams() {
    }

    public InheritClassAnalyzeParams(@NonNull TextDocumentIdentifier textDocument, @NonNull Position position) {
        super(textDocument, position);
    }

    public InheritClassAnalyzeParams(@NotNull List<ClassPair> classPairs, @NonNull TextDocumentIdentifier textDocument, @NonNull Position position) {
        this(textDocument, position);
        this.classPairs = classPairs;
    }

    public List<ClassPair> getClassPairs() {
        return this.classPairs;
    }

    public void setClassPairs(List<ClassPair> classPairs) {
        this.classPairs = classPairs;
    }

    @Pure
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this);
        b.add("classPairs", this.classPairs);
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
            InheritClassAnalyzeParams other = (InheritClassAnalyzeParams) obj;
            if (this.classPairs == null) {
                if (other.classPairs != null) {
                    return false;
                }
            } else if (!this.classPairs.equals(other.classPairs)) {
                return false;
            }

            return true;
        }
    }

    @Pure
    public int hashCode() {
        return 31 * super.hashCode() + (this.classPairs == null ? 0 : this.classPairs.hashCode());
    }
}

