package ca.gbc.apigateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

@Configuration
@Slf4j
public class Routes {
    @Value("${services.product.url}")
    public String productServiceUrl;

    @Value("${services.order.url}")
    public String orderServiceUrl;
    @Value("${services.inventory.url}")
    public String inventoryServiceUrl;
    @Bean
    public RouterFunction<ServerResponse> productServiceRoute(){
        //printf("%s", .. variable);
        log.info("Initialising product service route with URL: {}",productServiceUrl);

        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product"), request -> {
                    log.info("Received request for product-service: {}",request.uri());

                    try {
                        ServerResponse response= HandlerFunctions.http(productServiceUrl).handle(request);
                        log.info("Respomse Status: {}",response.statusCode());
                        return response;
                    }catch (Exception e){
                        log.error("Error occured while routing to {}",e.getMessage(),e);
                        return ServerResponse.status(500).body("Error occured when routing to product-service");
                    }
                })
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute(){
        //printf("%s", .. variable);
        log.info("Initialising order service route with URL: {}",orderServiceUrl);

        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/order"), request -> {
                    log.info("Received request for order-service: {}",request.uri());

                    try {
                        ServerResponse response= HandlerFunctions.http(orderServiceUrl).handle(request);
                        log.info("Respomse Status: {}",response.statusCode());
                        return response;
                    }catch (Exception e){
                        log.error("Error occured while routing to {}",e.getMessage(),e);
                        return ServerResponse.status(500).body("Error occured when routing to order-service");
                    }
                })
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute(){
        //printf("%s", .. variable);
        log.info("Initialising inventory service route with URL: {}",inventoryServiceUrl);

        return GatewayRouterFunctions.route("inventory_service")
                .route(RequestPredicates.path("/api/inventory"), request -> {
                    log.info("Received request for inventory-service: {}",request.uri());

                    try {
                        ServerResponse response= HandlerFunctions.http(inventoryServiceUrl).handle(request);
                        log.info("Respomse Status: {}",response.statusCode());
                        return response;
                    }catch (Exception e){
                        log.error("Error occured while routing to {}",e.getMessage(),e);
                        return ServerResponse.status(500).body("Error occured when routing to inventory-service");
                    }
                })
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> productServiceSwagger(){
        return GatewayRouterFunctions.route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product_service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8082"))
                .filter(setPath("/api-docs"))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> orderServiceSwagger(){
        return GatewayRouterFunctions.route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order_service/v3/api-docs"),
                        HandlerFunctions.http(orderServiceUrl))
                .filter(setPath("/api-docs"))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwagger(){
        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory_service/v3/api-docs"),
                        HandlerFunctions.http(inventoryServiceUrl))
                .filter(setPath("/api-docs"))
                .build();
    }
}