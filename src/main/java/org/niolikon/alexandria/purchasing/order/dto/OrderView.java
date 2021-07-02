package org.niolikon.alexandria.purchasing.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderView {
	
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
	
	private String id;
	
    private String username;

	private List<Entry> entries;
	
	private ShippingAddress address;
	
	private Status status;
	
	private LocalDateTime creationTime;
	
	private LocalDateTime updateTime;
}
