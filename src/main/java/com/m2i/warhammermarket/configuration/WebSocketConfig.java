package com.m2i.warhammermarket.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.m2i.warhammermarket.service.AuthChannelInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	
	 @Autowired
	    private AuthChannelInterceptor channelInterceptor;

	
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");//.withSockJS();(socksjs-noVite)
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/chatroom","/user","/notifications");
        registry.setUserDestinationPrefix("/user");

    }
    
    
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        // Add our interceptor for authentication/authorization
        registration.interceptors(channelInterceptor);

    }
}
