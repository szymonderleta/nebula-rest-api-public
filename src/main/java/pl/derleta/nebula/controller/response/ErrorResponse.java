package pl.derleta.nebula.controller.response;

import java.time.LocalDateTime;

public record ErrorResponse(String message,
                            String error,
                            LocalDateTime timestamp
) { }
