package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.components;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component("urlBuilder")
public class UrlBuilderComponent {
    public String buildUrl(String... params) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return "";

        HttpServletRequest request = attrs.getRequest();
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromRequest(request);

        for (int i = 0; i < params.length; i += 2) {
            if (i + 1 < params.length) {
                builder.replaceQueryParam(params[i], params[i + 1]);
            }
        }
        return builder.build().encode().toUriString();
    }
}