package com.monorama.medicine.model;

import lombok.Data;

@Data
public class Medicine {
	private String item_seq;
	private String item_name;
	private String entp_name;
	private String item_permit_date;
	private String etc_otc_code;
	private String chart;
	private String bar_code;
	private String material_name;
	private String ee_doc_id;
	private String ud_doc_id;
	private String nb_doc_id;
	private String storage_method;
	private String valid_term;
	private String reexam_target;
	private String reexam_date;
	private String pack_unit;
	private String edi_code;
	private String permit_kind_name;
	private String make_material_flag;
	private String newdrug_class_name;
	private String cancel_date;
	private String cancel_name;
	private String change_date;
	private String narcotic_kind_name;
}
