package net.javapractice.banking_app.dto;

public record TransferDataDto(Long senderId, Long recieverId, Double amount) {
    
}
