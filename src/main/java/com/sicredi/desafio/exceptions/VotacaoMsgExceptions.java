package com.sicredi.desafio.exceptions;

public class VotacaoMsgExceptions {
        public static final String REGISTRO_NAO_ENCONTRADO = "Registro com %s: '%s' não encontrado.";
        public static final String JA_EXISTE_VOTACAO_ABERTA_PARA_PAUTA = "Já existe uma votação aberta para a pauta código: '%s'. Data do encerramento: '%s'.";
        public static final String CPF_ASSOCIADO_NAO_HABILITADO = "Operação não foi concluída. O associado com código '%s' e CPF '%s' não está habilitado para participar desta votação.";
        public static final String VOTO_ASSOCIADO_DUPLICADO = "Não foi possível registrar voto em duplicidade. O associado com código '%s' já registrou um voto nessa pauta.";
        public static final String VOTACAO_NAO_ABERTA_PARA_PAUTA_INFORMADA = "A operação não pode ser concluída. A pauta com código: '%s' não possui nenhuma sessão de votação aberta no momento.";
        public static final String RANGE_STATUS_NAO_ENCONTRADO = "Status inválido. O status '%s' não é um status válido. Os status devem estar entre as opções disponíveis: [CANCELADA, COMPLETA, PENDENTE, ENVIADA, ENVIADA_COMPLETA].";
        public static final String VOTO_ASSOCIADO_INATIVO = "O voto não pôde ser registrado. O status atual do associado código '%s' é igual à: '%s'.";
        public static final String VOTO_PAUTA_NAO_ATIVA = "O voto não pôde ser registrado. O status atual da pauta código '%s' é igual à: '%s'.";
}