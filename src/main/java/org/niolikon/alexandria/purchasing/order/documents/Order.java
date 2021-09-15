package org.niolikon.alexandria.purchasing.order.documents;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "orders")
public class Order {
	
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
	
	@Id
	private String id;
	
    @Indexed
	private String username;

	private List<Entry> entries;
	
	private ShippingAddress address;
	
	private Status status;

	@CreatedDate
	private LocalDateTime creationTime;
	
	@LastModifiedDate
	private LocalDateTime updateTime;
}
