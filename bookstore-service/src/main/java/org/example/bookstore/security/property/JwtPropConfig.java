package org.example.bookstore.security.property;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "jwt")
public record JwtPropConfig(
    @NotNull
    @Min(600_000)
    Long ttl,
    @NotBlank
    String issuer,
    @NotBlank
    String secret
) {
}
