package weg.com.Low.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.javafaker.Faker;
import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import weg.com.Low.dto.CentroCustoDTO;
import weg.com.Low.dto.UsuarioDTO;
import weg.com.Low.model.entity.CentroCusto;
import weg.com.Low.model.entity.Demanda;
import weg.com.Low.model.entity.Departamento;
import weg.com.Low.model.entity.Usuario;
import weg.com.Low.model.enums.NivelAcesso;
import weg.com.Low.model.enums.Status;
import weg.com.Low.repository.CentroCustoRepository;
import weg.com.Low.repository.DemandaRepository;
import weg.com.Low.repository.DepartamentoRepository;
import weg.com.Low.repository.UsuarioRepository;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Override
    public void run(String... args) throws Exception {
        // Insere alguns dados no banco de dados
        Departamento departamento = departamentoRepository.save(new Departamento(1, "Departamento"));
        Usuario usuario = usuarioRepository.save(new Usuario(1,"nomeUsuario", "userUsuario", "emailUsuario", "$2a$10$o5zNvDXtP8UCBRN8fZcHc.6.2Kan67ucvrqxINLrI.9sLVSYzTH6a", departamento, NivelAcesso.GestorTI));

        List<Demanda> listaDemandas = new ArrayList<>();
        for(int i = 0; i < 70; i++){
            listaDemandas.add(gerarDemanda(usuario, i));
        }
        demandaRepository.saveAll(listaDemandas);
    }

    public CentroCusto gerarCentroCusto(){
        CentroCusto centroCusto = new CentroCusto();
        centroCusto.setNomeCentroCusto(Faker.instance().name().bloodGroup());
        centroCusto.setPorcentagemCentroCusto(100);
        return  centroCusto;
    }


//    gerarDemandaClassificada(Demanda demanda){
//
//    }

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