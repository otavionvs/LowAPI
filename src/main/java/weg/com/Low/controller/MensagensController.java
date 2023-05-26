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
import weg.com.Low.model.enums.NivelAcesso;
import weg.com.Low.model.enums.StatusMensagens;
import weg.com.Low.model.enums.TipoNotificacao;
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


    public List<Demanda> filterLastVersion(List<Demanda> demandaList){
        Map<Integer, Demanda> mapaDemandas = new HashMap<>();

        // Percorrer a lista de demandas
        for (Demanda demanda : demandaList) {
            int codigoDemanda = demanda.getCodigoDemanda();

            // Verificar se já existe uma demanda para o código atual
            if (mapaDemandas.containsKey(codigoDemanda)) {
                // Se a versão da demanda atual for maior, substitui no map
                if (demanda.getVersion() > mapaDemandas.get(codigoDemanda).getVersion()) {
                    mapaDemandas.put(codigoDemanda, demanda);
                }
            } else {
                // Se não existir, adicionar ao map
                mapaDemandas.put(codigoDemanda, demanda);
            }
        }

        // Retornar apenas as demandas de maior versão
        return new ArrayList<>(mapaDemandas.values());

    }
    @GetMapping("/qtd-total-n-lidas/{codigoUsuario}")
    public ResponseEntity<?> findQtdDemandasNaoLidas(@PathVariable(value = "codigo") Integer codigoUsuario) {
        Usuario usuario = usuarioService.findById(codigoUsuario).get();
        List<Demanda> demandas = demandaService.findBySolicitanteDemandaOrAnalista(usuario);
        List<Demanda> listaDemandasVersaoMaior = filterLastVersion(demandas);
        Integer quantidadeMensagensNaoLidas = 0;
        for(Demanda demanda: listaDemandasVersaoMaior){
            List<Mensagens> mensagens = mensagensService.findAllByDemanda(demanda);
            for (Mensagens mensagem : mensagens) {
                if(mensagem.getStatusMensagens() == StatusMensagens.NAO_VISTA && mensagem.getUsuarioMensagens().getCodigoUsuario() != usuario.getCodigoUsuario()){
                    quantidadeMensagensNaoLidas++;
                }
            }
        }

        return ResponseEntity.ok(quantidadeMensagensNaoLidas);
    }

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
        if (statusAtualizado) {
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


    /**
    * Inicia um novo Chat
    * Adiciona um analista na demanda para iniciar este chat
    *
    */
    @PutMapping("/iniciarChat/{codigoDemanda}")
    public Object startChat(@PathVariable(value = "codigoDemanda") Integer codigoDemanda, HttpServletRequest request) {
        Demanda demanda = demandaService.findLastDemandaById(codigoDemanda).get();
        Demanda novaDemanda = new Demanda();
        BeanUtils.copyProperties(demanda, novaDemanda);
        Usuario usuario = usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get();
        if (usuario.getNivelAcessoUsuario() == NivelAcesso.Analista || usuario.getNivelAcessoUsuario() == NivelAcesso.GestorTI) {
            novaDemanda.setAnalista(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você não tem permissão para ser analista de uma demanda!");
        }
        return ResponseEntity.ok(demandaService.save(novaDemanda, TipoNotificacao.SEM_NOTIFICACAO));
    }

    /**
     *
     * @param codigoUsuario
     * @return Map<String, Object>
     *
     * Este método pesquisa as demandas em que o código do usuário faz parte, verifica as mensagens que não estão
     * vistas, e retorna em um map com as demandas, e a quantidade de mensagens não lidas em ordem
     *
     */
    @GetMapping("/demandasDiscutidas/{codigoUsuario}")
    public Object findAllByUsuario(@PathVariable(value = "codigoUsuario") Integer codigoUsuario) {

        if (!demandaService.existsById(codigoUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demandas não encontradas!");
        }
        Usuario usuario = usuarioService.findById(codigoUsuario).get();
        System.out.println("Usuario: " + usuario.getNomeUsuario());
        List<Demanda> listaDemandas = new ArrayList<>();
        for (Demanda demanda : demandaService.findBySolicitanteDemandaOrAnalista(usuario)) {
            if (demanda.getAnalista() != null) {
                listaDemandas.add(demanda);
            }
        }
        List<ReturnMensagens> returnMensagens = new ArrayList<>();

        for (Demanda demanda : listaDemandas) {
            List<Mensagens> mensagens = mensagensService.findAllByDemanda(demanda);
            Date dataMaisAtual = encontrarDataMaisAtual(mensagens);
            Integer qtdNaoLidas = 0;
            for (Mensagens mensagem : mensagens) {
                if (mensagem.getStatusMensagens().equals(StatusMensagens.ENVIADA)) {
                    qtdNaoLidas++;
                    usuario = mensagem.getUsuarioMensagens();
                }
            }
            returnMensagens.add(new ReturnMensagens(qtdNaoLidas, dataMaisAtual, demanda.getCodigoDemanda(), usuario));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("demandas", listaDemandas);
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
        System.out.println("Enviou uma mensagem");
        Mensagens mensagens = new Mensagens();
        Demanda demanda  = demandaService.findLastDemandaById(mensagensDTO.getDemandaMensagens().getCodigoDemanda()).get();
        if(demanda.getAnalista().getCodigoUsuario() != mensagensDTO.getUsuarioMensagens().getCodigoUsuario()){
            messagingTemplate.convertAndSend("/noticicacoes-messages/" + demanda.getAnalista().getCodigoUsuario() + "/chat", mensagens);
        }else if(demanda.getSolicitanteDemanda().getCodigoUsuario() != mensagensDTO.getUsuarioMensagens().getCodigoUsuario()){
            messagingTemplate.convertAndSend("/noticicacoes-messages/" + demanda.getSolicitanteDemanda().getCodigoUsuario() + "/chat", mensagens);
        }
        mensagensDTO.getDemandaMensagens().setVersion(demanda.getVersion());
        BeanUtils.copyProperties(mensagensDTO, mensagens);
        mensagens.setStatusMensagens(StatusMensagens.ENVIADA);
        return mensagensService.save(mensagens);
    }
}
