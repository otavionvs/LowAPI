//package weg.com.Low.model.service;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import weg.com.Low.model.entity.Demanda;
//import weg.com.Low.model.entity.DemandaHistorico;
//import weg.com.Low.repository.DemandaHistoricoRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//public class DemandaHistoricoService {
//    private DemandaHistoricoRepository demandaHistoricoRepository;
//
//    public List<DemandaHistorico> findAll() {
//        return demandaHistoricoRepository.findAll();
//    }
//
//    public DemandaHistorico save(DemandaHistorico demandaHistorico) {
//        return demandaHistoricoRepository.save(demandaHistorico);
//    }
//
//    public Optional<DemandaHistorico> findById(Integer codigo) {
//        return demandaHistoricoRepository.findById(codigo);
//    }
//
//    public boolean existsById(Integer codigo) {
//        return demandaHistoricoRepository.existsById(codigo);
//    }
//
//    public void deleteById(Integer codigo) {
//        demandaHistoricoRepository.deleteById(codigo);
//    }
//
//    public List<DemandaHistorico> findByDemandaDemandaHistorico(Demanda demanda){
//        return demandaHistoricoRepository.findByDemandaDemandaHistorico(demanda);
//    }
//}
