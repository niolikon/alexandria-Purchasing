package org.niolikon.alexandria.purchasing.order;

import java.util.List;
import java.util.stream.Collectors;

import org.niolikon.alexandria.purchasing.order.converter.OrderToOrderViewConverter;
import org.niolikon.alexandria.purchasing.order.documents.Order;
import org.niolikon.alexandria.purchasing.order.dto.OrderRequest;
import org.niolikon.alexandria.purchasing.order.dto.OrderView;
import org.niolikon.alexandria.purchasing.system.MessageProvider;
import org.niolikon.alexandria.purchasing.system.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jdi.InternalException;

@Service
public class OrderService {
	
	private final OrderRepository orderRepo;
	private final OrderToOrderViewConverter orderConverter;
	private final MessageProvider messageProvider;
	
	public OrderService(OrderRepository orderRepo,
			OrderToOrderViewConverter orderConverter,
			MessageProvider messageProvider) {
		this.orderRepo = orderRepo;
		this.orderConverter = orderConverter;
		this.messageProvider = messageProvider;
	}
	
	public Order findOrderOrThrow(String id) {
		return orderRepo.findById(id)
				.orElseThrow( () -> new EntityNotFoundException(messageProvider.getMessage("order.NotFound", id)));
	}
	
	public OrderView getOrder(String id) {
		Order order = findOrderOrThrow(id);
		return orderConverter.convert(order);
	}
	
	public Page<OrderView> findAllOrders(Pageable pageable) {
		Page<Order> orders = orderRepo.findAll(pageable);
		List<OrderView> orderViews = orders.stream()
				.map( order -> orderConverter.convert(order))
				.collect(Collectors.toList());
		
		return new PageImpl<>(orderViews, pageable, orders.getTotalElements());
	}
	
	public OrderView create(OrderRequest req, String username) {
		Order order = new Order();
		order.setUsername(username);
		this.fetchFromRequest(order, req);
		
		try {
			Order orderSaved = orderRepo.save(order);
			return orderConverter.convert(orderSaved);
		} catch(Exception e) {
			throw new InternalException(messageProvider.getMessage("order.CouldNotSave"));
		}
	}
	
	@Transactional
    public void delete(String id) {
        try {
            orderRepo.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException(messageProvider.getMessage("order.NotFound", id));
        }
    }
	
	public OrderView update(Order order, OrderRequest req) {
		Order orderUpdated = this.fetchFromRequest(order, req);
		Order orderSaved = orderRepo.save(orderUpdated);
		return orderConverter.convert(orderSaved);
	}
	
	private Order fetchFromRequest(Order order, OrderRequest req) {
		order.setEntries( req.getEntries().stream().map((OrderRequest.Entry entryReq) -> {
			Order.Entry entry = new Order.Entry();
			entry.setProductId(entryReq.getProductId());
			entry.setQuantity(entryReq.getQuantity());

			return entry;
		}).collect(Collectors.toList()));
		
		Order.ShippingAddress address = new Order.ShippingAddress();
		address.setName(req.getAddress().getName());
		address.setSurname(req.getAddress().getSurname());
		address.setAddress(req.getAddress().getAddress());
		address.setCity(req.getAddress().getCity());
		address.setState(req.getAddress().getState());
		address.setZIP(req.getAddress().getZIP());
		address.setEmail(req.getAddress().getEmail());
		address.setTelephone(req.getAddress().getTelephone());
		order.setAddress(address);
		
		order.setStatus(Order.Status.valueOf(req.getStatus().toString()));
		
		return order;
	}
}
