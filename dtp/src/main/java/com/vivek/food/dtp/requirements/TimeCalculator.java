package com.vivek.food.dtp.requirements;

import org.springframework.http.ResponseEntity;

import com.vivek.food.dtp.models.DeliveryTimeProcessRequest;
import com.vivek.food.dtp.models.DeliveryTimeProcessResponse;

public interface TimeCalculator {

	public ResponseEntity<DeliveryTimeProcessResponse> calculateFeasibility(DeliveryTimeProcessRequest request);
}
