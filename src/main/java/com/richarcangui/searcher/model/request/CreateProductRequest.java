package com.richarcangui.searcher.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

	private String image;
	private String name;
	private String description;
	private String price;
	private String type;
	private String category;
}
