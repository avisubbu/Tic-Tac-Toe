package com.rge.tictactoe.handler;

import com.rge.tictactoe.model.GamePlayersHash;
import com.rge.tictactoe.utils.GameUtils;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.rge.tictactoe.config.AppProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Socket handler to handle communication for multiplayer games
 */
@Component
public class GameRoomHandler extends TextWebSocketHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRoomHandler.class);

    @Autowired
    private AppProperties appProperties;

    private static Queue<WebSocketSession> queue = new ConcurrentLinkedQueue<WebSocketSession>();

    private static Hashtable<String, GamePlayersHash> playersHash = new Hashtable<>();

    private static Hashtable<String, WebSocketSession> sessionMap = new Hashtable<>();

    /**
     * 
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        queue.add(session);
    }

    /**
     * 
     * @param session
     * @param message
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException{
        WebSocketSession player2;
        String hash;
        String payload = message.getPayload();
        // When playeres enter game room for the first time
        if("ESTABLISH_CONNECTION".equals(payload)){
            synchronized(queue){
                if(queue.size() > 1){
                    player2 = queue.poll();
                    queue.remove(session);
                    hash = GameUtils.prepareGame("multiplayer", session.getId(), player2.getId(),
                            appProperties.getDataStore());
                    GamePlayersHash playerHash = new GamePlayersHash(session.getId(), player2.getId(), hash);
                    playersHash.put(session.getId(), playerHash);
                    playersHash.put(player2.getId(), playerHash);
                    sessionMap.put(session.getId(), session);
                    sessionMap.put(player2.getId(), player2);
                    session.sendMessage(new TextMessage(playerHash.generateJson("START_GAME")));
                    player2.sendMessage(new TextMessage(playerHash.generateJson("START_GAME_NOT_YOUR_TURN")));
                }
            }
        }
        // Players have made a move
        else if("MOVE".equals(payload)){
            GamePlayersHash playerHash = playersHash.get(session.getId());
            if(playerHash.getPlayer1().equals(session.getId())){
                WebSocketSession socketSession = sessionMap.get(playerHash.getPlayer2());
                socketSession.sendMessage(new TextMessage(playerHash.generateJson("YOUR_TURN")));
            }else{
                WebSocketSession socketSession = sessionMap.get(playerHash.getPlayer1());
                socketSession.sendMessage(new TextMessage(playerHash.generateJson("YOUR_TURN")));
            }
        }
    }
}
