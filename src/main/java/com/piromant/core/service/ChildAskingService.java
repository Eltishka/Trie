package com.piromant.core.service;

import com.piromant.core.dal.RedirectingClient;
import com.piromant.core.dal.TrieRegistry;
import com.piromant.core.service.eventloop.EventManager;
import com.piromant.core.service.eventloop.events.BadChildrenEvent;
import com.piromant.dto.request.Redirectable;
import com.piromant.dto.request.requestfactory.RequestFactory;
import com.piromant.dto.response.Response;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class ChildAskingService {

    private final RedirectingClient redirectingClient;
    private final int minQuorumSize;
    private final float minAcceptancePart;
    private final TrieRegistry trieRegistry;
    private final EventManager eventManager;
    private final RequestFactory requestFactory;

    public Response getSingleResponse(Redirectable redirectable, String prefix, Character next) {
        return (Response) this.redirectingClient.redirect(
                this.requestFactory.createRequestForChild(redirectable),
                this.trieRegistry.getRandomChildAddress(prefix, next)
        );
    }

    public Response getQuorumResponse(Redirectable request, String prefix, Character next) {//TODO implement acceptance
        List<Response> results = IntStream.range(0, this.minQuorumSize)
                .mapToObj(_ -> this.trieRegistry.getRandomChildAddress(prefix, next))
                .map(address -> (Response) this.redirectingClient.redirect(
                        this.requestFactory.createRequestForChild(request),
                        address
                ))
                .toList();


        Response mostCommonResult = results.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        List<Response> badChildren = results.stream()
                .filter(res -> !res.equals(mostCommonResult))
                .toList();

        if(!badChildren.isEmpty()) {
            this.eventManager.publish(new BadChildrenEvent(request,
                    mostCommonResult, badChildren));
        }

        return mostCommonResult;
    }

}
