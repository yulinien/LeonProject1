package com.example.leonproject.config.i18n;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import java.util.Locale;
import java.util.Optional;


@Configuration
public class MyLocaleResolver implements LocaleResolver {

    private static final String PATH_PARAMETER = "lang";

    private static final String PATH_PARAMETER_SPLIT = "_";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String lang = request.getHeader(PATH_PARAMETER);
        Locale locale = request.getLocale();

        return Optional.ofNullable(lang)
                .map(l -> l.split(PATH_PARAMETER_SPLIT))
                .filter(split -> split.length == 2)
                .map(split -> new Locale(split[0], split[1]))
                .orElse(locale);
        // 其實直接return locale也可
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
