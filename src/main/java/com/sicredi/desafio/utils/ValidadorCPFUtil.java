package com.sicredi.desafio.utils;

public class ValidadorCPFUtil {

    //TODO: Como o validador está fora do ar, criei esse método manual para validação do cpf para conseguir simular a validação da api
    public static boolean validarCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^\\d]", "");

        // Verifica se o CPF tem 11 dígitos ou é uma sequência repetida
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) primeiroDigito = 0;
            if (primeiroDigito != (cpf.charAt(9) - '0')) return false;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) segundoDigito = 0;
            return segundoDigito == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }

    // TODO: mockando a resposta pq o serviço esta fora do ar
    public static String responseValidacaoCPFMock(String cpf) {
        try {
            // mock da resposta
            boolean isValid = validarCPF(cpf);

            // JSON fake
            String status = isValid ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE";
            String json = String.format("{\"status\": \"%s\"}", status);

            System.out.println("Resposta CPF (mock): " + json);
            return json;
        } catch (Exception e) {
            System.out.println("Erro no retorno.");
            return "{\"status\": \"ERRO\"}";
        }
    }

}
