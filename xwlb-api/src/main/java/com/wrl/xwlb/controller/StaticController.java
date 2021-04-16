package com.wrl.xwlb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StaticController {

  @RequestMapping("/newsKeywords")
  public String newsKeyWords(@RequestParam(required = false) String date,
                             @RequestParam(required = false) String startDate,
                             @RequestParam(required = false) String endDate) {
    return "/pages/news_key_words.html";
  }

  @RequestMapping("/newsText")
  public String newsText(@RequestParam String word,
                         @RequestParam(required = false) String date,
                         @RequestParam(required = false) String startDate,
                         @RequestParam(required = false) String endDate) {
    return "/pages/news_text.html";
  }
}
