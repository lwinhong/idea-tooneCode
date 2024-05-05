package com.tooneCode.completion;

import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementDecorator;
import com.tooneCode.completion.model.CodeCompletionItem;
import org.jetbrains.annotations.NotNull;

public class CodePrefixMatcher extends PrefixMatcher {
    private final PrefixMatcher originMatcher;

    public CodePrefixMatcher(PrefixMatcher originMatcher) {
        super(originMatcher.getPrefix());
        this.originMatcher = originMatcher;
    }

    public boolean prefixMatches(@NotNull LookupElement element) {
        if (element.getObject() instanceof CodeCompletionItem) {
            return true;
        } else {
            return element instanceof LookupElementDecorator ? this.prefixMatches(((LookupElementDecorator<?>) element).getDelegate()) : super.prefixMatches(element);
        }
    }

    public boolean isStartMatch(LookupElement element) {
        return element.getObject() instanceof CodeCompletionItem || super.isStartMatch(element);
    }

    public boolean prefixMatches(@NotNull String name) {
        return this.originMatcher.prefixMatches(name);
    }

    public @NotNull PrefixMatcher cloneWithPrefix(@NotNull String prefix) {
        return new CodePrefixMatcher(this.originMatcher.cloneWithPrefix(prefix));
    }
}