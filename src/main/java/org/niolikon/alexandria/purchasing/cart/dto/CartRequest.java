package org.niolikon.alexandria.purchasing.cart.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CartRequest {
	
	@Data
	public static class EntryRequest {
		private int productId;
		private int quantity;
	}

	private List<EntryRequest> entries;
	
}
