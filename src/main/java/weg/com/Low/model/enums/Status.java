package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    DRAFT("Draft"), //0
    BACKLOG_CLASSIFICACAO("BackLog - Classificação"), //1
    BACKLOG_APROVACAO("BackLog - Aprovação"), //2
    BACKLOG_PROPOSTA("BackLog - Proposta"), //3
    BUSINESS_CASE("Business Case"), //4
    ASSESSMENT("Assessment"), //5
    DISCUSSION("Discussion"), //6
    TO_DO("To Do"), //7
    DESIGN_AND_BUILD("Design and Build"), //8
    SUPPORT("Support"), //9
    CANCELLED("Cancelled"), //10
    DONE("Done"), //11
    RETURNED("Returned"); //12

    String status;
}
