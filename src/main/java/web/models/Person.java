package web.models;


import javax.validation.constraints.*;

public class Person {
private int id;
@NotEmpty(message = "Name should not be empty")
@Size(min = 2,max = 30, message = "name should be between 2 and 30 character")
private String name;
@Min(value = 0, message = "Age should be greater than 0")
@Max(value = 30,message = "max age 30 ")
private  int age;
@NotEmpty(message = "Email should not be empty")
@Email(message = "Email should be valid")
private String email;

  public   Person(){

    }

    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
