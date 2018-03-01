package net.luckyfly.keycloak;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
class ProductController {

  @Autowired ProductService productService;

  @GetMapping(path = "/products")
  public ModelAndView getProducts(){
	  ModelAndView mv = new ModelAndView("product");
	  mv.addObject("products", productService.getProducts());
     return mv;
  }
  
  @GetMapping(path = "/products-list")
  public @ResponseBody List<String> getProductsList(Model model){
	  return productService.getProducts();
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