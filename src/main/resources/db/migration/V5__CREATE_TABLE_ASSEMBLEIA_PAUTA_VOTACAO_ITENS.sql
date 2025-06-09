-- Essa tabela é para armazenar os votos no detalhe de cada associadoo
CREATE TABLE IF NOT EXISTS assembleia_pauta_votacao_itens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo VARCHAR(6) NOT NULL UNIQUE,
    codigo_votacao VARCHAR(6) NOT NULL,
    codigo_associado VARCHAR(6) NOT NULL,
    voto VARCHAR(10) NOT NULL,
    status VARCHAR(20),
    data_hora_inclusao TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    usuario_inclusao VARCHAR(100),
    data_hora_alteracao TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    usuario_alteracao VARCHAR(100),

    CONSTRAINT fk_votacao FOREIGN KEY (codigo_votacao)
        REFERENCES assembleia_pauta_votacao(codigo)
        ON DELETE CASCADE
);