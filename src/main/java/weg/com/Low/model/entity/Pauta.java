package weg.com.Low.model.entity;

import java.util.Date;
import java.util.List;

public class Pauta {
    int idPauta;
    Date reunicaoComissao;
    String comissaoSelecionada;
    List<DemandaAnalista> propostas;
}
