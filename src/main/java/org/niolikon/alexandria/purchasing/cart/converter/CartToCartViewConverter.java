package org.niolikon.alexandria.purchasing.cart.converter;

import java.util.stream.Collectors;

import org.niolikon.alexandria.purchasing.cart.documents.Cart;
import org.niolikon.alexandria.purchasing.cart.dto.CartView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CartToCartViewConverter implements Converter<Cart, CartView> {
    
    @Override
    public CartView convert(Cart source) {
    	
        CartView view = new CartView();
		view.setEntries(source.getEntries().stream().map((Cart.Entry entry) -> {
			CartView.EntryView entryView = new CartView.EntryView();
			entryView.setProductId(entry.getProductId());
			entryView.setQuantity(entry.getQuantity());

			return entryView;

		}).collect(Collectors.toList()));
		
		view.setCreationTime(source.getCreationTime());
		view.setUpdateTime(source.getUpdateTime());
        
        return view;
    }
    
}
