CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS assembleia_pauta_votacao (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    codigo VARCHAR(6) NOT NULL UNIQUE,
    codigo_assembleia VARCHAR(6) NOT NULL,
    codigo_pauta VARCHAR(6) NOT NULL,
    duracao TIME,
    total_votos INTEGER,
    resultado VARCHAR(3),
    percentual_votos FLOAT,
    status VARCHAR(20),
    data_hora_inicio TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    data_hora_encerramento TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    data_hora_inclusao TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    usuario_inclusao VARCHAR(100),
    data_hora_alteracao TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    usuario_alteracao VARCHAR(100),

    CONSTRAINT fk_pauta FOREIGN KEY (codigo_pauta)
        REFERENCES assembleia_pauta(codigo)
        ON DELETE CASCADE,

    CONSTRAINT fk_assembleia FOREIGN KEY (codigo_assembleia)
        REFERENCES assembleia(codigo)
        ON DELETE CASCADE
);
