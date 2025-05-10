package servlet;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class SessionCheckFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String referrer = httpRequest.getHeader("referer");
        String requestURI = httpRequest.getRequestURI();
        boolean isLoginPage = requestURI.endsWith("/userLogin/login.jsp");
        boolean isLogoutServlet = requestURI.endsWith("/LogoutServlet");
        boolean isSameOrigin = referrer != null && referrer.contains(httpRequest.getContextPath());

        // Skip filter for LogoutServlet to prevent redirect loop
        if (isLogoutServlet) {
            chain.doFilter(request, response);
            return;
        }

        // Allow login.jsp to load without redirection, or if no session exists, or if referrer is valid
        if (isLoginPage || session == null || (referrer != null && isSameOrigin)) {
            chain.doFilter(request, response);
            return;
        }

        // Redirect to LogoutServlet for all other pages (including index.jsp) if session exists and referrer is invalid
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/LogoutServlet");

    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}