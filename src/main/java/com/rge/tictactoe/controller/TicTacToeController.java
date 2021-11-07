package com.rge.tictactoe.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rge.tictactoe.config.AppProperties;
import com.rge.tictactoe.model.GameModel;
import com.rge.tictactoe.model.Move;
import com.rge.tictactoe.model.Step;
import com.rge.tictactoe.utils.GameUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for all the ajax calls
 */
@RestController
public class TicTacToeController{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TicTacToeController.class);

    @Autowired
    private AppProperties appProperties;

    /**
     * 
     * Method to register the move by the player.
     * 
     * @param dataMp
     * @return
     */
    @RequestMapping(value="/move", method=RequestMethod.POST)
    public String[][] registerMove(@RequestBody Map<String, String> dataMp){
        ObjectMapper objMapper = new ObjectMapper();
        try{
            String symbol = "O", gameId = dataMp.get("gameId");
            int row = Integer.parseInt(dataMp.get("row")),
                column = Integer.parseInt(dataMp.get("col"));
            File boardFile = Paths.get(appProperties.getDataStore()+File.separator+"games"+File.separator+gameId).toFile(),
                gameFile = Paths.get(appProperties.getDataStore()+File.separator+"game"+File.separator+"data"+File.separator+gameId).toFile();
            String[][] board = objMapper.readValue(boardFile, String[][].class);
            Date d = new Date();
            GameModel game = objMapper.readValue(gameFile, GameModel.class);
            
            if(game.getPlayer1().equals(dataMp.get("player"))){
                symbol = "X";
            }
            board[row][column] = symbol;

            Step step = new Step(dataMp.get("player"), row+""+column, d.getTime());
            game.getSteps().add(step);

            /**
             * Computer's move is triggered, applicable for single player
             */
            if(game.getGameType().equals("single") && GameUtils.anyMovesLeft(board) 
                && GameUtils.winner(board)==null){
                Move move = GameUtils.findBestMove(board, "O", symbol);
                Date dt = new Date();
                step = new Step("Computer", move.row+""+move.col, dt.getTime());
                game.getSteps().add(step);
                board[move.row][move.col] = "O";
            }

            objMapper.writeValue(boardFile, board);
            objMapper.writeValue(gameFile, game);

            return board;
        } catch(JsonGenerationException e){
            LOGGER.error("Exception occurred:",e);
        } catch(JsonMappingException e){
            LOGGER.error("Exception occurred:",e);
        } catch(IOException e){
            LOGGER.error("Exception occurred:",e);
        }
        return null;
    }

    /**
     * 
     * Display Game Panel for single player.
     * 
     * @param name
     * @return
     */
    @RequestMapping("/game/single/{name}")
    public String enterRoom(@PathVariable String name){
        String gameType="single";
        return GameUtils.prepareGame(gameType, name, null, appProperties.getDataStore());
    }

    /**
     * 
     * Return the board
     * 
     * @param gameId
     * @return
     */
    @RequestMapping("/board/{gameId}")
    public String[][] board(@PathVariable String gameId){
        ObjectMapper objMapper = new ObjectMapper();
        String[][] board;
        try{
            board = objMapper.readValue(
                Paths.get(appProperties.getDataStore()+File.separator+"games"+File.separator+gameId).toFile(), 
                String[][].class);
            return board;
        } catch(JsonGenerationException e){
            LOGGER.error("Exception occurred:",e);
        } catch(JsonMappingException e){
            LOGGER.error("Exception occurred:",e);
        } catch(IOException e){
            LOGGER.error("Exception occurred:",e);
        }
        return null;
    }

    /**
     * 
     * Get the moves of a game
     * 
     * @param gameId
     * @return
     */
    @RequestMapping("/step/{gameId}")
    public Set<Step> steps(@PathVariable String gameId){
        ObjectMapper objMapper = new ObjectMapper();

        GameModel game = null;
        try{
            game = objMapper.readValue(
                Paths.get(appProperties.getDataStore()+File.separator+"game"+File.separator+"data"+File.separator+gameId).toFile(), 
                GameModel.class);
        } catch(JsonGenerationException e){
            LOGGER.error("Exception occurred:",e);
        } catch(JsonMappingException e){
            LOGGER.error("Exception occurred:",e);
        } catch(IOException e){
            LOGGER.error("Exception occurred:",e);
        }
        return game.getSteps();
    }

    /**
     * 
     * Get Result of a game
     * 
     * @param gameId
     * @param player
     * @return
     */
    @RequestMapping("/result/{gameId}/{player}")
    public String result(@PathVariable String gameId, @PathVariable String player){
        ObjectMapper objMapper = new ObjectMapper();
        String winner = null;
        String[][] board = null;
        GameModel game = null;
        try{
            board = objMapper.readValue(
                Paths.get(appProperties.getDataStore()+File.separator+"games"+File.separator+gameId).toFile(), 
                String[][].class);
            
            game = objMapper.readValue(
                Paths.get(appProperties.getDataStore()+File.separator+"game"+File.separator+"data"+File.separator+gameId).toFile(), 
                GameModel.class);

            winner = GameUtils.winner(board);
            System.out.println("winner:::"+winner);
        } catch(JsonGenerationException e){
            LOGGER.error("Exception occurred:",e);
        } catch(JsonMappingException e){
            LOGGER.error("Exception occurred:",e);
        } catch(IOException e){
            LOGGER.error("Exception occurred:",e);
        }
        
        if(winner == null){
            if(board != null && !GameUtils.anyMovesLeft(board))
                return "Game is Draw..!";
            return "";
        }

        if("X".equals(winner)){
            if(game.getGameType().equals("single")){
                return "You Win...! Congratulations....!";
            }else{
                if(game.getPlayer1().equals(player)){
                    return "You Win...! Congratulations....!";
                }else{
                    return "You Opponent Win...!";
                }
            }
        }else{
            if(game.getGameType().equals("single")){
                return "Computer Win...! Better luck next time...!";
            }else{
                if(game.getPlayer1().equals(player)){
                    return "You Win...! Congratulations....!";
                }else{
                    return "You Opponent Win...!";
                }
            }
        }
    }
}
