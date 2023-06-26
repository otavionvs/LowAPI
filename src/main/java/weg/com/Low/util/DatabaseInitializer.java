package weg.com.Low.util;

import com.github.javafaker.Faker;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.*;
import weg.com.Low.model.service.RecursoService;
import weg.com.Low.repository.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class DatabaseInitializer implements CommandLineRunner{
    static ModelMapper modelMapper = new ModelMapper();
    static boolean vez = true;
    static int vezComissao = 0;
    static Usuario analista = new Usuario();
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private DemandaRepository demandaRepository;
    @Autowired
    private CentroCustoRepository centroCustoRepository;
    @Autowired
    private DemandaClassificadaRepository demandaClassificadaRepository;
    @Autowired
    private PropostaRepository propostaRepository;
    @Autowired
    private ReuniaoRepository reuniaoRepository;
    @Autowired
    private PersonalizacaoRepository personalizacaoRepository;


    public CentroCusto gerarCentroCusto() {
        CentroCusto centroCusto = new CentroCusto();
        centroCusto.setNomeCentroCusto(Faker.instance().name().bloodGroup());
        centroCusto.setPorcentagemCentroCusto(100);
        return centroCusto;
    }

    public Proposta gerarProposta(DemandaClassificada demanda) {
        Proposta proposta = modelMapper.map(demanda, Proposta.class);
        proposta.setPrazoProposta(Faker.instance().date().birthday());
        proposta.setCodigoPPMProposta(12345);
        proposta.setJiraProposta(Faker.instance().lorem().characters(30));
        proposta.setInicioExDemandaProposta(Faker.instance().date().birthday());
        proposta.setFimExDemandaProposta(Faker.instance().date().birthday());
        proposta.setPaybackProposta(Faker.instance().number().randomDouble(2, 1, 50000));
        proposta.setEscopoDemandaProposta(Faker.instance().leagueOfLegends().summonerSpell());
        proposta.setVersion(demanda.getVersion() + 1);
        if(vez){
            proposta.setStatusDemanda(Status.ASSESSMENT);
        }else{
            proposta.setStatusDemanda(Status.BUSINESS_CASE);
        }
        vez = !vez;
        List<String> responsaveis = new ArrayList<>();

        for(int i = 0; i< 5; i++){
            responsaveis.add(Faker.instance().gameOfThrones().character());
        }

        proposta.setResponsavelProposta(responsaveis);
        List<Recurso> listRecurso = new ArrayList<>();

        for(int i =0; i< 10; i++){
            Recurso recurso = new Recurso();
            recurso.setNomeRecurso(Faker.instance().gameOfThrones().house());
            List<CentroCusto> cc = new ArrayList<>();
            recurso.setCentroCustoRecurso(cc);
            recurso.setPerfilDespesaRecurso("Corporativo");
            recurso.setPeriodoExMesesRecurso(6);
            recurso.setQuantidadeHorasRecurso(Faker.instance().number().randomDigitNotZero());
            recurso.setTipoDespesaRecurso(TipoDespesa.interno);
            recurso.setValorHoraRecurso(Faker.instance().number().randomDouble(3, 1, 999));
            listRecurso.add(recurso);
        }
        proposta.setRecursosProposta(listRecurso);
        return proposta;
    }

    public DemandaClassificada gerarDemandaClassificada(Demanda demanda) {
        DemandaClassificada demandaClassificada = modelMapper.map(demanda, DemandaClassificada.class);
        demandaClassificada.setTamanhoDemandaClassificada(TamanhoDemanda.Grande);
        demandaClassificada.setBuSolicitanteDemandaClassificada(BussinessUnit.WAU);
        demandaClassificada.setBusBeneficiadasDemandaClassificada(List.of(BussinessUnit.WAU));
        demandaClassificada.setAnalista(analista);
        demandaClassificada.setSecaoDemandaClassificada(Secao.AAS);
        demandaClassificada.setVersion(1);
        demandaClassificada.setStatusDemanda(Status.BACKLOG_PROPOSTA);
        return demandaClassificada;
    }

    public Personalizacao gerarPersonalizacao(){
        Personalizacao personalizacao = new Personalizacao();
        personalizacao.setNomePersonalizacao("Cores WEG");
        personalizacao.setAtivaPersonalizacao(true);
        personalizacao.setCodigoPersonalizacao(1);
        personalizacao.setCoresPrimariasPersonalizacao(List.of("#72BBF7","#00579D" ,"#00579D", "#00579D","#8862A2", "#FFDD43", "#B4B0A8", "#EF8300", "#000000", "#0091BD", "#EA1010", "#00612E"));
        personalizacao.setCoresSecundariasPersonalizacao(List.of("#A7D5FB", "#4889B8", "#4889B8", "#4889B8", "#B389CF", "#FFF0AA", "#D2CFC7", "#FCC17A", "#494949", "#65CEEE", "#FF8383", "#529572"));
        personalizacao.setCoresPrimariasReuniaoPersonalizacao(List.of("#00579D", "#EF8300", "#8862A2", "#00612E", "#EA1010"));
        personalizacao.setCoresSecundariasReuniaoPersonalizacao(List.of("#4889B8", "#FCC17A", "#B389CF", "#529572", "#FF8383"));
        return personalizacao;
    }
    public Demanda gerarDemanda(Usuario usuario, Integer codigoDemanda) {
        Demanda demanda = new Demanda();
        demanda.setCodigoDemanda(codigoDemanda);
        demanda.setVersion(0);
        demanda.setStatusDemanda(Status.BACKLOG_CLASSIFICACAO);
        demanda.setTituloDemanda(Faker.instance().name().title());
        demanda.setSituacaoAtualDemanda(Faker.instance().gameOfThrones().quote());
        demanda.setObjetivoDemanda(Faker.instance().lorem().sentence());
        demanda.setFrequenciaDeUsoDemanda("MuitoAlta");
        demanda.setSolicitanteDemanda(usuario);

        Beneficio beneficio = new Beneficio();

        beneficio.setMoedaBeneficio(Moeda.Real);
        beneficio.setMemoriaDeCalculoBeneficio(Faker.instance().lorem().fixedString(1000));
        beneficio.setValorBeneficio(Faker.instance().number().randomDouble(5, 500, 50000000));

        demanda.setBeneficioPotencialDemanda(beneficio);

        Beneficio beneficio2 = new Beneficio();

        beneficio2.setMoedaBeneficio(Moeda.Dollar);
        beneficio2.setMemoriaDeCalculoBeneficio(Faker.instance().lorem().fixedString(1000));
        beneficio2.setValorBeneficio(Faker.instance().number().randomDouble(5, 500, 50000000));

        demanda.setBeneficioRealDemanda(beneficio2);


        demanda.setBeneficioQualitativoDemanda(Faker.instance().lorem().fixedString(1000));

        List<CentroCusto> listaCentroCusto = new ArrayList<>();
        listaCentroCusto.add(gerarCentroCusto());
        demanda.setCentroCustosDemanda(centroCustoRepository.saveAll(listaCentroCusto));
        return demanda;
    }
    public Reuniao gerarReuniao(int codigoReuniao, List<Proposta> demandas){
        Reuniao reuniao = new Reuniao();
        reuniao.setDataReuniao(Faker.instance().date().future(60, TimeUnit.DAYS));
        reuniao.setCodigoReuniao(codigoReuniao);
        vezComissao++;
        if(vezComissao == 1){

            reuniao.setComissaoReuniao(Comissao.CPGCI);
        }else if(vezComissao == 2){
            reuniao.setComissaoReuniao(Comissao.CTI);

        }else if(vezComissao == 3){

            reuniao.setComissaoReuniao(Comissao.CPVM);
        }else if(vezComissao == 4){

            reuniao.setComissaoReuniao(Comissao.DTI);
        }else if(vezComissao == 5){
            reuniao.setComissaoReuniao(Comissao.CGPN);

        }else if(vezComissao == 6){
            reuniao.setComissaoReuniao(Comissao.CWBS);
            vezComissao = 0;

        }
        reuniao.setStatusReuniao(StatusReuniao.PROXIMO);

        List<Proposta> novasPropostas = new ArrayList<>();

        for (Proposta demanda: demandas){
            Proposta novaProposta = modelMapper.map(demanda, Proposta.class);
            novaProposta.setVersion(demanda.getVersion() + 1);
            novaProposta.setStatusDemanda(Status.DISCUSSION);
            novasPropostas.add(novaProposta);
        }
        reuniao.setPropostasReuniao(novasPropostas);
        return reuniao;
    }

    public Demanda avancarStatus(Demanda demanda, Status status){
        demanda.setStatusDemanda(status);
        demanda.setVersion(demanda.getVersion() + 1);
        return demanda;
    }

    @Override
    public void run(String... args) throws Exception {
        Departamento departamento = departamentoRepository.save(new Departamento(1, "Departamento"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        List<Usuario> usuarios =  usuarioRepository.findAll();
        if(usuarios.size() != 0){
           return;
        }
        personalizacaoRepository.save(gerarPersonalizacao());
        Usuario gt = usuarioRepository.save(new Usuario(1, "gestorTI", "gt", "gt", encoder.encode("gt"), departamento, NivelAcesso.GestorTI));
        Usuario s = usuarioRepository.save(new Usuario(2, "solicitante", "s", "s", encoder.encode("s"), departamento, NivelAcesso.Solicitante));
        Usuario gn = usuarioRepository.save(new Usuario(3, "gerente de negócio", "gn", "gn", encoder.encode("gn"), departamento, NivelAcesso.GerenteNegocio));
        analista = usuarioRepository.save(  new Usuario(4, "analista", "a", "a", encoder.encode("a"), departamento, NivelAcesso.Analista));
        int contador = 0;
        List<Proposta> propostas = new ArrayList<>();
        for (int i = 1; i < 500; i++) {
            contador++;
            Demanda demanda = new Demanda();
            if(contador == 1){
                demanda = demandaRepository.save(gerarDemanda(gt, i));
            }
            if(contador == 2){
                demanda = demandaRepository.save(gerarDemanda(s, i));
            }
            if(contador == 3){
                demanda = demandaRepository.save(gerarDemanda(gn, i));
                contador = 0;
            }

            if (i < 470) {
                DemandaClassificada demandaClassificada = demandaClassificadaRepository.save(gerarDemandaClassificada(demanda));
                if (i < 450) {
                    Proposta proposta = propostaRepository.save(gerarProposta(demandaClassificada));
                    propostas.add(proposta);
                    if(i %2 == 0 && i < 300){
                        Reuniao reuniao = gerarReuniao(i, propostas);
                        reuniaoRepository.save(reuniao);
                        propostas.clear();
                    }

                    if(i < 200){
                        demandaRepository.save(avancarStatus(proposta, Status.DESIGN_AND_BUILD));

                    }
                    if(i < 190){
                        demandaRepository.save(avancarStatus(proposta, Status.SUPPORT));

                    } if(i < 170){
                        demandaRepository.save(avancarStatus(proposta, Status.DONE));

                    } if(i < 150){
                        demandaRepository.save(avancarStatus(proposta, Status.CANCELLED));

                    } if(i < 130){
                        demandaRepository.save(avancarStatus(proposta, Status.TO_DO));

                    }
                }
            }
        }
    }
}