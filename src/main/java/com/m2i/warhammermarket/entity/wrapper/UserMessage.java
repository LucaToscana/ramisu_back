package com.m2i.warhammermarket.entity.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserMessage {
		private String email;
		private String message;
		private String captchaToken;
	    
	
		
		public String getMessage() {
			return message.replaceAll("(\r\n|\n)", "<br />");
		}


}
