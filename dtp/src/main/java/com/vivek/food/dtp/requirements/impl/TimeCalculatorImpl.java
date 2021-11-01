package com.vivek.food.dtp.requirements.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vivek.food.dtp.models.DeliveryTimeProcessRequest;
import com.vivek.food.dtp.models.DeliveryTimeProcessResponse;
import com.vivek.food.dtp.models.OrderData;
import com.vivek.food.dtp.requirements.TimeCalculator;

@Service
public class TimeCalculatorImpl implements TimeCalculator {
	
	
	private BlockingQueue<OrderData> orderQueue; 
	private AtomicInteger remainingSlots = new AtomicInteger(7);
	
	public TimeCalculatorImpl() {
		orderQueue = new PriorityBlockingQueue<OrderData>(1, new OrderTimeComparator());
	}

	/**
	 * Assuming that WHOLE order will be prepared at any point of time, even if some slots are empty.
	 * It means that if an order requires 5 slots but 3 are available then also it will be put in queue.
	 * */
	@Override
	public ResponseEntity<DeliveryTimeProcessResponse> calculateFeasibility(DeliveryTimeProcessRequest request) {
		DeliveryTimeProcessResponse result;
		double maxTime=0;
		int cookingSlotConsumed=0;
		
		for(Character ch : request.getListOfMeals()) {
			if(ch=='M') {
				maxTime = Math.max(maxTime, 29);
				cookingSlotConsumed+=2;
			}
			else if(ch=='A') {
				maxTime = Math.max(maxTime, 17);
				cookingSlotConsumed++;
			}
			else {
				result = new DeliveryTimeProcessResponse(request.getOrderId(), "Invalid food code, should be either A(Appetiser) or M(Main Course) but found "+ch, Boolean.FALSE);
				return ResponseEntity.badRequest().body(result);
			}
			
		}
		if(cookingSlotConsumed>7) {
			result = new DeliveryTimeProcessResponse(request.getOrderId(), "Unable to process such a big order, it requires more staff/slots than available", Boolean.FALSE);
			return ResponseEntity.unprocessableEntity().body(result);
		}
		double time = request.getDistanceFromRest() * 8d + maxTime;
		
		if(time <= 150) {
			boolean add = false;
			String message = "Not accepting more orders now, please try after some time";
			
			if(remainingSlots.get() >= cookingSlotConsumed) {
				add = orderQueue.offer(new OrderData(request.getOrderId(), time, cookingSlotConsumed));
				if(!add) {
					result = new DeliveryTimeProcessResponse(request.getOrderId(), message, Boolean.FALSE);
					return ResponseEntity.unprocessableEntity().body(result);
				}
				remainingSlots.set(remainingSlots.get() - cookingSlotConsumed);
			}
			else {
				ArrayList<OrderData> list = new ArrayList<>();
				int newAvailableSlots = 0;
				double additionalTime = 0d;
				boolean feasibility = false;
				
				while(!orderQueue.isEmpty()) {
					OrderData order = orderQueue.poll();
					list.add(order);
					newAvailableSlots += order.getSlotsRequired();
					additionalTime += order.getTimeToDeliver();
					
					if(additionalTime + time > 150) {break;}
					
					if(remainingSlots.get() + newAvailableSlots >= cookingSlotConsumed) {feasibility = true; break;}
				}
				if(!feasibility) {
					orderQueue.addAll(list);
					result = new DeliveryTimeProcessResponse(request.getOrderId(), "Not feasible to process the order,"
							+ " too much preoccupied with previous orders and hence won't deliver on time."
							, Boolean.FALSE);
					return ResponseEntity.unprocessableEntity().body(result);
				}
				else {
					add = orderQueue.offer(new OrderData(request.getOrderId(), time + additionalTime, cookingSlotConsumed));
					if(!add) {
						result = new DeliveryTimeProcessResponse(request.getOrderId(), message, Boolean.FALSE);
						return ResponseEntity.unprocessableEntity().body(result);
					}
					time += additionalTime;
					remainingSlots.set(remainingSlots.get() + newAvailableSlots - cookingSlotConsumed);
				}
			}			
			result = new DeliveryTimeProcessResponse(request.getOrderId(), "Order Confirmed, Delivery time is "+ time + " minutes", Boolean.TRUE);
		}
		else {
			result = new DeliveryTimeProcessResponse(request.getOrderId(), "Orders with delivery time more than 2.5 hours are not fulfilled for the time being.", Boolean.FALSE);
			return ResponseEntity.unprocessableEntity().body(result);
		}
		return ResponseEntity.accepted().body(result);
	}

}

class OrderTimeComparator implements Comparator<OrderData>{

	@Override
	public int compare(OrderData o1, OrderData o2) {
		return (int)(o1.getTimeToDeliver() - o2.getTimeToDeliver());
	}
	
}
