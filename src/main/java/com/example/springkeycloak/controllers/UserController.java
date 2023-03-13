package com.example.springkeycloak.controllers;
import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

import com.example.springkeycloak.dtos.UserDTO;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/users")
@RestController
public class UserController {

    private RealmResource realm = null;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realmName;

    @PostMapping(path = "/create")
    public UserDTO createUser(@RequestBody @Valid UserDTO userDTO) {

        createNewUser(userDTO);
        return userDTO;
    }

    private Keycloak getAdminKeycloakUser() {
        return KeycloakBuilder.builder().serverUrl(authServerUrl)
                .grantType("password").realm("master")
                .clientId("admin-cli")
                .username("admin")
                .password("Pa55w0rd")
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build()).build();
    }

    private RealmResource getRealm() {
        if (realm == null) {
            System.out.println("Inside create realm...........");
            realm = getAdminKeycloakUser().realm(realmName);
        }

        return realm;
    }

    public void createNewUser(UserDTO userDTO) {

        // Create User
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        Response response = getRealm().users().create(userRepresentation);

        String userId = CreatedResponseUtil.getCreatedId(response);

        addRealmRole(userDTO.getRole().toString());

        List<RoleRepresentation> roleToAdd = new ArrayList<>();
        roleToAdd.add(getRealm()
                .roles()
                .get(userDTO.getRole().toString())
                .toRepresentation()
        );

        UserResource user = getRealm()
                .users()
                .get(userId);

        user.roles().realmLevel().add(roleToAdd);

        //Set password flow
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType("password");
        passwordCred.setValue(userDTO.getPassword());

        user.resetPassword(passwordCred);
    }

    public void addRealmRole(String newRole){
        List<String> allRoles = getAllRoles();
        if(!allRoles.contains(newRole)){
            RoleRepresentation roleRep = new  RoleRepresentation();
            roleRep.setName(newRole);
            roleRep.setDescription("ROLE_" + newRole);

            getRealm().roles().create(roleRep);
        }
    }

    public List<String> getAllRoles() {
        return getRealm()
                .roles()
                .list()
                .stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
    }

}