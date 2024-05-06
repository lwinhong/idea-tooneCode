package com.tooneCode.core.lsp;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.tooneCode.core.model.model.AuthGrantInfo;
import com.tooneCode.core.model.model.AuthLogoutResult;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.core.model.model.LoginStartResult;
import com.tooneCode.core.model.params.GetGrantInfosParams;
import com.tooneCode.core.model.params.LoginParams;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;

@JsonSegment("auth")
public interface AuthService {
    @JsonRequest
    default CompletableFuture<LoginStartResult> login(LoginParams loginParams) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<AuthStatus> status() {
        //throw new UnsupportedOperationException();
        return CompletableFuture.supplyAsync(AuthStatus::new);
    }

    @JsonRequest
    default CompletableFuture<AuthLogoutResult> logout() {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<AuthGrantInfo>> grantInfos(GetGrantInfosParams params) {
        throw new UnsupportedOperationException();
    }
}
