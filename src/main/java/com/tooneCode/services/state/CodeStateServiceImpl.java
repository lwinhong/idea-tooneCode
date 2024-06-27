package com.tooneCode.services.state;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

@Service
@State(name = "CodeStateService", storages = {@Storage("CodeStateService.xml")})
public final class CodeStateServiceImpl implements PersistentStateComponent<CodeStateServiceImpl.State>, CodeStateService {

    public static class State {
        public String appId;
    }

    private State myState = new State();

    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }
}
