package com.rge.tictactoe.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rge.tictactoe.model.GameModel;
import com.rge.tictactoe.model.Move;
import com.rge.tictactoe.model.Step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Game Utility Methods
 */
public class GameUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameUtils.class);

    /**
     * 
     * Utility method to prepare game metadata
     * 
     * @param gameType
     * @param player1
     * @param player2
     * @param ds
     * @return
     */
    public static String prepareGame(String gameType, String player1, String player2, String ds){
        Date d = new Date();
        String hash;
        if(gameType.equals("single")){
            hash = DigestUtils.sha256Hex(player1+d.getTime());
        } else{
            hash = DigestUtils.sha256Hex(player1+player2+d.getTime());
        }

        String[][] board = {{"_","_","_"},{"_","_","_"},{"_","_","_"}};
        ObjectMapper objMapper = new ObjectMapper();
        GameModel game = new GameModel(hash, gameType, player1, player2,
                d.getDate()+"-"+d.getMonth()+"-"+d.getYear(), new TreeSet<Step>());
        
        try{
            File fGame = new File(ds+File.separator+"games"+File.separator+hash);
            if(!fGame.exists()){
                File fDir = new File(ds+File.separator+"games");
                if (!fDir.exists()){
                    fDir.mkdirs();
                }
                fGame.createNewFile();
            }

            File fHash = new File(ds+File.separator+"game"+File.separator+"data"+File.separator+hash);
            if(!fHash.exists()){
                File fDir = new File(ds+File.separator+"game"+File.separator+"data");
                if (!fDir.exists()){
                    fDir.mkdirs();
                }
                fHash.createNewFile();
            }

            objMapper.writeValue(fGame, board);
            objMapper.writeValue(fHash, game);
        } catch(JsonGenerationException e){
            LOGGER.error("Exception occurred:",e);
        } catch(JsonMappingException e){
            LOGGER.error("Exception occurred:",e);
        } catch(IOException e){
            LOGGER.error("Exception occurred:",e);
        }
        return hash;
    }

    /**
     * 
     * Find the best move possible for a given board
     * 
     * @param brd
     * @param player
     * @param opponent
     * @return
     */
    public static Move findBestMove(String[][] brd, String player, String opponent){
        int bestValue = -1000;
        Move bestMove = new Move();
        bestMove.row = -1;
        bestMove.col = -1;

        // Loop through all the cells and generate the best move
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(brd[i][j].equals("_")){
                    brd[i][j] = player;
                    int moveValue = minimax(brd,false, player, opponent);
                    brd[i][j] = "_";
                    if(moveValue > bestValue){
                        bestMove.row = i;
                        bestMove.col = j;
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }

    private static int minimax(String[][] brd, Boolean isMax, String player, String opponent){
        String winner = winner(brd);

        if(winner != null && player.equals(winner)){
            return +10;            
        }else if (winner != null && opponent.equals(winner)){
            return -10;
        }

        if(anyMovesLeft(brd)){
            return 0;
        }

        if(isMax){
            int best = -1000;
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(brd[i][j].equals("_")){
                        brd[i][j]=player;
                        best = Math.max(best, minimax(brd, !isMax, player, opponent));
                        brd[i][j]="_";
                    }
                }
            }
            return best;
        }else{
            int best = 1000;
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(brd[i][j].equals("_")){
                        brd[i][j]=opponent;
                        best = Math.min(best, minimax(brd, !isMax, player, opponent));
                        brd[i][j]="_";
                    }
                }
            }
            return best;
        }
    }

    /**
     * 
     * Check if any moves are left in the game.
     * 
     * @param brd
     * @return
     */
    public static Boolean anyMovesLeft(String[][] brd){
        return Arrays.asList(brd).stream().anyMatch(arr -> Arrays.asList(arr).contains("_"));
    }

    /**
     * 
     * Find the winner of the board.
     * 
     * @param brd
     * @return
     */
    public static String winner(String[][] brd){
        int j=0,i=0;
        while(i<3){
            if(brd[i][j].equals(brd[i][j+1]) && brd[i][j+1].equals(brd[i][j+2])
                && !brd[i][j].equals("_")){
                return brd[i][j];
            }else if(brd[j][i].equals(brd[j+1][i]) && brd[j+1][i].equals(brd[j+2][i])
                && !brd[j][i].equals("_")){
                return brd[j][i];
            }
            i++;
        }
        i = 0;
        if((brd[i][j].equals(brd[i+1][j+1]) && brd[i+1][j+1].equals(brd[i+2][j+2])
            && !brd[i][j].equals("_"))
            ||(brd[i+2][j].equals(brd[i+1][j+1]) && brd[i+1][j+1].equals(brd[i][j+2])
                && !brd[i+1][j+1].equals("_"))){
                return brd[i+1][j+1];
        }
        return null;
    }
}
