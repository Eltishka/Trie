package service;

import com.piromant.core.dal.NetworkUtil;
import com.piromant.core.dal.RedirectingClient;
import com.piromant.core.dal.TrieContainer;
import com.piromant.core.service.TrieStructureService;
import com.piromant.core.service.eventloop.EventManager;
import com.piromant.core.service.eventloop.events.Event;
import com.piromant.dto.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrieStructureServiceTest {

    @Mock
    private TrieContainer trieContainer;

    @Mock
    private RedirectingClient redirectingClient;

    @Mock
    private EventManager eventManager;

    @Mock
    private NetworkUtil networkUtil;

    @InjectMocks
    private TrieStructureService trieStructureService;


    @Test
    void registerNewUser_Test() {

        String username = "test";
        String userAddress = "address";

        when(trieContainer.containsPrefix(anyString())).thenReturn(true);
        when(trieContainer.getRandomChildAddress(anyString(), anyChar())).thenReturn(null);
        when(trieContainer.createNewChild(any(), any(), any())).thenReturn(true);
        doNothing().when(eventManager).publish(any(Event.class));
        when(trieContainer.registerUserAddress(anyString(), anyString())).thenReturn(true);



        RegisterRequest registerRequest = new RegisterRequest(userAddress, username, 0);
        boolean registered = trieStructureService.register(registerRequest);

        assertTrue(registered);
        verify(trieContainer, times(5)).containsPrefix(anyString());
        verify(trieContainer, times(4)).createNewChild(any(), any(), any());
        verify(trieContainer, times(1)).registerUserAddress(anyString(), anyString());
        verify(eventManager, times(5)).publish(any(Event.class));
    }



}
