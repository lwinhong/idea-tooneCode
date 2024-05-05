package com.tooneCode.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementWeigher;
import com.tooneCode.completion.model.CodeCompletionItem;
import com.tooneCode.util.CompletionUtil;

public class CodeLookupElementWeigher extends LookupElementWeigher {
    public CodeLookupElementWeigher() {

        super("CodeLookupElementWeigher", false, true);
    }

    public Integer weigh(LookupElement element) {
        if (element.getObject() instanceof CodeCompletionItem) {
            CodeCompletionItem item = (CodeCompletionItem) element.getObject();
            String completionText = CompletionUtil.getCompletionText(item.getOriginItem());
            return CompletionUtil.isCompletionTextSingleToken(completionText) ? Integer.MAX_VALUE : ((CodeCompletionItem) element.getObject()).getIndex();
        } else {
            return Integer.MAX_VALUE;
        }
    }
}
