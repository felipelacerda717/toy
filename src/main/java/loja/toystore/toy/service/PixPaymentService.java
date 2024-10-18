package loja.toystore.toy.service;

import loja.toystore.toy.model.PixPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PixPaymentService {

    private final QRCodeService qrCodeService;

    @Autowired
    public PixPaymentService(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    public PixPayment generatePixPayment(BigDecimal amount) {
        String pixCode = generatePixCode();
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1);
        String qrCodeImage = qrCodeService.generateQRCodeImage(pixCode, 200, 200);
        return new PixPayment(pixCode, amount, expirationDate, qrCodeImage);
    }

    private String generatePixCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}