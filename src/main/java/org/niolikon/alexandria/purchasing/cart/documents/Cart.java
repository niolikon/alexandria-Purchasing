package org.niolikon.alexandria.purchasing.cart.documents;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "carts")
public class Cart {
	
	@Data
	public static class Entry {
		private int productId;
		private int quantity;
	}
	
	@Id
	private String id;
	
    @Indexed(unique=true)
	private String username;

	private List<Entry> entries;

	@CreatedDate
	private LocalDateTime creationTime;
	
	@LastModifiedDate
    @Indexed(expireAfterSeconds=3600)
	private LocalDateTime updateTime;
}
