package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    backLogClassificacao("BackLog - Classificação"), //0
    backLogAprovacao("BackLog - Aprovação"), //1
    backLogProposta("BackLog - Proposta"), //2
    Assessment("Assessment"), //3
    BusinessCase("Business Case"), //4
    ToDo("To Do"), //5
    DesignAndBuild("Design and Build"), //6
    Support("Support"),//7
    Cancelled("Cancelled"),//8
    Done("Done"),//9
    Draft("Draft");//10

    String status;
}
