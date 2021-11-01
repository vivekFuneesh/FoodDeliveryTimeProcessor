package com.vivek.food.dtp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTimeProcessResponse {

	private String orderId;
	private String message;
	private Boolean isConfirmed;
}
