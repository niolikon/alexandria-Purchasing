package org.niolikon.alexandria.purchasing.order.converter;

import java.util.stream.Collectors;

import org.niolikon.alexandria.purchasing.order.documents.Order;
import org.niolikon.alexandria.purchasing.order.dto.OrderView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderToOrderViewConverter implements Converter<Order, OrderView>{

	@Override
	public OrderView convert(Order source) {
		
		OrderView view = new OrderView();
		
		view.setId(source.getId());
		
		view.setUsername(source.getUsername());

		view.setEntries(source.getEntries().stream().map((Order.Entry entry) -> {
			OrderView.Entry entryView = new OrderView.Entry();
			entryView.setProductId(entry.getProductId());
			entryView.setQuantity(entry.getQuantity());
			
			return entryView;
		}).collect(Collectors.toList()));
		
		OrderView.ShippingAddress addressView = new OrderView.ShippingAddress();
		addressView.setName(source.getAddress().getName());
		addressView.setSurname(source.getAddress().getSurname());
		addressView.setAddress(source.getAddress().getAddress());
		addressView.setCity(source.getAddress().getCity());
		addressView.setState(source.getAddress().getState());
		addressView.setZIP(source.getAddress().getZIP());
		addressView.setEmail(source.getAddress().getEmail());
		addressView.setTelephone(source.getAddress().getTelephone());
		view.setAddress(addressView);
		
		view.setStatus(OrderView.Status.valueOf(source.getStatus().toString()));

		view.setCreationTime(source.getCreationTime());
		
		view.setUpdateTime(source.getCreationTime());
		
		return view;
	}

}
