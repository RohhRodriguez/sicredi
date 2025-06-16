package com.sicredi.desafio.cadastros.email;

import com.sicredi.desafio.cadastros.associado.model.entity.Associado;
import com.sicredi.desafio.cadastros.associado.repository.AssociadoRepository;
import com.sicredi.desafio.cadastros.votacao.model.entity.Voto;
import com.sicredi.desafio.cadastros.votacao.repository.VotacaoRepository;
import com.sicredi.desafio.cadastros.votacao.repository.VotoRepository;
import com.sicredi.desafio.cadastros.votacao.scheduler.filas.ResultadoVotacaoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailService {
    @Autowired private AssociadoRepository associadoRepository;
    @Autowired private VotacaoRepository votacaoRepository;
    @Autowired private VotoRepository votoRepository;
    @Autowired private JavaMailSender mailSender;

    @Value("${destinatario-padrao}")
    private String destinatarioPadrao;

    // TODO: No caso eu deixei preparado para enviar para todos os associados que votaram caso seja necessário e caso não seja passado o boolean, envia só para o email do adm
    public void enviarResultado(ResultadoVotacaoMessage resultado, boolean enviarParaTodosOsdestinatarios, String destinatario, String assunto, String corpo) {
        List<Voto> votos = votoRepository.findAllByCodigoVotacao(resultado.getCodigo());
        destinatario = destinatario != null ? destinatario : destinatarioPadrao;

        if (enviarParaTodosOsdestinatarios) {
            for (Voto voto : votos) {
                Optional<Associado> associado = associadoRepository.findByCodigo(voto.getCodigoAssociado());
                if (associado.isPresent() && associado.get().getEmail() != null) {
                    destinatario = associado.get().getEmail();
                }
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(destinatario);
                message.setSubject(assunto);
                message.setText(corpo);
                mailSender.send(message);
            }
        } else {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinatario);
            message.setSubject(assunto);
            message.setText(corpo);
            mailSender.send(message);
        }
    }

}
