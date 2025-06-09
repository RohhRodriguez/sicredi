package com.sicredi.desafio.integracoes.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.desafio.utils.ValidadorCPFUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class IntegracaoService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // TODO: como está fora do ar, vou desconsiderar e usar o mock
    @Value("${URL_SISTEMA_EXTERNO}") // ex: https://user-info.herokuapp.com/users
    private String urlSistemaExterno;

    public IntegracaoService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // TODO: Poderia ser implementado no cadastro do associado uma consulta já para verificar se ele está ou não apto para votar
    //  se o cpf for válido no caso... Mas de toda a forma, já estou fazendo essa validação no momento do registro do voto
    public boolean validaCPF(String cpf) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlSistemaExterno + "/" + cpf)).GET().header("Accept", "application/json").build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            //TODO: Vou ignorar a resposta real pq está fora e usar o meu mock de validação (json)
            String json = ValidadorCPFUtil.responseValidacaoCPFMock(cpf); // retorna {"status": "ABLE_TO_VOTE"} ou {"status": "UNABLE_TO_VOTE"}
            //if (response.statusCode() == 200) {
                //String body = response.body();
                //JsonNode jsonNode = objectMapper.readTree(body);
                JsonNode jsonNode = objectMapper.readTree(json); // pegando do mock
                String status = jsonNode.get("status").asText(); //chave = "status"

                System.out.println("Resposta CPF: " + status); // usando a mockada por enquanto

                return "ABLE_TO_VOTE".equalsIgnoreCase(status); // só retorma true se for igual ao esperado liberado para votar
            //}
        } catch (Exception e) {
            System.out.println("Erro no retonro.");
            return false;
        }
        //return false;
    }

}

