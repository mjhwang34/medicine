package com.monorama.medicine.model;

import java.util.List;

import lombok.Data;

@Data
public class SearchMedicineParam {
	private List<String> item_seq_list;
	private List<String> edi_code_list;
	private String item_name;
	private String entp_name;
	private String shape;
	private List<String> line_list;
	private List<String> color_list;
}
