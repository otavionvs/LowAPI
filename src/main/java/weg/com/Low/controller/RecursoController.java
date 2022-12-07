package weg.com.Low.controller;

import weg.com.Low.model.entity.Recurso;
import weg.com.Low.model.service.RecursoService;

import java.util.List;

public class RecursoController {
    RecursoService recursoService;

    public List<Recurso> findAll() {
        return recursoService.findAll();
    }

    public  Recurso save(S entity) {
        return recursoService.save(entity);
    }

    public void deleteById(Integer integer) {
        recursoService.deleteById(integer);
    }
}
