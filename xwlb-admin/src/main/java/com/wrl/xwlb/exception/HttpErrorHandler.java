package com.wrl.xwlb.exception;


import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
//@RestController
public class HttpErrorHandler implements ErrorController {

  private static final String DEFAULT_ERROR_PATH = "/error";
  private static final String DEFAULT_ERROR_PAGE = "/pages/404_page.html";
  private static final String DEFAULT_ERROR_MSG = "404 not found.";

  @RequestMapping(value = DEFAULT_ERROR_PATH)
  public String error(HttpServletRequest request) {
    return DEFAULT_ERROR_PAGE;
//    return DEFAULT_ERROR_MSG;
  }

  @Override
  public String getErrorPath() {
    return DEFAULT_ERROR_PATH;
  }
}
