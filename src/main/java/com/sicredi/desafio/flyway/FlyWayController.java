package com.sicredi.desafio.flyway;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO: desabilitei o flyway de rodar automático, pois queria ter maior controle sobre o momento de rodar as migrations...
// caso seja necessario já rodar na inicialização, teria que configurar na properties a inialização automatica (excluir a pasta empty em resources)
@RestController
@RequestMapping("migrations")
@Tag(name = "FlyWay", description = "Operações relacionadas ao FlyWay")
public class FlyWayController {

    @Autowired
    private FlyWayService flywayService;

    @GetMapping
    public ResponseEntity<String> executeManualMigration () {
        try {
            String msg = flywayService.executeMigrations();
            return ResponseEntity.status(HttpStatus.OK).body(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao realizar migrações: " + e.getClass().getSimpleName() + " - " + e.getMessage());

        }
    }

}
