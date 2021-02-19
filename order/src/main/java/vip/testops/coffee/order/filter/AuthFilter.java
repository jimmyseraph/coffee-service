package vip.testops.coffee.order.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;
import vip.testops.coffee.order.api.AccountApi;
import vip.testops.coffee.order.entities.ResponseEntity;
import vip.testops.coffee.order.entities.dto.AccountDTO;
import vip.testops.coffee.order.utils.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = "/order/*")
@Order(1)
@Slf4j
@Repository
public class AuthFilter implements Filter {

    private AccountApi accountApi;

    @Autowired
    public void setAccountApi(AccountApi accountApi){
        this.accountApi = accountApi;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init auth filter...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("checking the token");
        String token = request.getHeader("Access-Token");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<Object> responseEntity = new ResponseEntity<>();
        if(StringUtils.isEmptyOrNull(token)){
            responseEntity.setCode(2001);
            responseEntity.setMessage("Access-Token cannot be null.");
            log.error("access-token is null");
            PrintWriter out = response.getWriter();
            out.append(objectMapper.writeValueAsString(responseEntity));
            out.flush();
            return;
        }
        ResponseEntity<AccountDTO> resp = accountApi.authorize(token);
        if(resp.getCode() != 1000){
            log.info("Auth failed!");
            PrintWriter out = response.getWriter();
            out.append(objectMapper.writeValueAsString(resp));
            out.flush();
        } else {
            log.info("authorize success");
            AccountDTO accountDTO = resp.getData();
            request.setAttribute("accountDTO", accountDTO);
            filterChain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
        log.info("destroy filter...");
    }
}
