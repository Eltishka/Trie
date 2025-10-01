package com.piromant;

import com.piromant.infrastructure.corecontainer.CoreContainer;
import com.piromant.dto.request.StoleNodeRequest;
import com.piromant.infrastructure.dal.DockerNetworkUtil;
import com.piromant.infrastructure.dal.TrieContainerMapImpl;
import com.piromant.infrastructure.network.client.RedirectClient;
import com.piromant.dto.request.FindRequest;
import com.piromant.dto.request.RegisterRequest;
import com.piromant.infrastructure.network.server.HandlerInvoker;
import com.piromant.infrastructure.network.server.Server;
import com.piromant.infrastructure.controller.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String envBootstrapAddress = System.getenv("BOOTSTRAP_ADDR");
        String envUsername = System.getenv("USERNAME");
        String toFind = System.getenv("TO_FIND");
        System.out.println("Bootstrap address: " + envBootstrapAddress);
        System.out.println("Username: " + envUsername);

        Server server = getServer();
        RedirectClient redirectClient = new RedirectClient(5892);
        new Thread(server).start();
        System.out.println("Me: " + envUsername);
        if (!envUsername.equals("bootstrap")) {
            new Thread(() -> {
                System.out.println("Me: " + envUsername);
                System.out.println(redirectClient.redirect(new RegisterRequest(new DockerNetworkUtil().getMyAddress(), envUsername, 0), envBootstrapAddress));
                System.out.println("I find: " + toFind);
                System.out.println("Found user address - " + redirectClient.redirect(new FindRequest(toFind, 0), envBootstrapAddress));
                System.out.println("I stole the Node: " + redirectClient.redirect(new StoleNodeRequest(""), envBootstrapAddress));
            }).start();
        } else {
            System.out.println("Bootstrap node started (no BOOTSTRAP_ADDR/USERNAME env provided)");
        }
    }

    private static Server getServer() {
        CoreContainer coreContainer = new CoreContainer(new TrieContainerMapImpl(), new RedirectClient(5892), new DockerNetworkUtil(), 7);
        List<Handler> handlers = List.of(new FindRequestHandler(coreContainer.getTrieSearchingService()),
                new StructureRequestsHandler(coreContainer.getTrieStructureService()),
                new NewReplicaRequestHandler(coreContainer.getTrieReplicasService()),
                new GetTrieNodeHandler(coreContainer.getTrieReplicasService()));

        HandlerInvoker handlerInvoker = new HandlerInvoker(handlers);

        Server server = new Server(5892, handlerInvoker);
        return server;
    }

}