package com.baeker.member.base.security.oauth2.model.converters;

public interface ProviderUserConverter<T, R> {
    R convert(T t);
}
