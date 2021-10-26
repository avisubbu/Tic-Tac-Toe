package com.rge.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GamePlayersHash {
    private String player1;
    private String player2;
    private String hash;

    public String generateJson(String status){
        return "{\"player1\":\""+player1+"\",\"player2\":\""+player2+"\",\"gameId\":\""+hash+"\",\"status\":\""+status+"\"}";
    }
}