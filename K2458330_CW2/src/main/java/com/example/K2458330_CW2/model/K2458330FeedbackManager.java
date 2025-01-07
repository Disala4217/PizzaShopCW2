package com.example.K2458330_CW2.model;

import java.util.ArrayList;
import java.util.List;

public class K2458330FeedbackManager {
    private final List<K2458330FeedBackCommand> feedbackCommands = new ArrayList<>();

    public void executeCommand(K2458330FeedBackCommand command) {
        feedbackCommands.add(command);
        command.execute();
    }
}
