package com.wrl.xwlb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StaticController {

  @RequestMapping("/newsKeyWords")
  public String newsKeyWords(@RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate") String endDate) {
    return "/pages/news_key_words.html";
  }

  @RequestMapping("/newsText")
  public String newsText(@RequestParam(name = "word") String word,
                         @RequestParam(name = "startDate") String startDate,
                         @RequestParam(name = "endDate") String endDate) {
    return "/pages/news_text.html";
  }
}
