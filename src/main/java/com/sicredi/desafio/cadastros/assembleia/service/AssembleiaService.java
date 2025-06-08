package com.sicredi.desafio.cadastros.assembleia.service;

import com.sicredi.desafio.cadastros.assembleia.repository.AssembleiaRepository;
import com.sicredi.desafio.cadastros.assembleia.model.entity.Assembleia;
import com.sicredi.desafio.cadastros.assembleia.model.dto.AssembleiaDTO;
import com.sicredi.desafio.cadastros.assembleia.model.dto.FiltroAssembleiaDTO;
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
public class AssembleiaService {
    @Autowired
    AssembleiaRepository assembleiaRepository;
    @Autowired EntityManager entityManager;

    // Retorna uma lista filtrada ou não de assembleiass
    public PageResponse<Assembleia> findAllAssembleiasWithFilter(FiltroAssembleiaDTO filtroDTO, Pageable pageable) {
        if (filtroDTO.getStatusList() != null && !filtroDTO.getStatusList().isEmpty()) {
            for (String status : filtroDTO.getStatusList()) {
                GenericMethods.validateEnumIfPresent(status, VotacaoStatus.class,
                        new VotacaoExceptions.TEStatusInvalidoException(VotacaoMsgExceptions.RANGE_STATUS_NAO_ENCONTRADO, status));
            }
        }
        Page<Assembleia> page = DynamicFilterUtil.filter(Assembleia.class, filtroDTO, entityManager, pageable);
        return PageResponseUtil.fromPage(page);
    }

    // Salva uma assembleia
    public Assembleia saveAssembleia(AssembleiaDTO dto) {
        Assembleia assembleia = UtilMethods.mapDtoToEntity(dto, Assembleia.class);
        assembleia.setCodigo(UtilMethods.generateNextCode(assembleiaRepository, Assembleia::getCodigo));
        //TODO: vou deixar como ativo já na criação, mas poderia ter um status pendente e só após aprovação, seria liberado e o status setado como ativo
        assembleia.setStatus(RegistrationStatus.ATIVO.name());

        return assembleiaRepository.save(assembleia);
    }

    // busca por id uma assembleia
    public Assembleia findAssembleiaById(UUID id) {
        return assembleiaRepository.findById(id).orElseThrow(() ->
                new VotacaoExceptions.TERegistroNaoEncontradoException(VotacaoMsgExceptions.REGISTRO_NAO_ENCONTRADO, "ID da Assembléia: ", id));
    }
}
