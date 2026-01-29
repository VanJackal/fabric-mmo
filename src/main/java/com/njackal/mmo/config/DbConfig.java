package com.njackal.mmo.config;

public record DbConfig(
        String url,
        String user,
        String pass
) {
}
