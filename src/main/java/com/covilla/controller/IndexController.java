package com.covilla.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/")
	public String index(){
		return "site/index";
	}
	@RequestMapping("/about")
	public String about(){
		return "site/about";
	}
	@RequestMapping("/product")
	public String product(){
		return "site/product";
	}
	@RequestMapping("/contact")
	public String contact(){
		return "site/contact";
	}
	@RequestMapping("/403")
	public String error403(){
		return "403";
	}
	@RequestMapping("/404")
	public String error404(){
		return "404";
	}
	@RequestMapping("/500")
	public String error500(){
		return "500";
	}

	@RequestMapping("test")
	public String testPage(){
		return "bk/test";
	}
}
