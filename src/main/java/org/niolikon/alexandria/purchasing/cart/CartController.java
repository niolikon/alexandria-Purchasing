package org.niolikon.alexandria.purchasing.cart;

import java.security.Principal;

import javax.validation.Valid;

import org.niolikon.alexandria.purchasing.cart.documents.Cart;
import org.niolikon.alexandria.purchasing.cart.dto.CartRequest;
import org.niolikon.alexandria.purchasing.cart.dto.CartView;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/cart")
@Api(tags="Management of Cart documents")
public class CartController {
	
	private final CartService service;
	
	public CartController(CartService service) {
		this.service = service;
	}
	
	@GetMapping
	@ApiOperation(
            value = "Fetch user cart", notes = "Returns user's Cart data in JSON", response = CartView.class, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Cart has been fetched"),
            @ApiResponse(code = 401, message = "You are not logged in") })
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:3100", "http://localhost:3200"})
    public CartView getCart(Principal user) {
		return service.getCart(user.getName());
	}
	
	@PutMapping
	@ApiOperation(
            value = "Update user cart", notes = "Modifies user's Cart data with the input JSON Cart data", response = CartView.class, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Cart has been modified"),
            @ApiResponse(code = 401, message = "You are not logged in") })   
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:3100", "http://localhost:3200"})
	public CartView updateCart(@RequestBody @Valid CartRequest req,
			Principal user) {
		Cart cart = service.findCartOrNew(user.getName());
		return service.update(cart, req);
	}
}
