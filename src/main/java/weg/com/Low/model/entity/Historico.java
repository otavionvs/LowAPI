package weg.com.Low.model.entity;

import java.util.Date;
import java.util.List;

public class Historico {
    int idHistorico;
    Usuario pessoaResponsavel;
    Date dataAlteracao;
    List<Demanda> demandasHistorico;
}
