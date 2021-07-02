package org.niolikon.alexandria.purchasing.cart;

import java.util.Optional;

import org.niolikon.alexandria.purchasing.cart.documents.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
	
	public Optional<Cart> findByUsername(String username);

}
