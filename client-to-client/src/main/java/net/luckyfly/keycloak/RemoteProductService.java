package net.luckyfly.keycloak;

import java.util.Arrays;
import java.util.List;

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class RemoteProductService{

    @Autowired
    private KeycloakRestTemplate template;

    private String endpoint = "http://localhost:8081/products";

    public List<String> getProducts() {
       ResponseEntity<ProductsReponse> response = template.getForEntity(endpoint, ProductsReponse.class);
       System.out.println(response);
       return response.getBody().getProducts();
    }
}