package com.example.K2458330_CW2.model;

import java.util.ArrayList;
import java.util.List;

public class K2458330OrderInvoker {
    private final List<K2458330OrderCommand> commandQueue = new ArrayList<>();
    private K2458330OrderCommand command;

    public void setCommand(K2458330OrderCommand command) {
        this.command = command;
    }

    public void executeCommand() {
        if (command != null) {
            command.execute();
        }
    }

    public void addCommand(K2458330OrderCommand command) {
        commandQueue.add(command);
    }

    public void executeCommands() {
        for (K2458330OrderCommand command : commandQueue) {
            command.execute();
        }
        commandQueue.clear();
    }


}

