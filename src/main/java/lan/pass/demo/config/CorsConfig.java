//package lan.pass.demo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//@Configuration      //相当于xml配置文件
//public class Cors_config {
//    // 当前跨域请求最大有效时长。这里默认1天
//    private static final long MAX_AGE = 24 * 60 * 60;
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("http://localhost:8080"); // 1 设置访问源地址
//        corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
//        corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
//        corsConfiguration.setAllowCredentials(true); // 允许携带凭证
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.setMaxAge(MAX_AGE);
//        source.registerCorsConfiguration("/**", corsConfiguration); // 4 对接口配置跨域设置
///*        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        // 这个顺序很重要哦，为避免麻烦请设置在最前
//        bean.setOrder(0);*/
//        return new CorsFilter(source);
//    }
//}