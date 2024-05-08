package com.tooneCode.core.lsp;

import com.google.common.annotations.Beta;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.tooneCode.core.model.CompletionParams;
import org.eclipse.lsp4j.CallHierarchyIncomingCall;
import org.eclipse.lsp4j.CallHierarchyIncomingCallsParams;
import org.eclipse.lsp4j.CallHierarchyItem;
import org.eclipse.lsp4j.CallHierarchyOutgoingCall;
import org.eclipse.lsp4j.CallHierarchyOutgoingCallsParams;
import org.eclipse.lsp4j.CallHierarchyPrepareParams;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.ColorInformation;
import org.eclipse.lsp4j.ColorPresentation;
import org.eclipse.lsp4j.ColorPresentationParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.DeclarationParams;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.DocumentColorParams;
import org.eclipse.lsp4j.DocumentFormattingParams;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentHighlightParams;
import org.eclipse.lsp4j.DocumentLink;
import org.eclipse.lsp4j.DocumentLinkParams;
import org.eclipse.lsp4j.DocumentOnTypeFormattingParams;
import org.eclipse.lsp4j.DocumentRangeFormattingParams;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.FoldingRange;
import org.eclipse.lsp4j.FoldingRangeRequestParams;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.HoverParams;
import org.eclipse.lsp4j.ImplementationParams;
import org.eclipse.lsp4j.LinkedEditingRangeParams;
import org.eclipse.lsp4j.LinkedEditingRanges;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.Moniker;
import org.eclipse.lsp4j.MonikerParams;
import org.eclipse.lsp4j.PrepareRenameParams;
import org.eclipse.lsp4j.PrepareRenameResult;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.SelectionRange;
import org.eclipse.lsp4j.SelectionRangeParams;
import org.eclipse.lsp4j.SemanticTokens;
import org.eclipse.lsp4j.SemanticTokensDelta;
import org.eclipse.lsp4j.SemanticTokensDeltaParams;
import org.eclipse.lsp4j.SemanticTokensParams;
import org.eclipse.lsp4j.SemanticTokensRangeParams;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SignatureHelpParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.TypeDefinitionParams;
import org.eclipse.lsp4j.WillSaveTextDocumentParams;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.adapters.CodeActionResponseAdapter;
import org.eclipse.lsp4j.adapters.DocumentSymbolResponseAdapter;
import org.eclipse.lsp4j.adapters.LocationLinkListAdapter;
import org.eclipse.lsp4j.adapters.PrepareRenameResponseAdapter;
import org.eclipse.lsp4j.adapters.SemanticTokensFullDeltaResponseAdapter;
import org.eclipse.lsp4j.jsonrpc.json.ResponseJsonAdapter;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;

@JsonSegment("textDocument")
public interface TextDocumentService {
    @JsonRequest
    default CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
        throw new UnsupportedOperationException();
    }

    void cancelInlayCompletion();

    @JsonRequest(
            value = "completionItem/resolve",
            useSegment = false
    )
    default CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem unresolved) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<Hover> hover(HoverParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<SignatureHelp> signatureHelp(SignatureHelpParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    @ResponseJsonAdapter(LocationLinkListAdapter.class)
    default CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> declaration(DeclarationParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    @ResponseJsonAdapter(LocationLinkListAdapter.class)
    default CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> definition(DefinitionParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    @ResponseJsonAdapter(LocationLinkListAdapter.class)
    default CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> typeDefinition(TypeDefinitionParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    @ResponseJsonAdapter(LocationLinkListAdapter.class)
    default CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> implementation(ImplementationParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<? extends Location>> references(ReferenceParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(DocumentHighlightParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    @ResponseJsonAdapter(DocumentSymbolResponseAdapter.class)
    default CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(DocumentSymbolParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    @ResponseJsonAdapter(CodeActionResponseAdapter.class)
    default CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest(
            value = "codeAction/resolve",
            useSegment = false
    )
    default CompletableFuture<CodeAction> resolveCodeAction(CodeAction unresolved) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest(
            value = "codeLens/resolve",
            useSegment = false
    )
    default CompletableFuture<CodeLens> resolveCodeLens(CodeLens unresolved) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<? extends TextEdit>> rangeFormatting(DocumentRangeFormattingParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<? extends TextEdit>> onTypeFormatting(DocumentOnTypeFormattingParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<WorkspaceEdit> rename(RenameParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<LinkedEditingRanges> linkedEditingRange(LinkedEditingRangeParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonNotification
    void didOpen(DidOpenTextDocumentParams var1);

    @JsonNotification
    default void didChange(DidChangeTextDocumentParams var1) {

    }

    @JsonNotification
    default void didClose(DidCloseTextDocumentParams var1) {

    }

    @JsonNotification
    default void didSave(DidSaveTextDocumentParams var1) {

    }

    @JsonNotification
    default void willSave(WillSaveTextDocumentParams params) {
    }

    @JsonRequest
    default CompletableFuture<List<TextEdit>> willSaveWaitUntil(WillSaveTextDocumentParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<DocumentLink>> documentLink(DocumentLinkParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest(
            value = "documentLink/resolve",
            useSegment = false
    )
    default CompletableFuture<DocumentLink> documentLinkResolve(DocumentLink params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<ColorInformation>> documentColor(DocumentColorParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<ColorPresentation>> colorPresentation(ColorPresentationParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<FoldingRange>> foldingRange(FoldingRangeRequestParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    @ResponseJsonAdapter(PrepareRenameResponseAdapter.class)
    default CompletableFuture<Either<Range, PrepareRenameResult>> prepareRename(PrepareRenameParams params) {
        throw new UnsupportedOperationException();
    }

//    @JsonRequest
//    @Beta
//    default CompletableFuture<TypeHierarchyItem> typeHierarchy(TypeHierarchyParams params) {
//        throw new UnsupportedOperationException();
//    }
//
//    @JsonRequest(
//        value = "typeHierarchy/resolve",
//        useSegment = false
//    )
//    @Beta
//    default CompletableFuture<TypeHierarchyItem> resolveTypeHierarchy(ResolveTypeHierarchyItemParams params) {
//        throw new UnsupportedOperationException();
//    }

    @JsonRequest
    default CompletableFuture<List<CallHierarchyItem>> prepareCallHierarchy(CallHierarchyPrepareParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest(
            value = "callHierarchy/incomingCalls",
            useSegment = false
    )
    default CompletableFuture<List<CallHierarchyIncomingCall>> callHierarchyIncomingCalls(CallHierarchyIncomingCallsParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest(
            value = "callHierarchy/outgoingCalls",
            useSegment = false
    )
    default CompletableFuture<List<CallHierarchyOutgoingCall>> callHierarchyOutgoingCalls(CallHierarchyOutgoingCallsParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<SelectionRange>> selectionRange(SelectionRangeParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest(
            value = "textDocument/semanticTokens/full",
            useSegment = false
    )
    default CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest(
            value = "textDocument/semanticTokens/full/delta",
            useSegment = false
    )
    @ResponseJsonAdapter(SemanticTokensFullDeltaResponseAdapter.class)
    default CompletableFuture<Either<SemanticTokens, SemanticTokensDelta>> semanticTokensFullDelta(SemanticTokensDeltaParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest(
            value = "textDocument/semanticTokens/range",
            useSegment = false
    )
    default CompletableFuture<SemanticTokens> semanticTokensRange(SemanticTokensRangeParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<Moniker>> moniker(MonikerParams params) {
        throw new UnsupportedOperationException();
    }
}