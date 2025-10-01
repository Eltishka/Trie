package service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

@Testcontainers
public class NetworkRegister_Test {

    GenericContainer<?> bootstrapNode;
    GenericContainer<?> first;
    GenericContainer<?> second;
    Path dockerfilePath = Path.of("C:\\Users\\Piromant\\Documents\\yggdrasilchatdemo\\chatdemo\\src\\test\\resources");

    GenericContainer<?> createUserContainer(String rootAddr, String username, String toFind) {
        return new GenericContainer<>(
                new ImageFromDockerfile()
                        .withFileFromPath(".", dockerfilePath)
                        .withFileFromPath("app.jar",
                                Path.of("build/libs/chatdemo-1.0-SNAPSHOT.jar"))
        ).withNetwork(Network.SHARED)
                .withEnv("BOOTSTRAP_ADDR", rootAddr)
                .withEnv("USERNAME", username)
                .withEnv("TO_FIND", toFind)
                .withNetworkAliases(username);
    }

    @BeforeEach
    void init() {
        bootstrapNode = createUserContainer("bootstrap", "bootstrap", "bootstrap");
        first = createUserContainer("bootstrap", "first", "second");
        second = createUserContainer("bootstrap", "second", "first");
    }

    @Test
    void testNodesInteraction() throws Exception {
        bootstrapNode.start();
        Thread.sleep(1000);
        System.out.println("Started bootstrap node");

        first.start();
        Thread.sleep(1000);
        String logs = first.getLogs();
        System.out.println("First node logs:\n" + logs);

        second.start();
        Thread.sleep(1000);
        String logs2 = second.getLogs();
        System.out.println("Second node logs:\n" + logs2);

        System.out.println("Bootstrap node logs:\n" + bootstrapNode.getLogs());

        assertTrue(logs2.contains("Found useraddress - FindResponse[result=first, path=[bootstrap, bootstrap, bootstrap, bootstrap, bootstrap, bootstrap]]"));


    }

    private String parseYggAddr(String raw) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(raw);
        return root.at("/self/address").asText();
    }
}
