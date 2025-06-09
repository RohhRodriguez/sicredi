package com.sicredi.desafio.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FlyWayService {
    @Value("${DB_URL}")
    private String dbUrl;

    @Value("${DB_USERNAME}")
    private String dbUser;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    public String executeMigrations() {
        StringBuilder log = new StringBuilder();

        // TODO: Deixei dinâmico para caso seja necessário rodar em outros shemas (poderia vir como parâmetro por exemplo ou serem capturados e
        //  iterados para cada um a criação da tabela... Por enquanto ficou como fixo...)
        String schema = "sicredi";
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(dbUrl, dbUser, dbPassword)
                    .locations("classpath:db/migration")
                    .schemas(schema)
                    .baselineOnMigrate(true)
                    .validateMigrationNaming(true)
                    .load();
            flyway.migrate();
            log.append("Migração executada com sucesso!\n");
        } catch (Exception e) {
            log.append("Erro ao executar migração: ").append(e.getMessage()).append("\n");
        }
        return log.toString();
    }
}
