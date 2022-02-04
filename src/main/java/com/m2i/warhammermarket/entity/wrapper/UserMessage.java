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
		private String subject;
	    private String message;
		public boolean valid() {
			if(subject.matches("askcomm|askadm|askuser"))
			{
				if (message.length()>= 5 && message.length()< 255)return true; 
			}
			
			
			
			
			return false;
		}
		
		
		public String getMessage() {
			return message.replaceAll("(\r\n|\n)", "<br />");
		}	    
}
