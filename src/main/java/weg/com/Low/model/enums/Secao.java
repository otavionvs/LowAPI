package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Secao {
    SVE("Seção Desenvolvimento Sistemas de Vendas e E-commerce"), //0
    SIM("Seção Desenvolvimento Sistemas de manufatura"), //1
    SIE("Seção Desenvolvimento Sistemas de Engenhar"), //2
    SDO("Setor Desenvolvimento Plataforma Orchestra"), //3
    SCO("Seção Desenvolvimento Sistemas Corporativos"), //4
    PTI("Seção Projetos de TI"), //5
    AGD("Seção Arquitetura e Governança de Dados"), //6
    STD("Seção Desenvolvimento Tecnologias Digitais"),//7
    TIN("Seção Tecnologia de Infraestrutura"),//8
    SGI("Seção Suporte Global Infraestrutura"),//9
    SEG("Seção Segurança da Informação e Riscos TI"),//10
    AAS("Atendimento e serviços TI – América do Sul");//11

    String secao;
}

