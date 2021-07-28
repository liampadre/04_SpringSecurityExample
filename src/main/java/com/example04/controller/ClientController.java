package com.example04.controller;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example04.model.ClientEntity;
import com.example04.model.ClientRequest;
import com.example04.model.ClientResponse;
import com.example04.service.ClientEntityService;
import com.example04.util.ClientConverter;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientEntityService clientEntityService;

    @PostMapping()
    public ResponseEntity<ClientResponse> saveClient(@RequestBody ClientRequest clientRequest) {
        try {
            ClientEntity newClient = ClientConverter.convertClientRequestToClientEntity(clientRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(clientEntityService.saveClient(newClient));
        } catch(DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<ClientResponse>> getClients() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clientEntityService.getClients());
    }
}
