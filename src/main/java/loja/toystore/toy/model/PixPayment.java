package loja.toystore.toy.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PixPayment {
    private String pixCode;
    private BigDecimal amount;
    private LocalDateTime expirationDate;
    private String qrCodeImage;


    // Construtores
    public PixPayment() {}

    public PixPayment(String pixCode, BigDecimal amount, LocalDateTime expirationDate, String qrCodeImage) {
        this.pixCode = pixCode;
        this.amount = amount;
        this.expirationDate = expirationDate;
        this.qrCodeImage = qrCodeImage;
    }

    // Getters e Setters
    public String getPixCode() {
        return pixCode;
    }

    public void setPixCode(String pixCode) {
        this.pixCode = pixCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(String qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }
}