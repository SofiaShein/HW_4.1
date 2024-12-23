package ua.kiev.prog;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws SQLException {


        try (Connection conn = ConnectionFactory.getConnection()) {


            ApartmentDAOImpl dao = new ApartmentDAOImpl(conn, "Apartments");
            dao.createTable(Apartment.class);

            Scanner sc = new Scanner(System.in);
            dao.showApartmentByPriceAndRooms(sc);


        }

    }
    }