package com.piromant.core.service;

import com.piromant.core.dal.NetworkUtil;
import com.piromant.core.dal.TrieRegistry;
import com.piromant.dto.request.FindRequest;
import com.piromant.dto.response.FindResponse;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TrieSearchingService {
    private final TrieRegistry trieRegistry;
    private final NetworkUtil networkUtil;
    private final ChildAskingService childAskingService;


    public FindResponse findUser(FindRequest request) {
        String prefix = request.username().substring(0, request.currentLevel());
        if (!this.trieRegistry.containsPrefix(prefix)) {
            return null;
        }
        if (request.currentLevel() == request.username().length()) {
            return new FindResponse(this.trieRegistry.getUserAddress(prefix),
                    List.of(this.networkUtil.getMyAddress()));
        }

        Character next = request.username().charAt(request.currentLevel());

        if (this.trieRegistry.getRandomChildAddress(prefix, next) == null) {
            return new FindResponse(null, List.of(this.networkUtil.getMyAddress()));
        }

        FindResponse childResult;
        if(request.currentLevel() == 0) {
            childResult = (FindResponse) this.childAskingService.getQuorumResponse(
                    request, prefix, next
            );
        } else {
            childResult = (FindResponse) this.childAskingService.getSingleResponse(
                    request, prefix, next
            );
        }

        List<String> path = childResult.path();
        path.addFirst(this.networkUtil.getMyAddress());

        return new FindResponse(childResult.result(), path);
    }


}
