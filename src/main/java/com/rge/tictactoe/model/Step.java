package com.rge.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Step implements Comparable<Step>{

    private static volatile int counter=0;

    public Step(String player, String step, Long timestamp){
        counter+=1;
        this.sno=counter;
        this.player = player;
        this.step = step;
        this.timestamp = timestamp;
    }

    private Integer sno;
    private String player;
    private String step;
    private Long timestamp;

    @Override
    public int compareTo(Step o) {
        return this.sno.compareTo(o.getSno());
    }
}
