package weg.com.Low.util;
import com.github.javafaker.Faker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import weg.com.Low.model.entity.*;
import weg.com.Low.model.enums.*;
import weg.com.Low.repository.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

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

    @Override
    public void run(String... args) throws Exception {
        // Insere alguns dados no banco de dados
//        Departamento departamento = departamentoRepository.save(new Departamento(1, "Departamento"));
//
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        Usuario usuario = usuarioRepository.save(new Usuario(1,"nomeUsuario", "gt", "gt", encoder.encode("gt"), departamento, NivelAcesso.GestorTI));
//        Usuario usuario2 = usuarioRepository.save(new Usuario(2,"nomeUsuario", "gt2", "gt2@", encoder.encode("gt2"), departamento, NivelAcesso.GestorTI));
//
//        List<Demanda> listaDemandas = new ArrayList<>();
//        for(int i = 0; i < 70; i++){
//            listaDemandas.add(gerarDemanda(usuario, i));
//        }
//        listaDemandas = demandaRepository.saveAll(listaDemandas);

//        List<DemandaClassificada> listaDemandasClassificadas = new ArrayList<>();
//        for(int i = 0; i < 20; i++){
//            listaDemandasClassificadas.add(demandaClassificadaRepository.save(gerarDemandaClassificada(listaDemandas.get(i))));
//        }
//
//        demandaClassificadaRepository.saveAll(listaDemandasClassificadas);

//        List<Proposta> listaPropostas = new ArrayList<>();
//        for(int i = 0; i < 10; i++){
//            listaPropostas.add(gerarProposta(listaDemandasClassificadas.get(i)));
//        }
//        propostaRepository.saveAll(listaPropostas);
    }

    public CentroCusto gerarCentroCusto(){
        CentroCusto centroCusto = new CentroCusto();
        centroCusto.setNomeCentroCusto(Faker.instance().name().bloodGroup());
        centroCusto.setPorcentagemCentroCusto(100);
        return  centroCusto;
    }

    public Proposta gerarProposta(DemandaClassificada demanda){
        Proposta proposta = new Proposta();
        BeanUtils.copyProperties(demanda, proposta);
        proposta.setPrazoProposta(Faker.instance().date().birthday());
        proposta.setCodigoPPMProposta(12345);
        proposta.setJiraProposta(Faker.instance().lorem().characters(30));
        proposta.setInicioExDemandaProposta(Faker.instance().date().birthday());
        proposta.setFimExDemandaProposta(Faker.instance().date().birthday());
        proposta.setPaybackProposta(Faker.instance().number().randomDouble(2,1, 50000));
        proposta.setEscopoDemandaProposta(Faker.instance().leagueOfLegends().summonerSpell());
        proposta.setVersion(demanda.getVersion() + 1);
        proposta.setStatusDemanda(Status.ASSESSMENT);
        return proposta;
    }

    public DemandaClassificada gerarDemandaClassificada(Demanda demanda){
        DemandaClassificada demandaClassificada = new DemandaClassificada();
        demandaClassificada.setTamanhoDemandaClassificada(TamanhoDemanda.Grande);
        demandaClassificada.setBuSolicitanteDemandaClassificada(BussinessUnit.WAU);
        demandaClassificada.setBusBeneficiadasDemandaClassificada(List.of(BussinessUnit.WAU));
        demandaClassificada.setAnalista(demanda.getSolicitanteDemanda());
        demandaClassificada.setSecaoDemandaClassificada(Secao.AAS);
        BeanUtils.copyProperties(demanda, demandaClassificada);
        demandaClassificada.setVersion(1);
        demandaClassificada.setStatusDemanda(Status.BACKLOG_PROPOSTA);
        return demandaClassificada;
    }

    public Demanda gerarDemanda(Usuario usuario, Integer codigoDemanda){
        Demanda demanda = new Demanda();
        demanda.setCodigoDemanda(codigoDemanda);
        demanda.setVersion(0);
        demanda.setStatusDemanda(Status.BACKLOG_CLASSIFICACAO);
        demanda.setTituloDemanda(Faker.instance().name().title());
        demanda.setSituacaoAtualDemanda(Faker.instance().lorem().sentence());
        demanda.setObjetivoDemanda(Faker.instance().lorem().sentence());
        demanda.setFrequenciaDeUsoDemanda("MuitoAlta");
        demanda.setSolicitanteDemanda(usuario);
        List<CentroCusto> listaCentroCusto = new ArrayList<>();
        listaCentroCusto.add(gerarCentroCusto());
        centroCustoRepository.saveAll(listaCentroCusto);
        return demanda;
    }
}