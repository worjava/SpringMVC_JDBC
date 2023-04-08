package web.dao;

import org.springframework.stereotype.Component;
import web.models.Person;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

@Component // спринг создаст бин этого класса
public class PersonDAO { // будет общаться с БД работать со списком людей
    private static int PEOPLE_COUNT;

    private static String URL = "jdbc:mysql://localhost:3306/cinema";
    private static String user = "root";
    private static String password = "root";


    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, user, password);
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public List<Person> index() throws SQLException { // return all people
        List<Person> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *from Person");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Person person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setEmail(resultSet.getString("email"));
                person.setAge(resultSet.getInt("age"));
                list.add(person);
            }

        } finally {
//            connection.close();
        }

        return list;
    }

    public Person show(int id) {
        Person person = new Person();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("SELECT * FROM Person WHERE id =?");

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return person;
    }

    public void save(Person person) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Person (name, age, email) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.executeUpdate();

            // получение сгенерированного id
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                person.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Error saving person:");
            System.out.println("Name: " + person.getName());
            System.out.println("Age: " + person.getAge());
            System.out.println("Email: " + person.getEmail());
            e.printStackTrace();
        }
    }


    public void update(int id, Person person) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE Person SET name = ?, age = ?, email = ? WHERE id = ?");
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Person WHERE id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//
//        people.removeIf(a -> a.getId() == id);
}
