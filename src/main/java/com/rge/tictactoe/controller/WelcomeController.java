package com.rge.tictactoe.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller
 */
@Controller
public class WelcomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WelcomeController.class);

	/**
	 * 
	 * Welcome method
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "welcome";
	}

	/**
	 * 
	 * Get Game Room screen
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/multiplayer")
	public String multiplayer(Model model) {
		String[][] panel = {{"_","_","_"},{"_","_","_"},{"_","_","_"}};
		model.addAttribute("gamepanel", panel);
		model.addAttribute("gameType", "multiplayer");
		return "gameroom";
	}
	
	@RequestMapping("/game")
	public String startgame(Map<String, Object> model) {
		return "game";
	}

	/**
	 * 
	 * Get game panel for single player
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/singleplayer")
	public String singleplayer(Model model){
		String[][] panel = {{"_","_","_"},{"_","_","_"},{"_","_","_"}};
		model.addAttribute("gamepanel", panel);
		model.addAttribute("gameType", "single");
		return "game";
	}
}