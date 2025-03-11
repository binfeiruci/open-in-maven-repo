package com.github.binfeiruci.openinmavenrepo.actions;

import com.github.binfeiruci.openinmavenrepo.settings.OpenInMavenRepoSettings;
import com.intellij.ide.BrowserUtil;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

public class OpenInMavenRepoAction extends AnAction {
    private String getMavenCentralRepoUrl() {
        return OpenInMavenRepoSettings.getInstance().getState().centralRepoUrl;
    }

    private String getMavenPrivateUrl() {
        return OpenInMavenRepoSettings.getInstance().getState().privateRepoUrl;
    }

    private String getPrivateGroupIdPrefix() {
        return OpenInMavenRepoSettings.getInstance().getState().privateGroupIdPrefix;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        NotificationGroup notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("open-in-maven-repo");

        Project project = e.getProject();
        if (project == null) {
            return;
        }

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (!(psiFile instanceof XmlFile)) {
            return;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);
        if (element == null) {
            return;
        }

        XmlTag tag = PsiTreeUtil.getParentOfType(element, XmlTag.class);
        if (tag == null) {
            Notifications.Bus.notify(notificationGroup.createNotification("The selected element is not a valid Maven artifact.", NotificationType.WARNING), project);
            return;
        }

        if ("artifactId".equals(tag.getName()) || "groupId".equals(tag.getName()) || "version".equals(tag.getName())) {
            String groupId;
            XmlTag mavenParent = tag.getParentTag().findFirstSubTag("parent");
            if (mavenParent != null) {
                groupId = mavenParent.findFirstSubTag("groupId").getValue().getText();
            } else {
                groupId = tag.getParentTag().findFirstSubTag("groupId").getValue().getText();
            }

            String artifactId = tag.getParentTag().findFirstSubTag("artifactId").getValue().getText();
            String version = null;
            if ("version".equals(tag.getName()) && !tag.getValue().getText().trim().startsWith("${")) {
                version = tag.getValue().getText();
            }

            StringBuilder sb = new StringBuilder();
            if (!getPrivateGroupIdPrefix().trim().isEmpty() && groupId.contains(getPrivateGroupIdPrefix())) {
                sb.append(getMavenPrivateUrl());
                for (String s : groupId.split("\\.")) {
                    sb.append(s).append("/");
                }
                sb.append(artifactId).append("/");
                if (version != null) {
                    sb.append(version).append("/");
                }
            } else {
                sb.append(getMavenCentralRepoUrl())
                        .append(groupId)
                        .append("/")
                        .append(artifactId);
                if (version != null) {
                    sb.append("/").append(version);
                }
            }
            BrowserUtil.browse(sb.toString());
        } else {
            Notifications.Bus.notify(notificationGroup.createNotification("The selected element is not a valid Maven artifact.", NotificationType.WARNING), project);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (null != editor) {
            boolean isPom = editor.getVirtualFile().getName().equals("pom.xml") || editor.getVirtualFile().getName().endsWith(".pom");
            e.getPresentation().setEnabledAndVisible(isPom);
        } else {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}