package com.piromant.infrastructure.network.client;

import com.piromant.core.dal.RedirectingClient;
import com.piromant.dto.request.Redirectable;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RedirectClient implements RedirectingClient {

    private final int serverPort;

    @Override
    public Object redirect(Redirectable request, String url) {
        Client client = new Client(url, this.serverPort);
        client.sendRequest(request);
        Object result = client.receiveObject();
        client.close();
        return result;
    }
}
