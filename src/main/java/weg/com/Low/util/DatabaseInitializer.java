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
        proposta.setStatusDemanda(Status.ASSESSMENT);
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
            recurso.setPerfilDespesaRecurso(PerfilDespesa.corporativo);
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
        demandaClassificada.setAnalista(demanda.getSolicitanteDemanda());
        demandaClassificada.setSecaoDemandaClassificada(Secao.AAS);
        demandaClassificada.setVersion(1);
        demandaClassificada.setStatusDemanda(Status.BACKLOG_PROPOSTA);
        return demandaClassificada;
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
        centroCustoRepository.saveAll(listaCentroCusto);
        return demanda;
    }
    public Reuniao gerarReuniao(int codigoReuniao, List<Proposta> demandas){
        Reuniao reuniao = new Reuniao();
        reuniao.setDataReuniao(Faker.instance().date().future(3, TimeUnit.DAYS));
        reuniao.setCodigoReuniao(codigoReuniao);
        reuniao.setComissaoReuniao(Comissao.CPGCI);
        reuniao.setStatusReuniao(StatusReuniao.PROXIMO);
//        reuniao.setPropostasReuniao(demandas);
        return reuniao;
    }

    @Override
    public void run(String... args) throws Exception {
        Departamento departamento = departamentoRepository.save(new Departamento(1, "Departamento"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        List<Usuario> usuarios =  usuarioRepository.findAll();

        if(usuarios.size() != 0){
           return;
        }

        Usuario usuario = usuarioRepository.save(new Usuario(1, "nomeUsuario", "gt", "gt", encoder.encode("gt"), departamento, NivelAcesso.GestorTI));
        Usuario usuario2 = usuarioRepository.save(new Usuario(2, "nomeUsuario", "gt2", "gt2@", encoder.encode("gt2"), departamento, NivelAcesso.GestorTI));

        List<Proposta> propostas = new ArrayList<>();
        for (int i = 1; i < 70; i++) {
            Demanda demanda = demandaRepository.save(gerarDemanda(usuario, i));
            if (i < 20) {
                DemandaClassificada demandaClassificada = demandaClassificadaRepository.save(gerarDemandaClassificada(demanda));
                if (i < 11) {
                    Proposta proposta = propostaRepository.save(gerarProposta(demandaClassificada));
                    propostas.add(proposta);
                    if(i %2 == 0){
                        Reuniao reuniao = gerarReuniao(i, propostas);
                        reuniaoRepository.save(reuniao);
                        propostas.clear();
                    }
                }
            }
        }
    }
}