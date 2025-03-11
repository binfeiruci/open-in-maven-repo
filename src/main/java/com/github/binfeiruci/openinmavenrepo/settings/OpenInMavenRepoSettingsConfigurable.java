package com.github.binfeiruci.openinmavenrepo.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.awt.*;

public class OpenInMavenRepoSettingsConfigurable implements Configurable {
    private JTextField privateRepoUrlField;
    private JTextField groupIdPrefixField;
    private JTextField centralRepoUrlField;
    private final OpenInMavenRepoSettings settings;

    public OpenInMavenRepoSettingsConfigurable() {
        settings = OpenInMavenRepoSettings.getInstance();
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Open In Maven Repository";
    }

    @Override
    public JComponent createComponent() {
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        // Private repo URL field
        JPanel privateRepoPanel = new JPanel();
        privateRepoPanel.setLayout(new BoxLayout(privateRepoPanel, BoxLayout.X_AXIS));
        privateRepoPanel.setMaximumSize(new Dimension(1000, 35));
        privateRepoPanel.add(new JLabel("Private maven repository URL:"));
        privateRepoUrlField = new JTextField(30);
        privateRepoPanel.add(privateRepoUrlField);
        myPanel.add(privateRepoPanel);

        // Add some space
        myPanel.add(Box.createVerticalStrut(10));

        // Private group ID field
        JPanel groupIdPanel = new JPanel();
        groupIdPanel.setLayout(new BoxLayout(groupIdPanel, BoxLayout.X_AXIS));
        groupIdPanel.setMaximumSize(new Dimension(1000, 35));
        groupIdPanel.add(new JLabel("Private group id prefix:"));
        groupIdPrefixField = new JTextField(30);
        groupIdPanel.add(groupIdPrefixField);
        myPanel.add(groupIdPanel);

        // Add some space
        myPanel.add(Box.createVerticalStrut(10));

        // Central repo URL field
        JPanel centralRepoPanel = new JPanel();
        centralRepoPanel.setLayout(new BoxLayout(centralRepoPanel, BoxLayout.X_AXIS));
        centralRepoPanel.setMaximumSize(new Dimension(1000, 35));
        centralRepoPanel.add(new JLabel("Central maven repository URL:"));
        centralRepoUrlField = new JTextField(30);
        centralRepoPanel.add(centralRepoUrlField);
        myPanel.add(centralRepoPanel);

        return myPanel;
    }

    @Override
    public boolean isModified() {
        return !privateRepoUrlField.getText().equals(settings.getState().privateRepoUrl) ||
                !groupIdPrefixField.getText().equals(settings.getState().privateGroupIdPrefix) ||
                !centralRepoUrlField.getText().equals(settings.getState().centralRepoUrl);
    }

    @Override
    public void apply() {
        settings.getState().privateRepoUrl = privateRepoUrlField.getText();
        settings.getState().privateGroupIdPrefix = groupIdPrefixField.getText();
        settings.getState().centralRepoUrl = centralRepoUrlField.getText();
    }

    @Override
    public void reset() {
        privateRepoUrlField.setText(settings.getState().privateRepoUrl);
        groupIdPrefixField.setText(settings.getState().privateGroupIdPrefix);
        centralRepoUrlField.setText(settings.getState().centralRepoUrl);
    }
}