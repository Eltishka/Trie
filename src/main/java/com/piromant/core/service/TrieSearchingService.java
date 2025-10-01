package com.piromant.core.service;

import com.piromant.core.dal.NetworkUtil;
import com.piromant.core.dal.RedirectingClient;
import com.piromant.core.dal.TrieRegistry;
import com.piromant.core.service.eventloop.EventManager;
import com.piromant.core.service.eventloop.events.BadFinderChildrenEvent;
import com.piromant.dto.request.FindRequest;
import com.piromant.dto.response.FindResponse;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class TrieSearchingService {
    private final TrieRegistry trieRegistry;
    private final RedirectingClient redirectingClient;
    private final EventManager eventManager;
    private final NetworkUtil networkUtil;
    private final int rootRedirectCount;


    public FindResponse findUser(FindRequest request) {
        String prefix = request.username().substring(0, request.currentLevel());
        if (!this.trieRegistry.containsPrefix(prefix)) {
            return null;
        }
        if (request.currentLevel() == request.username().length()) {
            return new FindResponse(this.trieRegistry.getUserAddress(prefix),
                    List.of(this.networkUtil.getMyAddress()));
        }

        int redirectCount = request.currentLevel() == 0 ? rootRedirectCount : 1;

        Character next = request.username().charAt(request.currentLevel());

        if (this.trieRegistry.getRandomChildAddress(prefix, next) == null) {
            return new FindResponse(null, List.of(this.networkUtil.getMyAddress()));
        }

        List<FindResponse> results = IntStream.range(0, redirectCount)
                .mapToObj(_ -> this.trieRegistry.getRandomChildAddress(prefix, next))
                .map(address -> (FindResponse) this.redirectingClient.redirect(
                        new FindRequest(request.username(), request.currentLevel() + 1),
                        address
                ))
                .toList();


        FindResponse mostCommonResult = results.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        List<String> path = mostCommonResult.path();
        path.addFirst(this.networkUtil.getMyAddress());
        FindResponse result = new FindResponse(mostCommonResult.result(), path);

        List<FindResponse> badChildren = results.stream()
                .filter(res -> !res.equals(result))
                .toList();

        if(!badChildren.isEmpty()) {
            this.eventManager.publish(new BadFinderChildrenEvent(request,
                    result.result(), badChildren));
        }

        return result;
    }


}
