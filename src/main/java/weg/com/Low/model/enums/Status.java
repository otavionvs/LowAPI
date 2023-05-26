package weg.com.Low.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    BACKLOG_CLASSIFICACAO("BackLog - Classificação"), //0
    BACKLOG_APROVACAO("BackLog - Aprovação"), //1
    BACKLOG_PROPOSTA("BackLog - Proposta"), //2
    ASSESSMENT("Assessment"), //3
    BUSINESS_CASE("Business Case"), //4
    DISCUSSION("Discussion"), //5
    TO_DO("To Do"), //6
    DESIGN_AND_BUILD("Design and Build"), //7
    SUPPORT("Support"), //8
    CANCELLED("Cancelled"), //9
    DONE("Done"), //10
    DRAFT("Draft"), //11
    RETURNED("Returned"); //12

    String status;
}
