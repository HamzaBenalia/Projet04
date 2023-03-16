package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import static com.mysql.cj.conf.PropertyKey.logger;
import static com.parkit.parkingsystem.integration.config.DataBaseTestConfig.getConnection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class DataBaseConfigTest {

    @Test
    public void closePreparedStatement() throws SQLException {
        DataBaseConfig db = new DataBaseConfig();
        PreparedStatement ps = mock(PreparedStatement.class);
        db.closePreparedStatement(ps);
        verify(ps).close();

    }

    @Test
    public void testClosePreparedStatementExceptionShouldBeThrown() throws SQLException {
        // Créer un objet PreparedStatement fictif
        PreparedStatement ps = mock(PreparedStatement.class);
        DataBaseConfig db = new DataBaseConfig();

        // Simuler une exception lors de l'appel à la méthode close de l'objet fictif
        doThrow(new SQLException("Simulated SQLException")).when(ps).close();

        // Appeler la méthode closePreparedStatement avec l'objet fictif
        db.closePreparedStatement(ps);
    }

    @Test
    public void closeResultSetTest() throws SQLException {

        DataBaseConfig db = new DataBaseConfig();
        ResultSet rs = mock(ResultSet.class);
        db.closeResultSet(rs);
        verify(rs).close();
    }

    @Test
    public void closeResultSetShouldThrowException() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        DataBaseConfig db = new DataBaseConfig();
        doThrow(new SQLException("Error while closing result set")).when(rs).close();
        db.closeResultSet(rs);
    }
    @Test
    public void closeConnectionTest() throws SQLException {
        DataBaseConfig db = new DataBaseConfig();
        Connection con = mock(Connection.class);
        db.closeConnection(con);
        verify(con).close();
    }

    @Test
    public void closeConnectionShouldThrowException() throws SQLException {

        DataBaseConfig db = new DataBaseConfig();
        Connection con = mock(Connection.class);
        doThrow(new SQLException("Error while closing connection")).when(con).close();
        db.closeConnection(con);
    }

   /*@Test
    public void testGetConnectionHandlesIOException() throws ClassNotFoundException, SQLException {
        // Renommer le fichier JDBCPASSWORD.txt pour provoquer une exception IOException
        File jdbcPasswordFile = new File("resources/JDBCPASSWORD.txt");
        jdbcPasswordFile.renameTo(new File("resources/JDBCPASSWORD.txt"));

        // Appeler la méthode getConnection()
        try {
            getConnection();

        } catch (Exception e) {
            // Vérifier que l'exception a été gérée correctement
            assertEquals("Access denied for user 'root'@'localhost' (using password: NO)", e.getMessage());

        } finally {
            jdbcPasswordFile.renameTo(new File("resources/JDBCPASSWORD.txt"));


        }
    }
*/


}
