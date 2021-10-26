package com.rge.tictactoe.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameModel {
    private String id;
    private String gameType;
    private String player1;
    private String player2;
    private String gameDate;
    private Set<Step> steps;
}
