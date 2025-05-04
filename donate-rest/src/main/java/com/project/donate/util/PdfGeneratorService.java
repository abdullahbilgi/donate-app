package com.project.donate.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.project.donate.dto.CartDTO;
import com.project.donate.dto.Response.CartResponse;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.model.Address;
import com.project.donate.model.Market;
import com.project.donate.model.Product;
import com.project.donate.model.User;
import com.project.donate.records.ProductItem;
import com.project.donate.repository.ProductRepository;
import com.project.donate.repository.UserRepository;
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
    private final UserRepository userRepository;

    public ByteArrayInputStream generateCartPdf(CartResponse cartDTO) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Optional<User> user = userRepository.findById(cartDTO.getUser().getId());

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
            for (ProductItem item : cartDTO.getProductItems()) {
                Product product = productRepository.findById(item.productId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.productId()));
                Market market = product.getMarkets().stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Market not found for product: " + product.getId()));
                marketToProducts.computeIfAbsent(market, k -> new ArrayList<>()).add(product);
            }

            Market firstMarket = marketToProducts.keySet().iterator().next();
            Address address = firstMarket.getAddress();

            // --- HEADER TABLE (Logo + Market QR/adlar) ---
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{6f, 4f});
            headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Sol üst: Logo + LASTBITE
            PdfPTable leftHeader = new PdfPTable(2);
            leftHeader.setWidths(new float[]{1f, 3f});
            leftHeader.setWidthPercentage(100);
            leftHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            Image logo = Image.getInstance(getClass().getClassLoader().getResource("lastbite.png"));
            logo.scaleAbsolute(60, 40);
            PdfPCell logoCell = new PdfPCell(logo, false);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftHeader.addCell(logoCell);

            PdfPCell brandCell = new PdfPCell(new Phrase("LASTBITE", brandFont));
            brandCell.setBorder(Rectangle.NO_BORDER);
            brandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            brandCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftHeader.addCell(brandCell);

            PdfPCell leftCell = new PdfPCell(leftHeader);
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setVerticalAlignment(Element.ALIGN_TOP);
            headerTable.addCell(leftCell);

            // Sağ üst: Market QR kodları ve adları (alt alta)
            PdfPTable rightHeader = new PdfPTable(1);
            rightHeader.setWidthPercentage(100);
            rightHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            for (Map.Entry<Market, List<Product>> entry : marketToProducts.entrySet()) {
                Market market = entry.getKey();
                Address mAddress = market.getAddress();
                String mapsUrl = "https://www.google.com/maps?q=" + mAddress.getLatitude() + "," + mAddress.getLongitude();
                Image qrImage = generateQRCodeImage(mapsUrl, 60, 60);
                qrImage.setAlignment(Image.ALIGN_RIGHT);


                PdfPCell qrCell = new PdfPCell(qrImage, false);
                qrCell.setBorder(Rectangle.NO_BORDER);
                qrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                rightHeader.addCell(qrCell);

                Paragraph marketName = new Paragraph(market.getName(), marketFont);
                marketName.setAlignment(Element.ALIGN_RIGHT);

                PdfPCell nameCell = new PdfPCell(marketName);
                nameCell.setBorder(Rectangle.NO_BORDER);
                nameCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                //nameCell.setPaddingRight(20f);
                rightHeader.addCell(nameCell);
            }

            PdfPCell rightCell = new PdfPCell(rightHeader);
            rightCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(rightCell);

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

            infoTable.addCell(new Phrase("Order Number: " + cartDTO.getId(), bodyFont));

            PdfPCell purchaseDateCell = new PdfPCell(new Phrase("Purchase Date: " + cartDTO.getPurchaseDate().format(formatter), bodyFont));
            purchaseDateCell.setBorder(Rectangle.NO_BORDER);
            purchaseDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            infoTable.addCell(purchaseDateCell);

            String fullName = user.get().getName() + " " + user.get().getSurname();
            infoTable.addCell(new Phrase("User: " + fullName, bodyFont));

            PdfPCell expiredDateCell = new PdfPCell(new Phrase("Expired Date: " + cartDTO.getExpiredDate().format(formatter), bodyFont));
            expiredDateCell.setBorder(Rectangle.NO_BORDER);
            expiredDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            infoTable.addCell(expiredDateCell);

            document.add(infoTable);
            document.add(new Paragraph(" "));

            // --- PRODUCTS PER MARKET ---
            for (Map.Entry<Market, List<Product>> entry : marketToProducts.entrySet()) {
                Market market = entry.getKey();
                List<Product> products = entry.getValue();

                // Market başlığı
                Paragraph marketTitle = new Paragraph("Market: " + market.getName(), titleFont);
                marketTitle.setSpacingBefore(10);
                document.add(marketTitle);

                // Market adresi
                Address mAddress = market.getAddress();
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

                for (Product product : products) {
                    Optional<ProductItem> itemOpt = cartDTO.getProductItems().stream()
                            .filter(p -> p.productId().equals(product.getId()))
                            .findFirst();

                    if (itemOpt.isEmpty()) continue;
                    ProductItem item = itemOpt.get();

                    table.addCell(new Phrase(product.getName(), bodyFont));
                    table.addCell(new Phrase(String.valueOf(item.quantity()), bodyFont));
                    table.addCell(new Phrase((product.getPrice() * item.quantity()) + " TL", bodyFont));
                }

                document.add(table);
                document.add(new Paragraph(" "));
            }


            Paragraph totalPrice = new Paragraph("Total Price: " + cartDTO.getTotalPrice() + " TL", bodyFont);
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

        log.info("{} generated PDF of cart detail id: {}", GeneralUtil.extractUsername(), cartDTO.getId());

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
