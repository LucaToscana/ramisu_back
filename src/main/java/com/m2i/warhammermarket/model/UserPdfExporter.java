package com.m2i.warhammermarket.model;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;

import com.m2i.warhammermarket.entity.DAO.LineOfOrderDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

public class UserPdfExporter {

    private List<UsersInformationDAO> listUsersInfo;
    private List<UserDAO> listUsers;
    private List<LineOfOrderDAO> listOrders;

    public UserPdfExporter(List<UsersInformationDAO> listUsersInfo, List<UserDAO> listUsers, List<LineOfOrderDAO> listOrders) {
        this.listUsersInfo = listUsersInfo;
        this.listUsers = listUsers;
        this.listOrders = listOrders;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("id", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("mail", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("last_name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("first_name", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (UsersInformationDAO usersinfo : listUsersInfo) {
            table.addCell(String.valueOf(usersinfo.getId()));
            table.addCell(usersinfo.getLastName());
            table.addCell(usersinfo.getFirstName());
        }
        for(UserDAO user: listUsers) {
            table.addCell(user.getMail());
        }
        /*for(LineOfOrderDAO line: listOrders){
            table.addCell(line.getOrder());
            table.addCell(line.getProduct());
            table.addCell((line.getQuantity()));

        }*/
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Order details", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();
    }
}
