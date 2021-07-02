package org.niolikon.alexandria.purchasing.cart;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.niolikon.alexandria.purchasing.cart.converter.CartToCartViewConverter;
import org.niolikon.alexandria.purchasing.cart.documents.Cart;
import org.niolikon.alexandria.purchasing.cart.dto.CartRequest;
import org.niolikon.alexandria.purchasing.cart.dto.CartView;
import org.niolikon.alexandria.purchasing.system.MessageProvider;
import org.springframework.stereotype.Service;

import com.sun.jdi.InternalException;

@Service
public class CartService {
	
	private final CartRepository cartRepo;
	private final CartToCartViewConverter cartConverter;
	private final MessageProvider messageProvider;
	
	public CartService(CartRepository cartRepo,
			CartToCartViewConverter cartConverter,
			MessageProvider messageProvider) {
		this.cartRepo = cartRepo;
		this.cartConverter = cartConverter;
		this.messageProvider = messageProvider;
	}
	
	public Cart findCartOrNew(String username) {
		return cartRepo.findByUsername(username).orElseGet( () -> {
			Cart userCart = new Cart();
			userCart.setUsername(username);
			userCart.setEntries(new ArrayList<Cart.Entry>());
			cartRepo.save(userCart);
			
			return userCart;
		});
	}
	
	public CartView getCart(String username) {
		Cart cart = findCartOrNew(username);
		return cartConverter.convert(cart);
	}
	
    public void delete(String id) {
        try {
            cartRepo.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new InternalException(messageProvider.getMessage("cart.NotFound", id));
        }
    }
	
	public CartView update(Cart cart, CartRequest req) {
		Cart cartUpdated = this.fetchFromRequest(cart, req);
		Cart cartSaved = cartRepo.save(cartUpdated);
		return cartConverter.convert(cartSaved);
	}
	
	private Cart fetchFromRequest(Cart cart, CartRequest req) {
		cart.setEntries( req.getEntries().stream().map((CartRequest.EntryRequest entryReq) -> {
			Cart.Entry entry = new Cart.Entry();
			entry.setProductId(entryReq.getProductId());
			entry.setQuantity(entryReq.getQuantity());

			return entry;
		}).collect(Collectors.toList()));
		
		return cart;
	}
}
