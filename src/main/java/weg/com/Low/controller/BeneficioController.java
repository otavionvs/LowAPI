package weg.com.Low.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import weg.com.Low.model.service.BeneficioService;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/beneficio")
public class BeneficioController {
    private BeneficioService beneficioService;
}
