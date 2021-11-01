package com.vivek.food.dtp.models;

import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTimeProcessRequest {

	@NotEmpty
	@Size(min = 2, max = 2, message = "Order Id must of 2 digits")
	private String orderId;
	
	@NotEmpty
	@Size(max = 7, message = "Currently we are not accomodate more than 7 slots.")
	private List<Character> listOfMeals;
	
	@DecimalMax(value = "16.625", message = "distance must be <= 16.625 kms")
	private Double distanceFromRest;
}
