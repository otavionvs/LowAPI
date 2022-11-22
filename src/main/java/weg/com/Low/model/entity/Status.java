package weg.com.Low.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    BackLog("BackLog"), //0
    Assessment("Assessment"), //1
    BusinessCase("Business Case"), //2
    ToDo("To Do"), //3
    DesignAndBuild("Design and Build"), //3
    Support("Support"),
    Cancelled("Cancelled"),
    Done("Done"); //3

    String status;
}
