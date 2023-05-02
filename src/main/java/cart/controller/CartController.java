package cart.controller;

import cart.controller.dto.ProductResponse;
import cart.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CartController {

    private final ProductService productService;

    public CartController(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String findAllProducts(final Model model) {
        List<ProductResponse> productResponses = productService.findAll();
        model.addAttribute("products", productResponses);
        return "index";
    }
}
