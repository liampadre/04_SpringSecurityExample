package com.example04.service;

import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example04.model.ClientEntity;

@Service
@RequiredArgsConstructor
public class CustomClientDetailsService implements ClientDetailsService {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomClientDetailsService.class);

    private final ClientEntityService clientEntityService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        LOGGER.debug(String.format("Loading client by id: %s", clientId));
        Optional<ClientEntity> optional = clientEntityService.findByClientIdAndStatus(clientId, ClientEntity.Status.ACTIVE);
        if (!optional.isPresent()) {
            throw new NoSuchClientException("Client does not exist or has been revoked");
        } else {
            ClientEntity clientEntity = optional.get();
            LOGGER.debug(String.format("Active Client found: %s", clientEntity));
            return convertToClientDetails(clientEntity);
        }
    }

    private ClientDetails convertToClientDetails(ClientEntity clientEntity) {
        BaseClientDetails clientDetails = new BaseClientDetails();

        clientDetails.setClientId(clientEntity.getClientId());
        clientDetails.setClientSecret(clientEntity.getClientSecret());
        if (!CollectionUtils.isEmpty(clientEntity.getRoles())) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String role : clientEntity.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            clientDetails.setAuthorities(authorities);
        }
        if (!CollectionUtils.isEmpty(clientEntity.getRedirectURLs())) {
            clientDetails.setRegisteredRedirectUri(new HashSet<>(clientEntity.getRedirectURLs()));
        }
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put(NAME, clientEntity.getName());
        additionalInfo.put(DESCRIPTION, clientEntity.getDescription());
        clientDetails.setAdditionalInformation(additionalInfo);
        if (CollectionUtils.isEmpty(clientEntity.getGrantTypes())) {
            clientDetails.setAuthorizedGrantTypes(
                    Arrays.stream(ClientEntity.GrantTypes.values())
                            .map(value -> value.name().toLowerCase())
                            .collect(Collectors.toSet()));
        } else {
            clientDetails.setAuthorizedGrantTypes(clientEntity.getGrantTypes().stream()
                    .map(gt -> gt.name().toLowerCase())
                    .collect(Collectors.toSet()));
        }
        clientDetails.setResourceIds(Arrays.asList("oauth2-resource"));

        clientDetails.setScope(clientEntity.getScopes());

        return clientDetails;
    }
}
