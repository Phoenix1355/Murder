package net.phoenix1355.murder.commands;

public class CommandUsage {
    private final String _signature;
    private final String _description;

    public CommandUsage(String signature, String description) {
        _signature = signature;
        _description = description;
    }

    public String getSignature() {
        return _signature;
    }

    public String getDescription() {
        return _description;
    }
}
