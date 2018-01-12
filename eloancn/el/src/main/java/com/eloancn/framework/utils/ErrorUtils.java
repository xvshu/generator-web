package com.eloancn.framework.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorUtils {

	public static String error(BindingResult result){
		if (result.hasErrors()) {
			List<FieldError> list=result.getFieldErrors();
			FieldError er=list.get(0);			
			String errorcode = er.getCode()+"."+er.getObjectName()+"."+er.getField();
			return SpringSourceConfig.getMessage(errorcode);
		}
		return null;			
	}
	public static String errors(BindingResult result){
		StringBuffer buf = new StringBuffer();
		if (result.hasErrors()) {
			
			List<FieldError> list=result.getFieldErrors();
			for(FieldError er:list){
				String errorcode = er.getCode()+"."+er.getObjectName()+"."+er.getField();
				String s =SpringSourceConfig.getMessage(errorcode);
				if(null!=s){
					buf.append(SpringSourceConfig.getMessage(errorcode)) ;
					buf.append("\n");
				}
			}				
		}
		return buf.toString();			
	}
	public static Map<String,String> erro(BindingResult result){
		 Map<String,String> msg= new LinkedHashMap<String,String>();
		if (result.hasErrors()) {		
			List<FieldError> list=result.getFieldErrors();
			for(FieldError er:list){
				String errorcode = er.getCode()+"."+er.getObjectName()+"."+er.getField();
				String s =SpringSourceConfig.getMessage(errorcode);
				if(null!=s){
					msg.put(er.getField(), s);
				}
			}				
		}
		return msg;			
	}
}
