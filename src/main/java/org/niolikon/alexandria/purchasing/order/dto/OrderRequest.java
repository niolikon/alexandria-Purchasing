package org.niolikon.alexandria.purchasing.order.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderRequest {

	@Data
	public static class Entry {
		private int productId;
		private int quantity;
	}
	
	@Data
	public static class ShippingAddress {
		private String name;
		private String surname;
		
		private String address;

		private String city;
		private String state;
		private String ZIP;
		
		private String email;
		private String telephone;	
	}
	
	public static enum Status { NEW, PACKING, SHIPPED };

	private List<Entry> entries;
	
	private ShippingAddress address;
	
	private Status status;
}
