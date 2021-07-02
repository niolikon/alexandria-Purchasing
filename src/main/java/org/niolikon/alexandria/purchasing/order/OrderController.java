package org.niolikon.alexandria.purchasing.order;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.niolikon.alexandria.purchasing.order.documents.Order;
import org.niolikon.alexandria.purchasing.order.dto.OrderRequest;
import org.niolikon.alexandria.purchasing.order.dto.OrderView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/orders")
@Api(tags="Management of Order documents")
public class OrderController {
	
	private final OrderService service;
	
	public OrderController(OrderService service) {
		this.service = service;
	}
	
	@GetMapping("/{id}")
	@ApiOperation(
            value = "Read order by ID", notes = "Returns Order data in JSON", response = OrderView.class, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Order has been fetched"),
            @ApiResponse(code = 401, message = "You are not logged in"),
            @ApiResponse(code = 403, message = "You are not authorized to access this resource")})
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:3100", "http://localhost:3200"})
    public ResponseEntity<OrderView> getOrder(@ApiParam("The ID of the Order") @PathVariable String id, Principal user) {
		OrderView result = service.getOrder(id);
		Set<String> userRoles = getUserRoles();
		
		if (user.getName() == result.getUsername() ) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else if (userRoles.contains("ROLE_ADMIN") || userRoles.contains("ROLE_OPERATOR")) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}
	
	@GetMapping
    @ResponseBody
    @ApiOperation(
            value = "Read all orders", notes = "Returns Order data in JSON", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Orders have been fetched"),
            @ApiResponse(code = 401, message = "You are not logged in"),
            @ApiResponse(code = 403, message = "You are not authorized to access this resource"),
            @ApiResponse(code = 404, message = "No Orders are present in the repository")})
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:3100", "http://localhost:3200"})
    public ResponseEntity<Page<OrderView>> getAllOrders(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		Page<OrderView> result = service.findAllOrders(pageable);
		Set<String> userRoles = getUserRoles();
		
		if (userRoles.contains("ROLE_ADMIN") || userRoles.contains("ROLE_OPERATOR")) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @ApiOperation(
            value = "Create an Order", notes = "Stores the input JSON Order data", response = OrderView.class, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The Order has been stored"),
            @ApiResponse(code = 401, message = "You are not logged in"),
            @ApiResponse(code = 403, message = "You are not authorized to access this resource")})
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:3100"})
    public ResponseEntity<OrderView> create(@ApiParam("The input Order data") @RequestBody @Valid OrderRequest req, Principal user) {
    	if (user == null) {
    		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    	}
    	
    	return new ResponseEntity<>(service.create(req, user.getName()), HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(
            value = "Delete an Order", notes = "Deletes the specified Order data", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The Order has been deleted"),
            @ApiResponse(code = 401, message = "You are not logged in"),
            @ApiResponse(code = 403, message = "You are not authorized to access this resource"),
            @ApiResponse(code = 404, message = "Could not find the specified Order")})
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:3100"})
    public ResponseEntity<Object> deleteOrder(@ApiParam("The ID of the Order") @PathVariable String id) {
    	Set<String> userRoles = getUserRoles();
		
		if (userRoles.contains("ROLE_ADMIN") || userRoles.contains("ROLE_OPERATOR")) {
			service.delete(id);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		
        
    }
	
    @PutMapping("/{id}")
	@ApiOperation(
            value = "Update an Order", notes = "Modifies user's Order data with the input JSON Order data", response = OrderView.class, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Order has been modified"),
            @ApiResponse(code = 401, message = "You are not logged in"),
            @ApiResponse(code = 403, message = "You are not authorized to access this resource")})   
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:3100", "http://localhost:3200"})
	public ResponseEntity<OrderView> updateOrder(@ApiParam("The ID of the Product") @PathVariable String id, 
			@RequestBody @Valid OrderRequest req) {
    	Order Order = service.findOrderOrThrow(id);
		Set<String> userRoles = getUserRoles();
		
		if (userRoles.contains("ROLE_ADMIN") || userRoles.contains("ROLE_OPERATOR")) {
			return new ResponseEntity<>(service.update(Order, req), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}
    
    private Set<String> getUserRoles() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    	Set<String> roles = authentication.getAuthorities().stream()
    	     .map(r -> r.getAuthority()).collect(Collectors.toSet());
    	
    	return roles;
    }
}
