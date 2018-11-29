package com.sample.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorsController {

	
	@RequestMapping("/errors")
	@ResponseBody
	public String home(@RequestParam(value = "error") String error){
		
		return "<p>Errore numero: "+ error +"</p>";	
	}
}
