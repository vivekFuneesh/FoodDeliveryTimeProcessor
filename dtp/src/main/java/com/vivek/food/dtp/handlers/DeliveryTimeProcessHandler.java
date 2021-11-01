package com.vivek.food.dtp.handlers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vivek.food.dtp.models.DeliveryTimeProcessRequest;
import com.vivek.food.dtp.models.DeliveryTimeProcessResponse;
import com.vivek.food.dtp.requirements.TimeCalculator;

@RestController
@RequestMapping(path = "/v1/food/delivery/time")
public class DeliveryTimeProcessHandler {

	@Autowired
	TimeCalculator timeCalculator;
	
	@RequestMapping("/calculate")
	@PostMapping
	public ResponseEntity<DeliveryTimeProcessResponse> confirmTime(@RequestBody @Valid DeliveryTimeProcessRequest request) {
		return timeCalculator.calculateFeasibility(request);
	}
}
