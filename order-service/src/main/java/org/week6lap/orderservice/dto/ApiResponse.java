package org.week6lap.orderservice.dto;

/**
 * Generic response wrapper for REST APIs.
 *
 * @param success Indicates operation status (true for success, false for failure)
 * @param message Human-readable message for context
 * @param data    Optional payload (can be null on errors or deletions)
 */
public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {}
