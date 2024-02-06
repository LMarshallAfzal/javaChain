package com.leonard.network;

import org.apache.commons.validator.routines.InetAddressValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class Node {
    private final UUID uuid;
    private final String ipAddress;
    private final int port;
    private NodeStatus status;
    private final LocalDateTime lastActive;

    public enum NodeStatus {
        ACTIVE,
        INACTIVE,
        OFFLINE,
        PENDING
    }

    public Node(String ipAddress, int port, NodeStatus status, String lastActive) {
        try {
            this.lastActive = LocalDateTime.parse(lastActive);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid timestamp format. Expected format: yyyy-MM-ddTHH:mm:ss", e);
        }

        InetAddressValidator validator = InetAddressValidator.getInstance();
        if (validator.isValid(ipAddress)) {
            this.ipAddress = ipAddress;
        } else {
            throw new IllegalArgumentException("Invalid IP address. Expected format: x.x.x.x, where x is in range 0-255");
        }

        if (port < 65535 && port > 0 ) {
            this.port = port;
        } else {
            throw new IllegalArgumentException("Invalid port number. The port number must be between 0-65535");
        }

        this.uuid = UUID.randomUUID();
        this.status = status;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    @Override
    public String toString() {
        return "Node {" +
                "UUID=" + uuid +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                ", status='" + status + '\'' +
                ", lastActive=" + lastActive +
                '}';
    }
}
