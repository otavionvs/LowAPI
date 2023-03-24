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
    TO_DO("To Do"), //5
    DESIGN_AND_BUILD("Design and Build"), //6
    SUPPORT("Support"),//7
    CANCELLED("Cancelled"),//8
    DONE("Done"),//9
    DRAFT("Draft"),//10
    RETURNED("Returned");

    String status;
}
