package com.piromant.infrastructure.corecontainer.scanner;

import com.piromant.core.dal.TrieContainer;
import com.piromant.infrastructure.corecontainer.annotation.TrieContainerImplementation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class TrieContainerScanner {
    public static List<TrieContainer> findContainers() {
        List<TrieContainer> containers = new ArrayList<>();

        ServiceLoader<TrieContainer> loader = ServiceLoader.load(TrieContainer.class);
        for (TrieContainer container : loader) {
            if (container.getClass().isAnnotationPresent(TrieContainerImplementation.class)) {
                containers.add(container);
            }
        }

        return containers;
    }
}
