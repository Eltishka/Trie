package com.piromant.infrastructure.corecontainer.scanner;

import com.piromant.core.dal.RedirectingClient;
import com.piromant.core.dal.TrieContainer;
import com.piromant.infrastructure.corecontainer.annotation.RedirectingClientImplementation;
import com.piromant.infrastructure.corecontainer.annotation.TrieContainerImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class RedirectingClientScanner {

    public static List<RedirectingClient> findRedirectingClients() {
        List<RedirectingClient> redirectingClients = new ArrayList<>();

        ServiceLoader<RedirectingClient> loader = ServiceLoader.load(RedirectingClient.class);
        for (RedirectingClient container : loader) {
            if (container.getClass().isAnnotationPresent(RedirectingClientImplementation.class)) {
                redirectingClients.add(container);
            }
        }

        return redirectingClients;
    }
}
