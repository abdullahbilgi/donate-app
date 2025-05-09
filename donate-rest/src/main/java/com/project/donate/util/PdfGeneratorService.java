package com.project.donate.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.model.*;
import com.project.donate.repository.ProductRepository;
import com.project.donate.repository.UserRepository;
import com.project.donate.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class PdfGeneratorService {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    public ByteArrayInputStream generateCartPdf(Cart cart) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

         User user = userRepository.findUserByCart(cart)
                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        System.out.println("sssss");

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font footerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8);
            Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 24, new Color(0, 128, 0));
            Font marketFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // --- MARKETLERİ GRUPLA ---
            Map<Market, List<Product>> marketToProducts = new LinkedHashMap<>();

            for (CartProduct cartProduct : cart.getCartProducts()) {
                try {
                    Product product = productRepository.findById(cartProduct.getProduct().getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + cartProduct.getProduct().getId()));

                    Market market = product.getMarket(); // Artık ManyToOne, doğrudan erişiyoruz

                    if (market == null) {
                        throw new ResourceNotFoundException("Market not found for product: " + product.getId());
                    }

                    marketToProducts.computeIfAbsent(market, k -> new ArrayList<>()).add(product);
                } catch (ResourceNotFoundException ex) {
                    System.err.println("Hata: " + ex.getMessage());
                    // İstersen log kaydı al veya hata listesini topla
                } catch (Exception ex) {
                    System.err.println("Beklenmeyen bir hata oluştu: " + ex.getMessage());
                }
            }


            Market firstMarket = marketToProducts.keySet().iterator().next();
            Address address = firstMarket.getAddress();

            // --- HEADER TABLE (Logo + Market QR/adlar) ---
            PdfPTable headerTable = new PdfPTable(1); // sadece 1 kolon, sağ taraf kaldırıldı
            headerTable.setWidthPercentage(100);
            headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Sol üst: Logo + LASTBITE
            PdfPTable leftHeader = new PdfPTable(2);
            leftHeader.setWidths(new float[]{1f, 3f});
            leftHeader.setWidthPercentage(100);
            leftHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            try {
                Image logo = Image.getInstance(getClass().getClassLoader().getResource("lastbite.png"));
                logo.scaleAbsolute(60, 40);
                PdfPCell logoCell = new PdfPCell(logo, false);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                leftHeader.addCell(logoCell);
            } catch (Exception e) {
                log.warn("Logo yüklenemedi: {}", e.getMessage());
                leftHeader.addCell(new PdfPCell(new Phrase("LOGO", brandFont)));
            }

            PdfPCell brandCell = new PdfPCell(new Phrase("LASTBITE", brandFont));
            brandCell.setBorder(Rectangle.NO_BORDER);
            brandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            brandCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftHeader.addCell(brandCell);

            PdfPCell leftCell = new PdfPCell(leftHeader);
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setVerticalAlignment(Element.ALIGN_TOP);
            headerTable.addCell(leftCell); // sadece sol taraf ekleniyor

            document.add(headerTable);
            document.add(new Paragraph(" "));


            // --- ORDER DETAILS ---
            Paragraph title = new Paragraph("Order Details", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            infoTable.addCell(new Phrase("Order Number: " + cart.getId(), bodyFont)); // todo burası degistirilcek

            PdfPCell purchaseDateCell = new PdfPCell(new Phrase("Purchase Date: " + cart.getPurchaseDate().format(formatter), bodyFont));
            purchaseDateCell.setBorder(Rectangle.NO_BORDER);
            purchaseDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            infoTable.addCell(purchaseDateCell);

            String fullName = user.getName() + " " + user.getSurname();
            infoTable.addCell(new Phrase("User: " + fullName, bodyFont));

            PdfPCell expiredDateCell = new PdfPCell(new Phrase("Expired Date: " + cart.getExpiredDate().format(formatter), bodyFont));
            expiredDateCell.setBorder(Rectangle.NO_BORDER);
            expiredDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            infoTable.addCell(expiredDateCell);

            document.add(infoTable);
            document.add(new Paragraph(" "));

            // --- PRODUCTS PER MARKET ---
            for (Map.Entry<Market, List<Product>> entry : marketToProducts.entrySet()) {
                Market market = entry.getKey();
                List<Product> products = entry.getValue();

                Address mAddress = market.getAddress();
                String mapsUrl = "https://www.google.com/maps?q=" + mAddress.getLatitude() + "," + mAddress.getLongitude();

                // Yeni tablo: Market ismi ve QR kodu yan yana
                PdfPTable marketHeaderTable = new PdfPTable(2);
                marketHeaderTable.setWidthPercentage(100);
                marketHeaderTable.setWidths(new float[]{7f, 1.5f}); // Ayar: isim geniş, QR dar
                marketHeaderTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                // Market adı hücresi
                PdfPCell marketNameCell = new PdfPCell(new Phrase("Market: " + market.getName(), titleFont));
                marketNameCell.setBorder(Rectangle.NO_BORDER);
                marketNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                marketNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                marketHeaderTable.addCell(marketNameCell);

                // QR kod hücresi
                Image qrImage = generateQRCodeImage(mapsUrl, 50, 50);
                PdfPCell qrCell = new PdfPCell(qrImage, false);
                qrCell.setBorder(Rectangle.NO_BORDER);
                qrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                marketHeaderTable.addCell(qrCell);

                // PDF'e market başlığı + QR ekle
                document.add(marketHeaderTable);

                // Market adresi
                String fullMarketAddress = mAddress.getName() + ", " + mAddress.getRegion().getName() + " - " + mAddress.getZipCode();
                Paragraph addressPara = new Paragraph("Address: " + fullMarketAddress, bodyFont);
                document.add(addressPara);
                document.add(new Paragraph(" ", bodyFont));

                // Ürün tablosu
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{2, 2, 2});

                Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                table.addCell(new PdfPCell(new Phrase("Product", headFont)));
                table.addCell(new PdfPCell(new Phrase("Quantity", headFont)));
                table.addCell(new PdfPCell(new Phrase("Price", headFont)));

                List<CartProduct> cartProducts = cart.getCartProducts();
                for (CartProduct cartProduct : cartProducts) {
                    Product product = productService.getProductEntityById(cartProduct.getProduct().getId());

                    // Sadece bu markete ait ürünleri göster
                    if (!product.getMarket().equals(market)) continue;

                    table.addCell(new Phrase(product.getName(), bodyFont));
                    table.addCell(new Phrase(String.valueOf(cartProduct.getProductQuantity())));
                    table.addCell(new Phrase((cartProduct.getProductPrice()) + " TL", bodyFont));
                }

                document.add(table);
                document.add(new Paragraph(" "));
            }



            Paragraph totalPrice = new Paragraph("Total Price: " + cart.getTotalPrice() + " TL", bodyFont);
            totalPrice.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalPrice);
            document.add(new Paragraph(" "));

            // --- FOOTER ---
            Paragraph footer = new Paragraph("Thanks for shopping\nHas no financial value\nDocument is for informational purposes", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (DocumentException | IOException | WriterException e) {
            log.error("Error while creating PDF", e);
            throw new RuntimeException("Error while creating PDF", e);
        }

        log.info("{} generated PDF of cart detail id: {}", GeneralUtil.extractUsername(), cart.getId());

        return new ByteArrayInputStream(out.toByteArray());
    }

    public Image generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return Image.getInstance(pngOutputStream.toByteArray());
    }
}
