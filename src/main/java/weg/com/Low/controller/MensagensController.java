package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.MensagensDTO;
import weg.com.Low.dto.ReturnMensagens;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Mensagens;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.enums.StatusMensagens;
import weg.com.Low.model.service.*;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.MensagensService;
import weg.com.Low.security.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/mensagens")
public class MensagensController {
    private MensagensService mensagensService;
    private DemandaService demandaService;
    private UsuarioService usuarioService;
    private DemandaClassificadaService demandaClassificadaService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/{codigo}")
    public ResponseEntity<?> findAllByDemanda(@PathVariable(value = "codigo") Integer codigo, HttpServletRequest request) {
        if (!demandaService.existsById(codigo)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda não encontrada!");
        }
        Demanda demanda = demandaService.findLastDemandaById(codigo).get();
        List<Mensagens> mensagens = mensagensService.findAllByDemanda(demanda);

        TokenUtils tokenUtils = new TokenUtils();
        Usuario usuario = usuarioService.findByUserUsuario(tokenUtils.getUsuarioUsernameByRequest(request)).get();
        boolean statusAtualizado = false;
        for (Mensagens mensagem : mensagens) {
            //Se o usuário que viu a mensagem for diferente que o usuário que enviou, então ela é marcada como vista
            if ((mensagem.getUsuarioMensagens() != usuario) && (mensagem.getStatusMensagens() != StatusMensagens.VISTA)) {
                    mensagem.setStatusMensagens(StatusMensagens.VISTA);
                    statusAtualizado = true;
                    mensagensService.save(mensagem);
            }
        }
        if(statusAtualizado){
            messagingTemplate.convertAndSend("/demanda/" + codigo + "/chat", mensagens);
        }


        return ResponseEntity.ok(mensagens);
    }

    public Date encontrarDataMaisAtual(List<Mensagens> mensagens) {
        Date dataMaisAtual = null;

        for (Mensagens mensagem : mensagens) {
            if (dataMaisAtual == null || mensagem.getDataMensagens().after(dataMaisAtual)) {
                dataMaisAtual = mensagem.getDataMensagens();
            }
        }

        return dataMaisAtual;
    }

    @GetMapping("/demandasDiscutidas/{codigoUsuario}")
    public Object findAllByUsuario(@PathVariable(value = "codigoUsuario") Integer codigoUsuario) {

        if (!demandaService.existsById(codigoUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demandas não encontradas!");
        }
        Usuario usuario = usuarioService.findById(codigoUsuario).get();
        List<Demanda> listaDemandas = demandaClassificadaService.findBySolicitanteDemandaOrAnalista(usuario);
        List<ReturnMensagens> returnMensagens = new ArrayList<>();

        for (Demanda demanda: listaDemandas){
            List<Mensagens> mensagens = mensagensService.findAllByDemanda(demanda);
            Date dataMaisAtual = encontrarDataMaisAtual(mensagens);
            Integer qtdNaoLidas = 0;
            for(Mensagens mensagem: mensagens){
                if(mensagem.getStatusMensagens().equals(StatusMensagens.ENVIADA)){
                    qtdNaoLidas++;
                }
            }
            returnMensagens.add(new ReturnMensagens(qtdNaoLidas, dataMaisAtual, demanda.getCodigoDemanda()));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("demandas", listaDemandas);
        System.out.println(response);
        response.put("infoCard", returnMensagens);


        return ResponseEntity.ok(response);
    }

    //    @MessageMapping("/visualizar/{codigo}")
//    @SendTo("/demanda/{codigo}/chat")
//    public Mensagens save(@DestinationVariable Integer codigo) {
//        Mensagens mensagens = new Mensagens();
//        mensagensDTO.getDemandaMensagens().setVersion(demandaService.findLastDemandaById(codigo).get();
//        BeanUtils.copyProperties(mensagensDTO, mensagens);
//        mensagens.setStatusMensagens(StatusMensagens.ENVIADA);
//        return mensagensService.save(mensagens);
//    }
    @MessageMapping("/demanda/{codigo}")
    @SendTo("/demanda/{codigo}/chat")
    public Mensagens save(@Payload MensagensDTO mensagensDTO) {
        Mensagens mensagens = new Mensagens();
        mensagensDTO.getDemandaMensagens().setVersion(demandaService.findLastDemandaById(mensagensDTO.getDemandaMensagens().getCodigoDemanda()).get().getVersion());
        BeanUtils.copyProperties(mensagensDTO, mensagens);
        mensagens.setStatusMensagens(StatusMensagens.ENVIADA);
        return mensagensService.save(mensagens);
    }
}
