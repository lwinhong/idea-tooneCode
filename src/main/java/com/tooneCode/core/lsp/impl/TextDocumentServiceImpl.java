package com.tooneCode.core.lsp.impl;

import com.tooneCode.core.api.CodeGenerateApiManager;
import com.tooneCode.core.api.CodeGenerateResponse;
import com.tooneCode.core.lsp.LanguageClient;
import com.tooneCode.core.lsp.TextDocumentService;
import com.tooneCode.core.model.CompletionParams;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TextDocumentServiceImpl implements TextDocumentService {
    private final LanguageClient languageClient;
    private CodeGenerateResponse lastResponse;

    public TextDocumentServiceImpl(LanguageClient languageClient) {
        this.languageClient = languageClient;
    }

    @Override
    public void cancelInlayCompletion() {
        if (lastResponse != null) {
            lastResponse.dispose();
        }
        lastResponse = null;
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams params) {
        cancelInlayCompletion();
        var items = new ArrayList<CompletionItem>();
        var generateLength = params.getRemoteModelParams().getGenerateLength();
        var requestData = getRequestData(params);
        requestData.put("generateLength", generateLength);
        var request = lastResponse = CodeGenerateApiManager.codeGenerateRequest(requestData, (response) -> {
            if ("line".equals(generateLength)) {
                if (response.endsWith("\n") && StringUtils.isNotEmpty(response.trim())) {
                    lastResponse.cancel();
                    return false;
                }
            }
            return true;
        });
        if (request == null)
            return CompletableFuture.completedFuture(Either.forLeft(items));

        String code = request.getResult();
        if (StringUtils.isBlank(code)) {
            return CompletableFuture.completedFuture(Either.forLeft(items));
        }
        String finalCode = code.trim();
        return CompletableFuture.supplyAsync(() -> {
            CompletionItem item = new CompletionItem();
            item.setLabel(finalCode);
            item.setKind(CompletionItemKind.Text);
            item.setDetail(finalCode);
            item.setDocumentation(finalCode);
            item.setInsertText(finalCode);
            item.setInsertTextFormat(InsertTextFormat.PlainText);
            var edit = new TextEdit();

            var position = params.getPosition();

            edit.setRange(new Range(new Position(position.getLine(), position.getCharacter()),
                    new Position(position.getLine(), position.getCharacter() + 4)));
            item.setTextEdit(Either.forLeft(edit));
            items.add(item);

            Either<List<CompletionItem>, CompletionList> list = Either.forLeft(items);
            languageClient.collectCompletionResult(item);

            //lastResponse = null;
            return list;
        });
    }


    private static @NotNull Map<String, Object> getRequestData(CompletionParams position) {
        var measurements = position.getCompletionContextParams().getMeasurements();
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("chatType", "code");
        requestData.put("prompt", measurements.getPrefixCode());
        requestData.put("laterCode", measurements.getSuffixCode());
        requestData.put("filePath", measurements.getFileName());
        requestData.put("stream", Boolean.TRUE);
        requestData.put("requestId", position.getRequestId());
        requestData.put("max_length ", 200);
        requestData.put("lang", measurements.getLanguage());
        return requestData;
    }

    @Override
    public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem unresolved) {
        return TextDocumentService.super.resolveCompletionItem(unresolved);
    }

    @Override
    public CompletableFuture<Hover> hover(HoverParams params) {
        return TextDocumentService.super.hover(params);
    }

    @Override
    public CompletableFuture<SignatureHelp> signatureHelp(SignatureHelpParams params) {
        return TextDocumentService.super.signatureHelp(params);
    }

    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> declaration(DeclarationParams params) {
        return TextDocumentService.super.declaration(params);
    }

    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> definition(DefinitionParams params) {
        return TextDocumentService.super.definition(params);
    }

    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> typeDefinition(TypeDefinitionParams params) {
        return TextDocumentService.super.typeDefinition(params);
    }

    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> implementation(ImplementationParams params) {
        return TextDocumentService.super.implementation(params);
    }

    @Override
    public CompletableFuture<List<? extends Location>> references(ReferenceParams params) {
        return TextDocumentService.super.references(params);
    }

    @Override
    public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(DocumentHighlightParams params) {
        return TextDocumentService.super.documentHighlight(params);
    }

    @Override
    public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(DocumentSymbolParams params) {
        return TextDocumentService.super.documentSymbol(params);
    }

    @Override
    public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
        return TextDocumentService.super.codeAction(params);
    }

    @Override
    public CompletableFuture<CodeAction> resolveCodeAction(CodeAction unresolved) {
        return TextDocumentService.super.resolveCodeAction(unresolved);
    }

    @Override
    public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams params) {
        return TextDocumentService.super.codeLens(params);
    }

    @Override
    public CompletableFuture<CodeLens> resolveCodeLens(CodeLens unresolved) {
        return TextDocumentService.super.resolveCodeLens(unresolved);
    }

    @Override
    public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams params) {
        return TextDocumentService.super.formatting(params);
    }

    @Override
    public CompletableFuture<List<? extends TextEdit>> rangeFormatting(DocumentRangeFormattingParams params) {
        return TextDocumentService.super.rangeFormatting(params);
    }

    @Override
    public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(DocumentOnTypeFormattingParams params) {
        return TextDocumentService.super.onTypeFormatting(params);
    }

    @Override
    public CompletableFuture<WorkspaceEdit> rename(RenameParams params) {
        return TextDocumentService.super.rename(params);
    }

    @Override
    public CompletableFuture<LinkedEditingRanges> linkedEditingRange(LinkedEditingRangeParams params) {
        return TextDocumentService.super.linkedEditingRange(params);
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams var1) {

    }

    @Override
    public void didChange(DidChangeTextDocumentParams var1) {
        TextDocumentService.super.didChange(var1);
    }

    @Override
    public void didClose(DidCloseTextDocumentParams var1) {
        TextDocumentService.super.didClose(var1);
    }

    @Override
    public void didSave(DidSaveTextDocumentParams var1) {
        TextDocumentService.super.didSave(var1);
    }

    @Override
    public void willSave(WillSaveTextDocumentParams params) {
        TextDocumentService.super.willSave(params);
    }

    @Override
    public CompletableFuture<List<TextEdit>> willSaveWaitUntil(WillSaveTextDocumentParams params) {
        return TextDocumentService.super.willSaveWaitUntil(params);
    }

    @Override
    public CompletableFuture<List<DocumentLink>> documentLink(DocumentLinkParams params) {
        return TextDocumentService.super.documentLink(params);
    }

    @Override
    public CompletableFuture<DocumentLink> documentLinkResolve(DocumentLink params) {
        return TextDocumentService.super.documentLinkResolve(params);
    }

    @Override
    public CompletableFuture<List<ColorInformation>> documentColor(DocumentColorParams params) {
        return TextDocumentService.super.documentColor(params);
    }

    @Override
    public CompletableFuture<List<ColorPresentation>> colorPresentation(ColorPresentationParams params) {
        return TextDocumentService.super.colorPresentation(params);
    }

    @Override
    public CompletableFuture<List<FoldingRange>> foldingRange(FoldingRangeRequestParams params) {
        return TextDocumentService.super.foldingRange(params);
    }

    @Override
    public CompletableFuture<Either<Range, PrepareRenameResult>> prepareRename(PrepareRenameParams params) {
        return TextDocumentService.super.prepareRename(params);
    }

    @Override
    public CompletableFuture<List<CallHierarchyItem>> prepareCallHierarchy(CallHierarchyPrepareParams params) {
        return TextDocumentService.super.prepareCallHierarchy(params);
    }

    @Override
    public CompletableFuture<List<CallHierarchyIncomingCall>> callHierarchyIncomingCalls(CallHierarchyIncomingCallsParams params) {
        return TextDocumentService.super.callHierarchyIncomingCalls(params);
    }

    @Override
    public CompletableFuture<List<CallHierarchyOutgoingCall>> callHierarchyOutgoingCalls(CallHierarchyOutgoingCallsParams params) {
        return TextDocumentService.super.callHierarchyOutgoingCalls(params);
    }

    @Override
    public CompletableFuture<List<SelectionRange>> selectionRange(SelectionRangeParams params) {
        return TextDocumentService.super.selectionRange(params);
    }

    @Override
    public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
        return TextDocumentService.super.semanticTokensFull(params);
    }

    @Override
    public CompletableFuture<Either<SemanticTokens, SemanticTokensDelta>> semanticTokensFullDelta(SemanticTokensDeltaParams params) {
        return TextDocumentService.super.semanticTokensFullDelta(params);
    }

    @Override
    public CompletableFuture<SemanticTokens> semanticTokensRange(SemanticTokensRangeParams params) {
        return TextDocumentService.super.semanticTokensRange(params);
    }

    @Override
    public CompletableFuture<List<Moniker>> moniker(MonikerParams params) {
        return TextDocumentService.super.moniker(params);
    }
}
