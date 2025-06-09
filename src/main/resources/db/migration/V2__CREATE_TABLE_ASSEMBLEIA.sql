CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS assembleia (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    codigo VARCHAR(6) NOT NULL UNIQUE,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(255),
    quantidade_membros INTEGER,
    status VARCHAR(20) NOT NULL,
    data_hora_inclusao TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    usuario_inclusao VARCHAR(100),
    data_hora_alteracao TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    usuario_alteracao VARCHAR(100)
);
