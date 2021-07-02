package org.niolikon.alexandria.purchasing.cart.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CartView {
	
	@Data
	public static class EntryView {
		private int productId;
		private int quantity;
	}

	private List<EntryView> entries;
	
	private LocalDateTime creationTime;
	
	private LocalDateTime updateTime;
	
}
