package com.monorama.medicine.model;

import com.monorama.medicine.StatusEnum;

import lombok.Data;

@Data
public class Message {

	private String code;
	
    private String result;
	
    private Object data;

    public Message() {
        this.code = StatusEnum.OK.getStatusCode();
        this.data = null;
        this.result = "success";
    }

}
