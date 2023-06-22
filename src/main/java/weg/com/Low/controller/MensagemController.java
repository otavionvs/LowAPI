package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import weg.com.Low.dto.MensagemDTO;
import weg.com.Low.dto.MensagemDTO;
import weg.com.Low.dto.ReturnMensagens;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.NivelAcesso;
import weg.com.Low.model.enums.StatusMensagens;
import weg.com.Low.model.service.*;
import weg.com.Low.model.service.DemandaService;
import weg.com.Low.model.service.MensagemService;
import weg.com.Low.security.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/low/mensagens")
public class MensagemController {
    private MensagemService mensagemService;
    private DemandaService demandaService;
    private UsuarioService usuarioService;
    private DemandaClassificadaService demandaClassificadaService;
    private ConversaService conversaService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public List<Demanda> filterLastVersion(List<Demanda> demandaList) {
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



    @GetMapping("/{codigoConversa}")
    public ResponseEntity<?> findAllByConversa(@PathVariable(value = "codigoConversa") Integer codigoConversa, HttpServletRequest request) {
        if (!conversaService.existsById(codigoConversa)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conversa não encontrada!");
        }
        Conversa conversa = conversaService.findById(codigoConversa).get();
        List<Mensagem> mensagens = conversa.getMensagemConversa();


        TokenUtils tokenUtils = new TokenUtils();
        Usuario usuario = usuarioService.findByUserUsuario(tokenUtils.getUsuarioUsernameByRequest(request)).get();
        boolean statusAtualizado = false;
        for (Mensagem mensagem : mensagens) {
            //Se o usuário que viu a mensagem for diferente que o usuário que enviou, então ela é marcada como vista
            if ((mensagem.getUsuarioMensagem() != usuario) && (mensagem.getStatusMensagem() != StatusMensagens.VISTA)) {
                mensagem.setStatusMensagem(StatusMensagens.VISTA);
                statusAtualizado = true;
                mensagemService.save(mensagem);
            }
        }
        if (statusAtualizado) {
            messagingTemplate.convertAndSend("/visto/" + codigoConversa + "/chat", mensagens);
        }

        return ResponseEntity.ok(mensagens);
    }

    public Date encontrarDataMaisAtual(List<Mensagem> mensagens) {
        Date dataMaisAtual = null;

        for (Mensagem mensagem : mensagens) {
            if (dataMaisAtual == null || mensagem.getDataMensagem().after(dataMaisAtual)) {
                dataMaisAtual = mensagem.getDataMensagem();
            }
        }

        return dataMaisAtual;
    }

    @GetMapping("/verificanotificacoes")
    public ResponseEntity<Boolean> verificaNotificacoes(HttpServletRequest request) {
        TokenUtils tokenUtils = new TokenUtils();
        String username = tokenUtils.getUsuarioUsernameByRequest(request);
        Usuario usuario = usuarioService.findByUserUsuario(username).get();
        List<Conversa> conversas = conversaService.findByUsuariosConversa(usuario);
        for(Conversa conversa: conversas){
            for (Mensagem mensagem: conversa.getMensagemConversa()){
                //Se a mensagem ainda não foi vista, e não foi ele mesmo que enviou a mensagem
                if(mensagem.getStatusMensagem() != StatusMensagens.VISTA && mensagem.getUsuarioMensagem().getCodigoUsuario() != usuario.getCodigoUsuario()){
                    return ResponseEntity.status(200).body(true);
                }
            }
        }
        return ResponseEntity.status(200).body(false);
    }
    /**
     * Inicia um novo Chat
     * Adiciona um analista na demanda para iniciar este chat
     */
    @PutMapping("/iniciarChat/{codigoDemanda}")
    public Object startChat(@PathVariable(value = "codigoDemanda") Integer codigoDemanda, HttpServletRequest request) {
        Demanda demanda = demandaService.findLastDemandaById(codigoDemanda).get();
        Conversa conversa = new Conversa();
        conversa.setConversaAtiva(true);
        conversa.setDemandaConversa(demanda);
        Usuario analista = usuarioService.findByUserUsuario(new TokenUtils().getUsuarioUsernameByRequest(request)).get();
        Usuario solicitante = demanda.getSolicitanteDemanda();
        if(analista.getCodigoUsuario() == solicitante.getCodigoUsuario()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Você não pode iniciar um chat com você mesmo!");
        }
        if(analista.getNivelAcessoUsuario() == NivelAcesso.Solicitante){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Você não tem permissão para iniciar uma conversa!");
        }
        if(analista.getNivelAcessoUsuario() == NivelAcesso.GerenteNegocio){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Você não tem permissão para iniciar uma conversa!");
        }

        conversa.setUsuariosConversa(List.of(analista, solicitante));

        return ResponseEntity.ok(conversaService.save(conversa));
    }

    /**
     * @param
     * @return Map<String, Object>
     * <p>
     * Este método pesquisa as demandas em que o código do usuário faz parte, verifica as mensagens que não estão
     * vistas, e retorna em um map com as demandas, e a quantidade de mensagens não lidas em ordem
     */
    @GetMapping("/conversas/{codigoUsuario}")
    public Object findAllByUsuario(@PathVariable(value = "codigoUsuario") Integer codigoUsuario) {

        if (!usuarioService.existsById(codigoUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não existe!");
        }
        Usuario usuario = usuarioService.findById(codigoUsuario).get();
        List<Conversa> conversas = conversaService.findByUsuariosConversa(usuario);



        for (Conversa conversa : conversas) {
            List<Mensagem> listMensagem = conversa.getMensagemConversa();
            Date dataMaisAtual = encontrarDataMaisAtual(listMensagem);
            Integer qtdNaoLidas = 0;
            for (Mensagem mensagem : listMensagem) {
                if (mensagem.getStatusMensagem().equals(StatusMensagens.ENVIADA)) {
                    qtdNaoLidas++;
                    usuario = mensagem.getUsuarioMensagem();
                }
            }
            conversa.setQtdMensagensNaoLidas(qtdNaoLidas);
            conversa.setHoraUltimaMensagem(dataMaisAtual);
            conversa.setUsuarioAguardando(usuario);
        }

        return ResponseEntity.ok(conversas);
    }

    @Transactional
    @MessageMapping("/visto/{codigoConversa}")
    @SendTo("/visto/{codigoConversa}/chat")
    public String visto(@DestinationVariable Integer codigoConversa, MensagemDTO nulo) {
        Conversa conversa = conversaService.findById(codigoConversa).get();
        List<Mensagem> mensagens = conversa.getMensagemConversa();
        for(Mensagem mensagem: mensagens){
            System.out.println("No for");
            mensagem.setStatusMensagem(StatusMensagens.VISTA);
            mensagemService.save(mensagem);
        }

        return "Visto";
    }

    @Transactional
    @MessageMapping("/demanda/{codigoConversa}")
    @SendTo("/demanda/{codigoConversa}/chat")
    public Mensagem save(@Payload MensagemDTO mensagemDTO) {
        Mensagem mensagem = new Mensagem();
        Conversa conversa = conversaService.findById(mensagemDTO.getConversaMensagem().getCodigoConversa()).get();
        BeanUtils.copyProperties(mensagemDTO, mensagem);
        mensagem.setConversa(conversa);
        Usuario usuarioMensagem = usuarioService.findById(mensagem.getUsuarioMensagem().getCodigoUsuario()).get();

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.addAll(conversa.getUsuariosConversa());
        for (Usuario usuario: usuarios){
            //Se o usuário que enviou a mensagem for diferente do que estiver cadastrado na mensagem ele notifica o usuário
            if(usuarioMensagem.getCodigoUsuario() != usuario.getCodigoUsuario()){
                messagingTemplate.convertAndSend("/notifica/" + usuario.getCodigoUsuario(), conversa.getCodigoConversa());
            }
        }


        mensagem.setStatusMensagem(StatusMensagens.ENVIADA);
        System.out.println(mensagem.getTextoMensagem());
        return mensagemService.save(mensagem);
    }
}
