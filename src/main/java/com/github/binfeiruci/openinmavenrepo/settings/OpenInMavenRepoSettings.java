package com.github.binfeiruci.openinmavenrepo.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

@State(
        name = "com.github.binfeiruci.openinmavenrepo.settings.OpenInMavenRepoSettings",
        storages = @Storage("OpenInMavenRepoSettings.xml")
)
public class OpenInMavenRepoSettings implements PersistentStateComponent<OpenInMavenRepoSettings.State> {
    private State myState = new State();

    public static OpenInMavenRepoSettings getInstance() {
        return ApplicationManager.getApplication().getService(OpenInMavenRepoSettings.class);
    }

    @NotNull
    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.myState = state;
    }

    public static class State {
        public String privateRepoUrl = "http://maven.qima-inc.com/content/groups/public/";
        public String privateGroupIdPrefix = "com.youzan";
        public String centralRepoUrl = "https://central.sonatype.com/artifact/";
    }
}