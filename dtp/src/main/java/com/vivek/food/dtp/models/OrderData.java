package com.vivek.food.dtp.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderData {

	@NotEmpty
	@Size(min = 2, max = 2)
	private String orderId;
	
	@NotNull
	@Range(max = 150, min = 0, message = "0 <= delivery time <= 150 minutes")
	private Double timeToDeliver;
	
	@NotNull
	@Range(max = 7, min = 1, message = "1 <= Number of Cooking Slots <= 7")
	private Integer slotsRequired;
}
