package net.luckyfly.keycloak;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class ProductController {

	@Autowired RemoteProductService productService;

  @GetMapping(path = "/products")
  public String getProducts(Model model){
     model.addAttribute("products", productService.getProducts());
     return "product";
  }
  
  @GetMapping(path = "/error")
  public String error(Model model){
     return "error";
  }

  @GetMapping(path = "/logout")
  public String logout(HttpServletRequest request) throws ServletException {
     request.logout();
     return "/";
  }
}