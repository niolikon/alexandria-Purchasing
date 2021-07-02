package org.niolikon.alexandria.purchasing.order;

import org.niolikon.alexandria.purchasing.order.documents.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

}
