package com.sicredi.desafio.cadastros.associado.service;

import com.sicredi.desafio.cadastros.associado.model.dto.AssociadoDTO;
import com.sicredi.desafio.cadastros.associado.model.dto.FiltroAssociadoDTO;
import com.sicredi.desafio.cadastros.associado.model.entity.Associado;
import com.sicredi.desafio.cadastros.associado.repository.AssociadoRepository;
import com.sicredi.desafio.exceptions.VotacaoExceptions;
import com.sicredi.desafio.exceptions.VotacaoMsgExceptions;
import com.sicredi.desafio.utils.*;
import com.sicredi.desafio.utils.enums.RegistrationStatus;
import com.sicredi.desafio.utils.enums.VotacaoStatus;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AssociadoService {
    @Autowired
    AssociadoRepository associadoRepository;
    @Autowired EntityManager entityManager;

    // Retorna uma lista filtrada ou não de associados
    public PageResponse<Associado> findAllAssociadosWithFilter(FiltroAssociadoDTO filtroDTO, Pageable pageable) {
        if (filtroDTO.getStatusList() != null && !filtroDTO.getStatusList().isEmpty()) {
            for (String status : filtroDTO.getStatusList()) {
                GenericMethods.validateEnumIfPresent(status, VotacaoStatus.class,
                        new VotacaoExceptions.TEStatusInvalidoException(VotacaoMsgExceptions.RANGE_STATUS_NAO_ENCONTRADO, status));
            }
        }
        Page<Associado> page = DynamicFilterUtil.filter(Associado.class, filtroDTO, entityManager, pageable);
        return PageResponseUtil.fromPage(page);
    }

    // Salva uma associado
    // TODO: Poderia ser implementado no cadastro do associado uma consulta já para verificar se ele está ou não apto pela integração externa do cpf
    public Associado saveAssociado(AssociadoDTO dto) {
        Associado associado = UtilMethods.mapDtoToEntity(dto, Associado.class);
        associado.setCodigo(UtilMethods.generateNextCode(associadoRepository, Associado::getCodigo));
        //TODO: vou deixar como ativo já na criação, mas poderia ter um status pendente e só após aprovação, seria liberado e o status setado como ativo
        associado.setStatus(RegistrationStatus.ATIVO.name());

        return associadoRepository.save(associado);
    }

    // busca por id uma Associado
    public Associado findAssociadoById(UUID id) {
        return associadoRepository.findById(id).orElseThrow(() ->
                new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.REGISTRO_NAO_ENCONTRADO, "ID da Associado: ", id));
    }

}
