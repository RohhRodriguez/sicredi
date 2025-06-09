package com.sicredi.desafio.exceptions;

public class VotacaoExceptions {

    public static class TERegistroNaoEncontradoException extends RuntimeException {
        public TERegistroNaoEncontradoException(String message, Object... args) {
            super(String.format(message, args));
        }
    }

    public static class TEStatusInvalidoException extends RuntimeException {
        public TEStatusInvalidoException(String message, Object... args) {
            super(String.format(message, args));
        }
    }

    public static class TEFiltroInvalidoException extends RuntimeException {
        public TEFiltroInvalidoException(String message, Object... args) {
            super(String.format(message, args));
        }
    }

    public static class TEVotacaoInvalidoException extends RuntimeException {
        public TEVotacaoInvalidoException(String message, Object... args) {
            super(String.format(message, args));
        }
    }
}
