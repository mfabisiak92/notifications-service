package com.mafasoft.notifications.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResourceType {
    STANDARD("standard"),
    OTHER("other");

    private final String name;
}
